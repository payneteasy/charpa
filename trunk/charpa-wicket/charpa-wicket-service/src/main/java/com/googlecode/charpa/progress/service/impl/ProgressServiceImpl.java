package com.googlecode.charpa.progress.service.impl;

import com.googlecode.charpa.progress.service.*;
import com.googlecode.charpa.progress.service.exception.ProgressNotFoundException;
import com.googlecode.charpa.progress.service.spi.IProgressStorageStrategy;
import com.googlecode.charpa.progress.service.spi.IResourceResolver;
import com.googlecode.charpa.progress.service.spi.ISecurityService;

import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Progress service
 */
public class ProgressServiceImpl implements IProgressInfoService, IProgressManagerService {

    final Logger LOG = LoggerFactory.getLogger(ProgressServiceImpl.class);

    public ProgressServiceImpl() {
        ProgressId id = createProgressId("Start system");
        startProgress(id, "Start system", 1);
        incrementProgressValue(id);
        finishProgress(id);
    }
    
    public void setStorageStrategies(Map<String, IProgressStorageStrategy> aStorageStrategies) {
    	theStorageStrategies = aStorageStrategies;
    }
    
    public void setDefaultStorageStrategy(IProgressStorageStrategy aStrategy) {
    	theDefaultStorageStrategy = aStrategy;
    }
    
    public void setResourceResolver(IResourceResolver aResourceResolver) {
    	theResourceResolver = aResourceResolver;
    }
    
    public void setSecurityService(ISecurityService aSecurityService) {
		theSecurityService = aSecurityService;
	}

	public void setDefaultQualifier(String aQualifier) {
    	theDefaultQualifier = aQualifier;
    }
    
    public void setRemoveThresholdMillis(long aMillis) {
    	theRemoveThresholdMillis = aMillis;
    }
    
    protected IProgressStorageStrategy selectStorageStrategy(ProgressId aProgressId) {
    	for (Entry<String, IProgressStorageStrategy> entry : theStorageStrategies.entrySet()) {
    		if (aProgressId.toString().startsWith(entry.getKey())) {
    			return entry.getValue();
    		}
    	}
    	return theDefaultStorageStrategy;
    }

    public ProgressId createProgressId(String aName) {
        return createProgressId(aName, Collections.<String, String>emptyMap());
    }
    
    public ProgressId createProgressId(String aName, String aQualifier) {
    	return createProgressId(aName, Collections.<String, String>emptyMap(), aQualifier);
    }
    
    public ProgressId createProgressId(String aName, Map<String, String> aPageParameters) {
    	return createProgressId(aName, aPageParameters, theDefaultQualifier);
    }
    
    public ProgressId createProgressId(String aName, Map<String, String> aPageParameters, String aQualifier) {
        ProgressId id = new ProgressId(aQualifier + UUID.randomUUID().toString());
        ProgressInfo info = new ProgressInfo(id, aName, getStartingMessage(),
        		theSecurityService.getCurrentSecurityInfo(), aPageParameters);
        selectStorageStrategy(id).createProgress(id, info);
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: CREATED [ {} ]", id, aName);
        }
        return id;
    }

	private String getStartingMessage() {
		return theResourceResolver.resolve("starting", "Starting...");
	}

    ////////////////////////////////
    // INFO INTERFACE
    //
    public void invoke(ProgressId aProgressId, Runnable aRunnable) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: INVOKED", aProgressId);
        }
        theExecutor.execute(aRunnable);
    }

    public IProgressInfo getProgressInfo(ProgressId aProgressId) {
        ProgressInfo info = findProgress(aProgressId);
        return createReadOnlyProgressInfo(info);
    }

    private IProgressInfo createReadOnlyProgressInfo(ProgressInfo aProgressInfo) {
        Period elapsedPeriod ;
        if(aProgressInfo.getStartedTime()!=null) {
            Date end = aProgressInfo.getEndedTime()!=null ? aProgressInfo.getEndedTime() : new Date();
            elapsedPeriod = new Period(end.getTime() - aProgressInfo.getStartedTime().getTime());
        } else {
            elapsedPeriod = null;
        }

        Period leftPeriod ;
        if(aProgressInfo.getStartedTime()!=null
                && aProgressInfo.getMax()>0
                && aProgressInfo.getCurrentValue()>0
                && aProgressInfo.getMax() != aProgressInfo.getCurrentValue()
                && elapsedPeriod!=null
                && aProgressInfo.getEndedTime()==null) {
            leftPeriod = new Period(aProgressInfo.getLeftTime());
        } else {
            leftPeriod = null;
        }

        return new ImmutableProgressInfo(aProgressInfo.getId(), aProgressInfo.getName(), aProgressInfo.getMax()
                , aProgressInfo.getProgressText()
                , aProgressInfo.getCurrentValue()
                , aProgressInfo.getState()
                , aProgressInfo.getPageParameters()
                , aProgressInfo.getCreatedTime()
                , aProgressInfo.getStartedTime()
                , aProgressInfo.getEndedTime()
                , elapsedPeriod
                , leftPeriod
        );
    }

    public List<IProgressInfo> listProgresses() {
    	return listProgresses(null);
    }
    
	public List<IProgressInfo> listProgresses(String name) {
        ArrayList<IProgressInfo> list = new ArrayList<IProgressInfo>();
        for (IProgressStorageStrategy strategy : theStorageStrategies.values()) {
            for (ProgressInfo info : strategy.listProgresses(name)) {
                list.add(createReadOnlyProgressInfo(info));
            }
        }
        if (!theStorageStrategies.containsValue(theDefaultStorageStrategy)) {
            for (ProgressInfo info : theDefaultStorageStrategy.listProgresses(name)) {
                list.add(createReadOnlyProgressInfo(info));
            }
        }

        Collections.sort(list, new Comparator<IProgressInfo>() {
            public int compare(IProgressInfo o1, IProgressInfo o2) {
                return o2.getCreatedTime().compareTo(o1.getCreatedTime());
            }
        });
        return list;
	}

    public void cancelProgress(ProgressId aProgressId) {
    	selectStorageStrategy(aProgressId).cancelProgress(aProgressId);
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: CANCELLED", aProgressId);
        }
    }
    
	public List<LogMessage> getLastLogMessages(ProgressId aId, int aCount) {
		return selectStorageStrategy(aId).listLatestLogMessages(aId, aCount);
	}

    ////////////////////////////////
    // MANAGER INTERFACE
    //

    public void startProgress(ProgressId aProgressId, String aProgressName, int aMaxValue) {
    	selectStorageStrategy(aProgressId).startProgress(aProgressId, aProgressName, aMaxValue);
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: STARTED [ {} ]", aProgressId, aProgressName);
        }
    }

    public void setProgressText(ProgressId aProgressId, String aProgressText) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: text: {}", aProgressId, aProgressText);
        }
        selectStorageStrategy(aProgressId).changeProgressText(aProgressId, aProgressText);
    }

    public void incrementProgressValue(ProgressId aProgressId) {
    	incrementProgressValue(aProgressId, 1);
    }
    
    public void incrementProgressValue(ProgressId aProgressId, int aDelta) {
    	selectStorageStrategy(aProgressId).incrementProgressValue(aProgressId, aDelta);
    }

    public void finishProgress(ProgressId aProgressId) {
    	selectStorageStrategy(aProgressId).finishProgress(aProgressId);
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: FINISHED", aProgressId);
        }
    }

    public boolean isCancelled(ProgressId aProgressId) {
        return selectStorageStrategy(aProgressId).isCancelled(aProgressId);
    }

    public boolean isRunning(ProgressId progressId) {
        return selectStorageStrategy(progressId).isRunning(progressId);
    }
    
	public boolean isStarted(ProgressId progressId) {
		return selectStorageStrategy(progressId).isStarted(progressId);
	}

    public void setName(ProgressId aProgressId, String name) {
    	selectStorageStrategy(aProgressId).changeProgressName(aProgressId, name);   
    }
    
    public void progressFailed(ProgressId aProgressId, Exception aException) {
        LOG.error(aProgressId+": "+aException.getMessage(), aException);

        selectStorageStrategy(aProgressId).progressFailed(aProgressId, aException);
        
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: FAILED", aProgressId);
        }
    }

    public void info(ProgressId aProgressId, String aInfoMessage) {
    	selectStorageStrategy(aProgressId).addInfoMessage(aProgressId, aInfoMessage);
    }

    public void error(ProgressId aProgressId, String aErrorMessage) {
    	selectStorageStrategy(aProgressId).addErrorMessage(aProgressId, aErrorMessage);
    }
    
	public void removeStaleProgresses() {
		Date olderThan = new Date(new Date().getTime() - theRemoveThresholdMillis);
		for (IProgressStorageStrategy strategy : theStorageStrategies.values()) {
			strategy.deleteStaleProgresses(olderThan);
		}
		if (!theStorageStrategies.containsValue(theDefaultStorageStrategy)) {
			theDefaultStorageStrategy.deleteStaleProgresses(olderThan);
		}
	}

    ////////////////////////////////
    // privates
    //

    /**
     * Finds progress info and throw exception if was not found
     * @param aProgressId progress id
     * @return progress info
     */
    private ProgressInfo findProgress(ProgressId aProgressId) {
        if(aProgressId==null) {
            throw new IllegalStateException("progress id is null");
        }

        ProgressInfo info = selectStorageStrategy(aProgressId).findProgress(aProgressId);

        if(info == null) {
            throw new ProgressNotFoundException("Progress with id "+aProgressId+" was not found");
        }

        return info;
    }

    private final Executor theExecutor = Executors.newSingleThreadExecutor();
    private Map<String, IProgressStorageStrategy> theStorageStrategies = new HashMap<String, IProgressStorageStrategy>();
    private IResourceResolver theResourceResolver = new DefaultResourceResolver();
    private IProgressStorageStrategy theDefaultStorageStrategy = new InMemoryStorageStrategy(new ISecurityServiceFactory() {
		public ISecurityService getSecurityService() {
			return theSecurityService;
		}
	});
    private ISecurityService theSecurityService = new NullSecurityService();
    private String theDefaultQualifier = "";
    private long theRemoveThresholdMillis = 3600 * 1000 * 24; // one full day
    
    static interface ISecurityServiceFactory {
    	ISecurityService getSecurityService();
    }
}

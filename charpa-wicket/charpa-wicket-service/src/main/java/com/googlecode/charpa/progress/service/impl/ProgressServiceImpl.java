package com.googlecode.charpa.progress.service.impl;

import com.googlecode.charpa.progress.service.*;
import com.googlecode.charpa.progress.service.spi.IProgressStorageStrategy;
import com.googlecode.charpa.progress.service.spi.InMemoryStorageStrategy;

import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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

    public ProgressId createProgressId(String aName) {
        return createProgressId(aName, Collections.<String, String>emptyMap());
    }
    
    public ProgressId createProgressId(String aName, Map<String, String> aPageParameters) {
        ProgressId id = new ProgressId(UUID.randomUUID().toString());
        ProgressInfo info = new ProgressInfo(id, aName, aPageParameters);
        theStorageStrategy.createProgress(id, info);
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: CREATED [ {} ]", id, aName);
        }
        return id;
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
                , aProgressInfo.getLogMessages()
        );
    }

    public List<IProgressInfo> listProgresses() {
        ArrayList<IProgressInfo> list = new ArrayList<IProgressInfo>();
        for (ProgressInfo info : theStorageStrategy.listProgresses()) {
            list.add(createReadOnlyProgressInfo(info));
        }

        Collections.sort(list, new Comparator<IProgressInfo>() {
            public int compare(IProgressInfo o1, IProgressInfo o2) {
                return o2.getCreatedTime().compareTo(o1.getCreatedTime());
            }
        });
        return list;
    }

    public void cancelProgress(ProgressId aProgressId) {
        theStorageStrategy.cancelProgress(aProgressId);
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: CANCELLED", aProgressId);
        }
    }

    ////////////////////////////////
    // MANAGER INTERFACE
    //

    public void startProgress(ProgressId aProgressId, String aProgressName, int aMaxValue) {
        theStorageStrategy.startProgress(aProgressId, aProgressName, aMaxValue);
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: STARTED [ {} ]", aProgressId, aProgressName);
        }
    }

    public void setProgressText(ProgressId aProgressId, String aProgressText) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: text: {}", aProgressId, aProgressText);
        }
        theStorageStrategy.changeProgressText(aProgressId, aProgressText);
    }

    public void incrementProgressValue(ProgressId aProgressId) {
        theStorageStrategy.incrementProgressValue(aProgressId);
    }

    public void finishProgress(ProgressId aProgressId) {
        theStorageStrategy.finishProgress(aProgressId);
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: FINISHED", aProgressId);
        }
    }

    public boolean isCancelled(ProgressId aProgressId) {
        return theStorageStrategy.isCancelled(aProgressId);
    }

    public void setName(ProgressId aProgressId, String name) {
        theStorageStrategy.changeProgressName(aProgressId, name);   
    }
    
    public void progressFailed(ProgressId aProgressId, Exception aException) {
        LOG.error(aProgressId+": "+aException.getMessage(), aException);

        theStorageStrategy.progressFailed(aProgressId, aException);
        
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: FAILED", aProgressId);
        }
    }

    public void info(ProgressId aProgressId, String aInfoMessage) {
    	theStorageStrategy.addInfoMessage(aProgressId, aInfoMessage);
    }

    public void error(ProgressId aProgressId, String aErrorMessage) {
    	theStorageStrategy.addErrorMessage(aProgressId, aErrorMessage);
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

        ProgressInfo info = theStorageStrategy.findProgress(aProgressId);

        if(info == null) {
            throw new IllegalStateException("Progress with id "+aProgressId+" was not found");
        }

        return info;
    }

    private final Executor theExecutor = Executors.newSingleThreadExecutor();
    private IProgressStorageStrategy theStorageStrategy = new InMemoryStorageStrategy();
}

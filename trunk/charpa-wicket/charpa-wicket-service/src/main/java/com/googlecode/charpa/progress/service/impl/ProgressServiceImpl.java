package com.googlecode.charpa.progress.service.impl;

import com.googlecode.charpa.progress.service.*;
import org.apache.commons.lang.StringUtils;
import org.joda.time.Period;
import org.springframework.util.Assert;
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
        return createProgressId(aName, Collections.EMPTY_MAP);
    }
    
    public ProgressId createProgressId(String aName, Map<String, String> aPageParameters) {
        ProgressId id = new ProgressId(UUID.randomUUID().toString());
        ProgressInfo info = new ProgressInfo(id, aName, aPageParameters);
        theProgresses.put(id, info);
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: CREATED [ {} ]", id.toString(), aName);
        }
        return id;
    }

    ////////////////////////////////
    // INFO INTERFACE
    //
    public void invoke(ProgressId aProgressId, Runnable aRunnable) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: INVOKED", aProgressId.toString());
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
                , StringUtils.left(aProgressInfo.getProgressText(), 100)
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
        ArrayList<IProgressInfo> list = new ArrayList<IProgressInfo>();
        for (ProgressInfo info : theProgresses.values()) {
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
        ProgressInfo info = findProgress(aProgressId);
        info.setState(ProgressState.CANCELLED);
        info.setEndedTime(new Date());
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: CANCELLED", aProgressId.toString());
        }
    }

    ////////////////////////////////
    // MANAGER INTERFACE
    //

    public void startProgress(ProgressId aProgressId, String aProgressName, int aMaxValue) {
        ProgressInfo info = findProgress(aProgressId);
        info.setName(aProgressName);
        info.setMax(aMaxValue);
        info.setProgressText(""); // removes "Starting ..." text
        info.setStartedTime(new Date());
        info.setState(ProgressState.RUNNING);
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: STARTED [ {} ]", aProgressId.toString(), aProgressName);
        }
    }

    public void setProgressText(ProgressId aProgressId, String aProgressText) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: text: {}", aProgressId.toString(), aProgressText);
        }
        findProgress(aProgressId).setProgressText(aProgressText);
    }

    public void incrementProgressValue(ProgressId aProgressId) {
        findProgress(aProgressId).incrementValue();
    }

    public void finishProgress(ProgressId aProgressId) {
        ProgressInfo info = findProgress(aProgressId);
        info.setEndedTime(new Date());
        info.setState(ProgressState.FINISHED);
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: FINISHED", aProgressId.toString());
        }
    }

    public boolean isCancelled(ProgressId aProgressId) {
        ProgressInfo info = findProgress(aProgressId);
        return info.getState() == ProgressState.CANCELLED;
    }

    public void setName(ProgressId aProgressId, String name) {
        ProgressInfo info = findProgress(aProgressId);
        info.setName(name);   
    }
    
    public void progressFailed(ProgressId aProgressId, Exception aException) {
        ProgressInfo info = findProgress(aProgressId);
        info.setState(ProgressState.FAILED);
        info.setEndedTime(new Date());
        info.setProgressText(aException.getMessage());
        if(LOG.isDebugEnabled()) {
            LOG.debug("{}: FAILED", aProgressId.toString());
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
        ProgressInfo info = theProgresses.get(aProgressId);
        Assert.notNull(info, "Progress with id "+aProgressId+" was not found");
        return info;
    }

    private Map<ProgressId, ProgressInfo> theProgresses = Collections.synchronizedMap(new HashMap<ProgressId, ProgressInfo>());
    private final Executor theExecutor = Executors.newSingleThreadExecutor();
}

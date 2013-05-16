package com.googlecode.charpa.progress.service;

import java.util.List;
import java.util.Map;

/**
 * Progress service
 */
public interface IProgressInfoService {

    ProgressId createProgressId(String aName);

    ProgressId createProgressId(String aName, Map<String, String> aPageParameters);
    
    ProgressId createProgressId(String aName, String aQualifier);

    ProgressId createProgressId(String aName, Map<String, String> aPageParameters, String aQualifier);

    void invoke(ProgressId aProgressId, Runnable aRunnable);

    IProgressInfo getProgressInfo(ProgressId aProgressId);

    List<IProgressInfo> listProgresses();
    
    List<IProgressInfo> listProgresses(String name);

    /**
     * Cancels progress
     * @param aProgressId progress id
     */
    void cancelProgress(ProgressId aProgressId);

    /**
     * Get last messages
     * @param aCount count messages
     * @return messages
     */
    List<LogMessage> getLastLogMessages(ProgressId aId, int aCount) ;

}

package com.googlecode.charpa.progress.service;

/**
 * Manages progress
 */
public interface IProgressManagerService {
    void startProgress(ProgressId aProgressId, String aProgressName, int aMaxValue);

    void setProgressText(ProgressId aProgressId, String aProgressText);

    void incrementProgressValue(ProgressId aProgressId);

    void progressFailed(ProgressId aProgressId, Exception aException);

    void finishProgress(ProgressId aProgressId);

    /**
     * Is progress cancelled
     * @param aProgressId progress id
     * @return true if cancelled
     */
    boolean isCancelled(ProgressId aProgressId);
    
    void setName(ProgressId aProgressId, String name);

    /**
     * Add info level message
     * @param aProgressId progress id
     * @param aInfoMessage message
     */
    void info(ProgressId aProgressId, String aInfoMessage);

    /**
     * Add error  message
     * @param aErrorMessage message
     * @param aProgressId progress id
     */
    void error(ProgressId aProgressId, String aErrorMessage);
    
    /**
     * Removes progresses which have been finished or failed for some time ago,
     * or did not start at all.
     */
    void removeStaleProgresses();
}
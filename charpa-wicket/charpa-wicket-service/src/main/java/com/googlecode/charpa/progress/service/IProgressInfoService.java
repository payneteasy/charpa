package com.googlecode.charpa.progress.service;

import com.googlecode.charpa.progress.service.impl.ProgressInfo;

import java.util.List;
import java.util.Map;

/**
 * Progress service
 */
public interface IProgressInfoService {

    ProgressId createProgressId(String aName);

    ProgressId createProgressId(String aName, Map<String, String> aPageParameters);

    void invoke(ProgressId aProgressId, Runnable aRunnable);

    IProgressInfo getProgressInfo(ProgressId aProgressId);

    List<IProgressInfo> listProgresses();

    /**
     * Cancels progress
     * @param aProgressId progress id
     */
    void cancelProgress(ProgressId aProgressId);

//    /**
//     * Get last messages
//     * @param aCount count messages
//     * @return messages
//     */
//    List<LogMessage> getLastLogMessages(ProgressId aId, int aCount) ;
//
}

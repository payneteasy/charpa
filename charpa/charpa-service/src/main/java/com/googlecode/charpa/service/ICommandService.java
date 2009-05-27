package com.googlecode.charpa.service;

import com.googlecode.charpa.progress.service.ProgressId;

/**
 * Executes command
 */
public interface ICommandService {

    /**
     * Executes command for application server instance
     * @param aProgressId progress id
     * @param aHostId     host
     * @param aAppId      application
     * @param aCommand    command
     */
    void executeCommand(ProgressId aProgressId, String aHostId, String aAppId, String aCommand);
}

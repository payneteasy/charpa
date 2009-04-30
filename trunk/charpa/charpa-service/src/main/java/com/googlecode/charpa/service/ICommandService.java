package com.googlecode.charpa.service;

/**
 * Executes command
 */
public interface ICommandService {

    /**
     * Executes command for application server instance
     * @param aInstanceId instance id
     * @param aCommand    command
     */
    void executeCommand(String aInstanceId, String aCommand);
}

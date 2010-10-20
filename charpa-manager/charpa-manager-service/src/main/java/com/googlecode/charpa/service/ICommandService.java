package com.googlecode.charpa.service;

import com.googlecode.charpa.progress.service.ProgressId;

import java.io.IOException;
import java.util.Map;

/**
 * Executes command
 */
public interface ICommandService {

    /**
     * Executes command for application server instance
     * @param aProgressId    progress id
     * @param aApplicationId application id
     * @param aCommand       command
     */
    void executeCommand(ProgressId aProgressId, long aApplicationId, String aCommand, Map<String, String> aEnv) ;
}

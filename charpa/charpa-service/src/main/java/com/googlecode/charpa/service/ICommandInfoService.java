package com.googlecode.charpa.service;

import com.googlecode.charpa.service.domain.CommandInfo;

/**
 * Gets information about command
 */
public interface ICommandInfoService {

    /**
     * Gets info about command
     *
     * @param aApplicationId app id
     * @param aCommandName   command name
     * @return command info
     */
    CommandInfo getCommandInfo(long aApplicationId, String aCommandName);
}

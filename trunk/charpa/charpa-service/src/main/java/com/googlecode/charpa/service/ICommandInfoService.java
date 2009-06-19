package com.googlecode.charpa.service;

import com.googlecode.charpa.service.domain.CommandInfo;
import com.googlecode.charpa.service.model.CommandForList;

import java.util.List;

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

    /**
     * Gets all commands
     *
     * @return commands list
     */
    List<CommandForList> getAllCommands();
}
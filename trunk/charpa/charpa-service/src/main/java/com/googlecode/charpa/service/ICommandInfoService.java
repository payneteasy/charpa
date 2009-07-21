package com.googlecode.charpa.service;

import com.googlecode.charpa.service.domain.CommandInfo;
import com.googlecode.charpa.service.model.CommandForList;

import java.util.List;
import java.io.IOException;

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
     * @throws java.io.IOException on io error
     */
    CommandInfo getCommandInfo(long aApplicationId, String aCommandName) throws IOException;

    /**
     * Gets all commands
     *
     * @return commands list
     */
    List<CommandForList> getAllCommands();
}

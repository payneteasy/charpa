package com.googlecode.charpa.service.impl;

import com.googlecode.charpa.service.ICommandInfoService;
import com.googlecode.charpa.service.domain.CommandInfo;

import java.io.File;

/**
 * Implementation of ICommandInfoService
 */
public class CommandInfoServiceImpl implements ICommandInfoService {
    /**
     * {@inheritDoc}
     */
    public CommandInfo getCommandInfo(long aApplicationId, String aCommandName) {
        CommandInfo info = new CommandInfo();
        File appDir = new File(theCommandsDir, String.valueOf(aApplicationId));
        if(!appDir.exists()) throw new IllegalStateException("Directory "+appDir.getAbsolutePath()+" is not exists");
        File commandFile = new File(appDir, aCommandName);
        if(!commandFile.exists()) throw new IllegalStateException("Command "+commandFile.getAbsolutePath()+" is not exists");
        info.setLocalFile(commandFile);
        return info;
    }

    /**
     * Command dir name
     * @param aCommandsDirName commands dir name
     */
    public void setCommandsDirName(String aCommandsDirName) {

        theCommandsDir = new File(aCommandsDirName);
    }

    /**
     * Command dir name
     */
    private File theCommandsDir;
}

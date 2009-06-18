package com.googlecode.charpa.service.impl;

import com.googlecode.charpa.service.ICommandInfoService;
import com.googlecode.charpa.service.dao.IApplicationDao;
import com.googlecode.charpa.service.dao.IHostDao;
import com.googlecode.charpa.service.model.CommandForList;
import com.googlecode.charpa.service.domain.CommandInfo;
import com.googlecode.charpa.service.domain.Application;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

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
     * {@inheritDoc}
     */
    public List<CommandForList> getAllCommands() {
        List<CommandForList> list = new LinkedList<CommandForList>();
        List<Application> applications = theApplicationDao.getAllApplications();
        for (Application application : applications) {
            File appDir = new File(theCommandsDir, String.valueOf(application.getId()));
            if(appDir.exists() && appDir.isDirectory()) {
                for (File file : appDir.listFiles()) {
                    if(file.isFile() && file.exists()) {
                        CommandForList cmd = new CommandForList();
                        cmd.setCommandName(file.getName());
                        cmd.setApplicationId(application.getId());
                        cmd.setApplicationName(application.getApplicationName());
                        cmd.setHostname(theHostDao.getHostById(application.getId()).getName());
                        list.add(cmd);
                    }
                }
            }
        }
        return list;
    }

    /**
     * Command dir name
     * @param aCommandsDirName commands dir name
     */
    public void setCommandsDirName(String aCommandsDirName) {

        theCommandsDir = new File(aCommandsDirName);
    }

    /** IApplicationDao */
    public void setApplicationDao(IApplicationDao aApplicationDao) { theApplicationDao = aApplicationDao ; }

    /** IHostDao */
    public void setHostDao(IHostDao aHostDao) { theHostDao = aHostDao ; }

    /** IHostDao */
    private IHostDao theHostDao ;
    /** IApplicationDao */
    private IApplicationDao theApplicationDao ;
    /**
     * Command dir name
     */
    private File theCommandsDir;
}

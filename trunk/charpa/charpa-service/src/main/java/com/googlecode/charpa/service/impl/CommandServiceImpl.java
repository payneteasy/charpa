package com.googlecode.charpa.service.impl;

import com.googlecode.charpa.progress.service.IProgressManagerService;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.service.ICommandInfoService;
import com.googlecode.charpa.service.ICommandService;
import com.googlecode.charpa.service.IHostService;
import com.googlecode.charpa.service.ISecurityShellService;
import com.googlecode.charpa.service.dao.IApplicationDao;
import com.googlecode.charpa.service.dao.IUserDao;
import com.googlecode.charpa.service.domain.Application;
import com.googlecode.charpa.service.domain.CommandInfo;
import com.googlecode.charpa.service.domain.Host;
import com.googlecode.charpa.service.domain.User;

/**
 * Implementation of ICommandService
 */
public class CommandServiceImpl implements ICommandService {

    /**
     * {@inheritDoc}
     */
    public void executeCommand(ProgressId aProgressId, long aApplicationId, String aCommand) {

        Application application = theApplicationDao.getApplicationById(aApplicationId);
        Host host = theHostService.getHostById(application.getHostId());

        theProgressManagerService.startProgress(aProgressId
                , String.format("Executing $s/$s: $s ..."
                , host.getHostname(), application.getApplicationName(), aCommand)
                , 1
        );

        User user = theUserDao.getUserById(application.getUserId());
        CommandInfo command = theCommandInfoService.getCommandInfo(aApplicationId, aCommand);

        try {
            // copy command to remote host
            theSecurityShellService.copyFileToRemoteHost(host.getHostname()
                    , host.getSshPort()
                    , user.getUsername()
                    , user.getPassword()
                    , command.getLocalFile()
                    , "."
            );

            // executes command
            theSecurityShellService.executeCommand(host.getHostname()
                    , host.getSshPort()
                    , user.getUsername()
                    , user.getPassword()
                    , null
                    , aCommand
                    , null
                    , null
            );

            theProgressManagerService.finishProgress(aProgressId);
        } catch (Exception e) {
            theProgressManagerService.progressFailed(aProgressId, e);
        }

    }

    /**
     * ISecurityShellService
     *
     * @param aSecurityShellService ssh service
     */
    public void setSecurityShellService(ISecurityShellService aSecurityShellService) {
        theSecurityShellService = aSecurityShellService;
    }

    /**
     * IProgressManagerService
     *
     * @param aProgressManagerService progress service
     */
    public void setProgressManagerService(IProgressManagerService aProgressManagerService) {
        theProgressManagerService = aProgressManagerService;
    }

    /**
     * IHostService
     *
     * @param aHostService host service
     */
    public void setHostService(IHostService aHostService) {
        theHostService = aHostService;
    }

    /**
     * Application dao
     *
     * @param aApplicationDao app dao
     */
    public void setApplicationDao(IApplicationDao aApplicationDao) {
        theApplicationDao = aApplicationDao;
    }

    /**
     * User dao
     *
     * @param aUserDao user dao
     */
    public void setUserDao(IUserDao aUserDao) {
        theUserDao = aUserDao;
    }

    /**
     * ICommandInfoService
     *
     * @param aCommandInfoService command info service
     */
    public void setCommandInfoService(ICommandInfoService aCommandInfoService) {
        theCommandInfoService = aCommandInfoService;
    }

    /**
     * ICommandInfoService
     */
    private ICommandInfoService theCommandInfoService;
    /**
     * User dao
     */
    private IUserDao theUserDao;
    /**
     * Application dao
     */
    private IApplicationDao theApplicationDao;
    /**
     * IHostService
     */
    private IHostService theHostService;
    /**
     * IProgressManagerService
     */
    private IProgressManagerService theProgressManagerService;
    /**
     * ISecurityShellService
     */
    private ISecurityShellService theSecurityShellService;
}

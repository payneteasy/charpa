package com.googlecode.charpa.service.impl;

import com.googlecode.charpa.progress.service.IProgressManagerService;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.service.*;
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
    public void executeCommand(final ProgressId aProgressId, long aApplicationId, String aCommand) {

        Application application = theApplicationDao.getApplicationById(aApplicationId);
        Host host = theHostService.getHostById(application.getHostId());

        theProgressManagerService.startProgress(aProgressId
                , String.format("Run %s / %s / %s ...", host.getHostname(), application.getApplicationName(), aCommand)
                , 2
        );

        User user = theUserDao.getUserById(application.getUserId());
        CommandInfo command = theCommandInfoService.getCommandInfo(aApplicationId, aCommand);

        try {
            // copy command to remote host
            theProgressManagerService.setProgressText(aProgressId, String.format("Copy file %s to %s...", aCommand, host.getHostname()));
            theSecurityShellService.copyFileToRemoteHost(host.getHostname()
                    , host.getSshPort()
                    , user.getUsername()
                    , user.getPassword()
                    , command.getLocalFile()
                    , "."
            );
            theProgressManagerService.incrementProgressValue(aProgressId);

            // executes command
            theProgressManagerService.setProgressText(aProgressId, String.format("Executing command %s ...", aCommand));
            theSecurityShellService.executeCommand(host.getHostname()
                    , host.getSshPort()
                    , user.getUsername()
                    , user.getPassword()
                    , null
                    , String.format("chmod +x ./%s && ./%s && rm ./%s", aCommand, aCommand, aCommand)
                    , null
                    , new ICommandOutputListener() {
                public void onOutputLine(Level aLevel, String aLine) {
                    if(aLevel == Level.ERROR) {
                        theProgressManagerService.setProgressText(aProgressId, "ERROR: "+aLine);
                    } else {
                        theProgressManagerService.setProgressText(aProgressId, aLine);
                    }
                }
            }
            );
            theProgressManagerService.incrementProgressValue(aProgressId);

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

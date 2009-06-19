package com.googlecode.charpa.service.impl;

import com.googlecode.charpa.progress.service.IProgressManagerService;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.service.*;
import com.googlecode.charpa.service.model.HttpProxyInfo;
import com.googlecode.charpa.service.dao.IApplicationDao;
import com.googlecode.charpa.service.dao.IUserDao;
import com.googlecode.charpa.service.domain.*;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

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

        String key = aApplicationId + " - " + aCommand;
        Integer previousCount = thePreviosCommandLinesCount.get(key);
        final int max = 2 + (previousCount!=null ? previousCount : 0) ;

        theProgressManagerService.startProgress(aProgressId
                , String.format("Run %s / %s / %s ...", host.getHostname(), application.getApplicationName(), aCommand)
                , max
        );

        User user = theUserDao.getUserById(application.getUserId());
        CommandInfo command = theCommandInfoService.getCommandInfo(aApplicationId, aCommand);

        try {
            HttpProxyInfo httpProxyInfo = createProxyInfo(host.getHttpProxy());
            // copy command to remote host
            theProgressManagerService.setProgressText(aProgressId, String.format("Copy file %s to %s...", aCommand, host.getHostname()));
            theSecurityShellService.copyFileToRemoteHost(host.getHostname()
                    , host.getSshPort()
                    , user.getUsername()
                    , user.getPassword()
                    , command.getLocalFile()
                    , "."
                    , httpProxyInfo
            );
            theProgressManagerService.incrementProgressValue(aProgressId);

            // executes command
            theProgressManagerService.setProgressText(aProgressId, String.format("Executing command %s ...", aCommand));

            final AtomicInteger count = new AtomicInteger(0);
            theSecurityShellService.executeCommand(host.getHostname()
                    , host.getSshPort()
                    , user.getUsername()
                    , user.getPassword()
                    , null
                    , String.format("chmod +x ./%s && ./%s && rm ./%s", aCommand, aCommand, aCommand)
                    , null
                    , new ICommandOutputListener() {
                public void onOutputLine(Level aLevel, String aLine) {
                    // increment lines count
                    int currentCount = count.incrementAndGet();
                    if(currentCount + 2 <= max) {
                        theProgressManagerService.incrementProgressValue(aProgressId);
                    }
                    
                    if(aLevel == Level.ERROR) {
                        theProgressManagerService.error(aProgressId, aLine);
                    } else {
                        theProgressManagerService.info(aProgressId, aLine);
                    }
                }
            }
                    , httpProxyInfo
            );
            theProgressManagerService.incrementProgressValue(aProgressId);

            theProgressManagerService.finishProgress(aProgressId);
            thePreviosCommandLinesCount.put(key, count.get());
        } catch (Exception e) {
            theProgressManagerService.progressFailed(aProgressId, e);
        }

    }

    /**
     * Creates proxy info if proxy is setted to host
     * @param aProxy proxy
     * @return proxy info
     */
    private HttpProxyInfo createProxyInfo(HttpProxy aProxy) {
        return aProxy!=null ? new HttpProxyInfo(aProxy.getHostname(), aProxy.getPort()) : null;
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

    private Map<String, Integer> thePreviosCommandLinesCount = Collections.synchronizedMap(new HashMap<String, Integer>());
}
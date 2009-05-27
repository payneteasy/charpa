package com.googlecode.charpa.service.impl;

import com.googlecode.charpa.service.ICommandService;
import com.googlecode.charpa.service.ISecurityShellService;
import com.googlecode.charpa.service.IHostService;
import com.googlecode.charpa.service.domain.Host;
import com.googlecode.charpa.service.domain.User;
import com.googlecode.charpa.service.domain.Application;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.progress.service.IProgressManagerService;

/**
 * Implementation of ICommandService
 */
public class CommandServiceImpl implements ICommandService {

    /**
     * {@inheritDoc}
     */
    public void executeCommand(ProgressId aProgressId, String aHostId, String aAppId, String aCommand) {

        theProgressManagerService.startProgress(aProgressId
                , String.format("Executing $s/$s: $s ...", aHostId, aAppId, aCommand)
                , 1
        );

//        Host host = theHostService.getHostById(aHostId);
//        User user = theUsersService.getUser(aHostId, aApplicationId);
//        Application application = theApplicationService.getApplication(aHostId, aAppId);
//
//        // copy command to remote host
//        theSecurityShellService.executeCommand(host
//                , host.getSecurityShellPort()
//                , user.getUsername()
//                , user.getPassword()
//                , application.getEnvironmentVariables()
//                , null
//                , commandOutputListener
//        );


        // executes command

    }

    /** ISecurityShellService */
    public void setSecurityShellService(ISecurityShellService aSecurityShellService) { theSecurityShellService = aSecurityShellService ; }

    /** IProgressManagerService */
    public void setProgressManagerService(IProgressManagerService aProgressManagerService) { theProgressManagerService = aProgressManagerService ; }

    /** IHostService */
    public void setHostService(IHostService aHostService) { theHostService = aHostService ; }

    /** IHostService */
    private IHostService theHostService ;
    /** IProgressManagerService */
    private IProgressManagerService theProgressManagerService ;
    /** ISecurityShellService */
    private ISecurityShellService theSecurityShellService ;
}

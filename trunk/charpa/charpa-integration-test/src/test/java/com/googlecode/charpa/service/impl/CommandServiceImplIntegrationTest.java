package com.googlecode.charpa.service.impl;

import junit.framework.TestCase;
import com.googlecode.charpa.service.dao.impl.simple.ApplicationSimpleDao;
import com.googlecode.charpa.service.dao.impl.simple.SimplePersister;
import com.googlecode.charpa.service.dao.impl.simple.HostSimpleDao;
import com.googlecode.charpa.service.dao.impl.simple.UserSimpleDao;
import com.googlecode.charpa.progress.service.impl.ProgressServiceImpl;
import com.googlecode.charpa.progress.service.ProgressId;

/**
 * 
 */
public class CommandServiceImplIntegrationTest extends TestCase {

    public static final String CONFIG_XML = "src/test/resources/CommandServiceImplIntegrationTest/config.xml";
    
    public void test() throws Exception {
        SimplePersister persister = new SimplePersister(CONFIG_XML);

        CommandServiceImpl commandService = new CommandServiceImpl();

        // application dao
        ApplicationSimpleDao applicationDao = new ApplicationSimpleDao();
        applicationDao.setPersister(persister);
        commandService.setApplicationDao(applicationDao);

        // host
        HostSimpleDao hostDao = new HostSimpleDao();
        hostDao.setPersister(persister);
        HostServiceImpl hostService = new HostServiceImpl();
        hostService.setHostDao(hostDao);
        commandService.setHostService(hostService);

        // user
        UserSimpleDao userDao = new UserSimpleDao();
        userDao.setPersister(persister);
        commandService.setUserDao(userDao);

        // command info
        CommandInfoServiceImpl commandInfoService = new CommandInfoServiceImpl();
        commandInfoService.setCommandsDirName("src/test/resources/CommandServiceImplIntegrationTest");
        commandService.setCommandInfoService(commandInfoService);

        // progress
        ProgressServiceImpl progressService = new ProgressServiceImpl();
        commandService.setProgressManagerService(progressService);

        // ssh service
        SecurityShellServiceImpl sshService = new SecurityShellServiceImpl();
        commandService.setSecurityShellService(sshService);

        ProgressId id = progressService.createProgressId("Executes test command");
        commandService.executeCommand(id, 1L, "test.sh", null);

    }
}

package com.googlecode.charpa.service.impl;

import junit.framework.TestCase;
import com.googlecode.charpa.service.dao.impl.simple.ApplicationSimpleDao;
import com.googlecode.charpa.service.dao.impl.simple.SimplePersister;
import com.googlecode.charpa.service.dao.impl.simple.HostSimpleDao;
import com.googlecode.charpa.service.model.CommandForList;
import com.googlecode.charpa.service.model.VariableInfo;
import com.googlecode.charpa.service.domain.CommandInfo;

import java.util.List;

/**
 * Test for CommandInfoServiceImpl
 */
public class CommandInfoServiceImplTest extends TestCase {

    public static final String COMMANDS_DIR = "src/test/resources/testbase";
    public static final String CONFIG_XML   = "src/test/resources/testbase/config.xml";

    public void testGetAllCommands() throws Exception {

        ApplicationSimpleDao applicationDao = new ApplicationSimpleDao();
        SimplePersister persister = new SimplePersister(CONFIG_XML);
        applicationDao.setPersister(persister);

        HostSimpleDao hostDao = new HostSimpleDao();
        hostDao.setPersister(persister);

        CommandInfoServiceImpl commandInfoService = new CommandInfoServiceImpl();
        commandInfoService.setCommandsDirName(COMMANDS_DIR);
        commandInfoService.setApplicationDao(applicationDao);
        commandInfoService.setHostDao(hostDao);

        List<CommandForList> list = commandInfoService.getAllCommands();
        assertNotNull(list);
        assertEquals(1, list.size());
        CommandForList cmd = list.get(0);
        assertEquals("test.sh", cmd.getCommandName());
        assertEquals("test-application", cmd.getApplicationName());
        assertEquals(1, cmd.getApplicationId());
        assertEquals("testhost", cmd.getHostname());

        CommandInfo commandInfo = commandInfoService.getCommandInfo(cmd.getApplicationId(), "test.sh");
        assertNotNull(commandInfo);
        assertEquals(1, commandInfo.getVariables().size());
        VariableInfo var = commandInfo.getVariables().get(0);
        assertEquals("ENV_SOURCE_DIR", var.getName());
        assertEquals("~/svn/project-source", var.getDefaultValue());
        assertEquals("source dir", var.getComment());
    }
}

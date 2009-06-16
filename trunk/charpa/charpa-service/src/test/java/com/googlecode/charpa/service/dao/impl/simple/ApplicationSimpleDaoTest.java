package com.googlecode.charpa.service.dao.impl.simple;

import junit.framework.TestCase;
import com.googlecode.charpa.service.domain.Host;
import com.googlecode.charpa.service.domain.Application;

import java.io.File;

/**
 * Tests for HostSimpleDao
 */
public class ApplicationSimpleDaoTest extends TestCase {

    private static final String CONFIG_XML = "target/applications.xml";

    public void test() throws Exception {

        File file = new File(CONFIG_XML);
        file.delete();

        ApplicationSimpleDao dao = new ApplicationSimpleDao();
        SimplePersister persister = new SimplePersister(CONFIG_XML);
        dao.setPersister(persister);

        Application application = new Application();
        application.setApplicationName("test-application");
        application.setHostId(1L);
        application.setUserId(1L);

        dao.createApplication(application);

        Application loaded = dao.getApplicationById(application.getId());

        assertEquals(application.getId(), loaded.getId());
        assertEquals(application.getApplicationName(), loaded.getApplicationName());
        assertEquals(application.getUserId(), loaded.getUserId());
        assertEquals(application.getHostId(), loaded.getHostId());
    }
}
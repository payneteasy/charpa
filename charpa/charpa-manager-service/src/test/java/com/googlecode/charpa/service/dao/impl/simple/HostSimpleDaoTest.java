package com.googlecode.charpa.service.dao.impl.simple;

import junit.framework.TestCase;
import com.googlecode.charpa.service.domain.Host;

import java.io.File;

/**
 * Tests for HostSimpleDao
 */
public class HostSimpleDaoTest extends TestCase {

    private static final String CONFIG_XML = "target/hosts.xml";

    public void test() throws Exception {

        File file = new File(CONFIG_XML);
        file.delete();

        HostSimpleDao dao = new HostSimpleDao();
        SimplePersister persister = new SimplePersister(CONFIG_XML);
        dao.setPersister(persister);

        Host host = new Host();
        host.setHostname("localhost");
        host.setName("test-host");
        host.setSshPort(22);

        dao.addHost(host);

        Host loadedHost = dao.getHostById(host.getId());
        assertEquals(host.getId(), loadedHost.getId());
        assertEquals(host.getName(), loadedHost.getName());
        assertEquals(host.getHostname(), loadedHost.getHostname());
        assertEquals(host.getSshPort(), loadedHost.getSshPort());
    }
}

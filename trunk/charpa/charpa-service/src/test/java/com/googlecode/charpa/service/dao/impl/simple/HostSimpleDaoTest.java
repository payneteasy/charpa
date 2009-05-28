package com.googlecode.charpa.service.dao.impl.simple;

import junit.framework.TestCase;
import com.googlecode.charpa.service.domain.Host;

import java.io.File;

/**
 *
 */
public class HostSimpleDaoTest extends TestCase {

    public void test() throws Exception {

        File file = new File("target/hosts.xml");
        file.delete();

        HostSimpleDao dao = new HostSimpleDao();
        SimplePersister persister = new SimplePersister("target/hosts.xml");
        dao.setPersister(persister);

        Host host = new Host();
        host.setHostname("host1");
        host.setIpAddress("localhost");

        dao.addHost(host);

        
    }
}

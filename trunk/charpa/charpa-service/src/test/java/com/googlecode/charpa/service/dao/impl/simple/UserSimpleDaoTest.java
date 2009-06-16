package com.googlecode.charpa.service.dao.impl.simple;

import junit.framework.TestCase;
import com.googlecode.charpa.service.domain.Host;
import com.googlecode.charpa.service.domain.User;

import java.io.File;

/**
 * Tests for HostSimpleDao
 */
public class UserSimpleDaoTest extends TestCase {

    private static final String CONFIG_XML = "target/users.xml";

    public void test() throws Exception {

        File file = new File(CONFIG_XML);
        file.delete();

        UserSimpleDao dao = new UserSimpleDao();
        SimplePersister persister = new SimplePersister(CONFIG_XML);
        dao.setPersister(persister);

        User user = new User();
        user.setUsername("test-user");
        user.setPassword("password");
        user.setComment("comment");

        dao.createUser(user);

        User loaded = dao.getUserById(user.getId());

        assertEquals(user.getId(), loaded.getId());
        assertEquals(user.getUsername(), loaded.getUsername());
        assertEquals(user.getPassword(), loaded.getPassword());
        assertEquals(user.getComment(), loaded.getComment());
    }
}
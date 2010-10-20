package com.googlecode.charpa.service.impl;

import junit.framework.TestCase;

import java.util.UUID;
import java.util.Map;
import java.util.Collections;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Command service test
 */
public class CommandServiceImplTest extends TestCase {

    public void testArchive() throws IOException, InterruptedException {
        CommandServiceImpl service = new CommandServiceImpl();
        service.setArchivesDirectory("target/archives");

        String uniqueId  = "test-"+System.currentTimeMillis();
        File commandFile = new File("src/test/resources/testbase/1/test.sh");
        Map<String, String> env = Collections.singletonMap("TEST_VAR", "test-value");

        File archiveFile = service.createTarGzArchive(uniqueId, commandFile, env );
        assertNotNull(archiveFile);
    }
}

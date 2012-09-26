package com.googlecode.charpa.progress.service.impl;

import com.googlecode.charpa.progress.service.ProgressId;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author rpuch
 */
public class ProgressServiceImplTest {
    @Test
    public void test() {
        ProgressServiceImpl service = new ProgressServiceImpl();
        ProgressId id = service.createProgressId("id");
        service.startProgress(id, "name", 100);
        for (int i = 1; i <= 100; i++) {
            service.error(id, String.valueOf(i));
        }

        Assert.assertEquals("51", service.getLastLogMessages(id, 100).iterator().next().getMessage());
        Assert.assertEquals("100", service.getLastLogMessages(id, 1).iterator().next().getMessage());
    }
}

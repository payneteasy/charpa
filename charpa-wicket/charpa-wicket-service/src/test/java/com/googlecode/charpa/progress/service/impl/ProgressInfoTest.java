package com.googlecode.charpa.progress.service.impl;

import com.googlecode.charpa.progress.service.ProgressId;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * @author rpuch
 */
public class ProgressInfoTest {
    @Test
    public void test() {
        ProgressInfo info = new ProgressInfo(new ProgressId("id"), "name", "text", "security", Collections.<String, String>emptyMap());
        for (int i = 1; i <= 100; i++) {
            info.error(String.valueOf(i));
        }

        Assert.assertEquals(50, info.getLogMessages().size());
        Assert.assertEquals("51", info.getLogMessages().get(0).getMessage());
        Assert.assertEquals("100", info.getLogMessages().get(49).getMessage());
    }
}

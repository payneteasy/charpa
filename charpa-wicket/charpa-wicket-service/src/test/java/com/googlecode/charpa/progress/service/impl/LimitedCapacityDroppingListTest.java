package com.googlecode.charpa.progress.service.impl;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author rpuch
 */
public class LimitedCapacityDroppingListTest {
    @Test
    public void testZeroCapacity() {
        try {
            new LimitedCapacityDroppingList<String>(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void test() {
        List<String> list = new LimitedCapacityDroppingList<String>(2);
        list.add("a");
        Assert.assertEquals(Arrays.asList("a"), list);
        Assert.assertEquals(1, list.size());

        list.add("b");
        Assert.assertEquals(Arrays.asList("a", "b"), list);
        Assert.assertEquals(2, list.size());

        list.add("c");
        Assert.assertEquals(Arrays.asList("b", "c"), list);
        Assert.assertEquals(2, list.size());

        Assert.assertEquals(Arrays.asList("b", "c"), list.subList(0, 2));

        list.remove(0);
        Assert.assertEquals(Arrays.asList("c"), list);
        Assert.assertEquals(1, list.size());

        list.remove("c");
        Assert.assertEquals(Collections.emptyList(), list);
        Assert.assertEquals(0, list.size());
    }
}

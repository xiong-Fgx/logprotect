package com.xiong.merkle.merkletree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KeyIndexTest {

    @Test
    public void testAll() {
        KeyIndex<String> k = new KeyIndex<String>("test", true, 1);

        assertEquals("test", k.getEntry());
        assertEquals(true, k.isExist());
        assertEquals(1, k.getIndex());

    }

}

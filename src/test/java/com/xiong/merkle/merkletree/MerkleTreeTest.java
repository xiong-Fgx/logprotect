/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 - 2018 
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xiong.merkle.merkletree;

import com.xiong.merkle.crypto.ByteUtil;
import com.xiong.merkle.utils.ByteableLong;
import com.xiong.merkle.utils.ByteableString;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class MerkleTreeTest {

    private IMerkleTree<ByteableString> stringTree;
    private IMerkleTree<ByteableLong> longTree;

    @Before
    public void setup() {
        stringTree = new MerkleTree<>();
        longTree = new MerkleTree<>();
    }

    @Test
    public void removeAll() {
        /*
        * 先向树中添加几个元素，保证整个tree不为空，然后调用removeAll再看是否为空，以验证是否将所有的节点全部remove
        *
        * */
        stringTree.add(new ByteableString("String1"));
        stringTree.add(new ByteableString("String2"));
        stringTree.add(new ByteableString("String3"));
        stringTree.add(new ByteableString("QAQ"));

        assertThat(stringTree.getRootHash(), is(notNullValue()));

        stringTree.removeAll();

        assertThat(stringTree.getRootHash(), is(nullValue()));
        assertThat(stringTree.getHeight(), is(0));


    }

    @Test
    public void testTreeUpdate() {
        /*
        * 测试是否进行了update，如果是向树中添加了原来没有的元素，则不会update，如果是加入了原来已经有的元素，则update=true
        * */
        boolean update;

        update = longTree.add(new ByteableLong(1));
        assertThat("Did not expect an update (should have been create)", update, is(false));

        update = longTree.add(new ByteableLong(2));
        assertThat("Did not expect an update (should have been create)", update, is(false));

        update = longTree.add(new ByteableLong(2));
        assertThat("Expected an update", update, is(true));

        update = longTree.add(new ByteableLong(5));
        assertThat("Did not expect an update (should have been create)", update, is(false));

        assertThat(longTree.toPrettyString(), is("(1 (2 5))"));
    }

    @Test
    public void testIteration() {
        // instantly returns false, because no root element
        assertThat(stringTree.iterateNodes(node -> true), is(false));

        HashWithCount hwc = stringTree.getHashWithCount();
        System.out.println(ByteUtil.toString00(hwc.hash));
        System.out.println(hwc.count);

        stringTree.add(new ByteableString("String1"));
        hwc = stringTree.getHashWithCount();
        System.out.println(ByteUtil.toString00(hwc.hash));
        System.out.println(hwc.count);
        stringTree.add(new ByteableString("String2"));
        hwc = stringTree.getHashWithCount();
        System.out.println(ByteUtil.toString00(hwc.hash));
        System.out.println(hwc.count);
        stringTree.add(new ByteableString("String3"));
        hwc = stringTree.getHashWithCount();
        System.out.println(ByteUtil.toString00(hwc.hash));
        System.out.println(hwc.count);
        stringTree.add(new ByteableString("String4"));
        hwc = stringTree.getHashWithCount();
        System.out.println(ByteUtil.toString00(hwc.hash));
        System.out.println(hwc.count);
        stringTree.add(new ByteableString("String5"));
        hwc = stringTree.getHashWithCount();
        System.out.println(ByteUtil.toString00(hwc.hash));
        System.out.println(hwc.count);
        stringTree.add(new ByteableString("String6"));
        hwc = stringTree.getHashWithCount();
        System.out.println(ByteUtil.toString00(hwc.hash));
        System.out.println(hwc.count);
        stringTree.add(new ByteableString("String7"));
        hwc = stringTree.getHashWithCount();
        System.out.println(ByteUtil.toString00(hwc.hash));
        System.out.println(hwc.count);
        stringTree.add(new ByteableString("String8"));
        hwc = stringTree.getHashWithCount();
        System.out.println(ByteUtil.toString00(hwc.hash));
        System.out.println(hwc.count);
        stringTree.add(new ByteableString("String9"));
        hwc = stringTree.getHashWithCount();
        System.out.println(ByteUtil.toString00(hwc.hash));
        System.out.println(hwc.count);

        ArrayList<String> hitItems = new ArrayList<>();

        stringTree.iterateNodes(node -> {
            if (node.isLeafNode()) {
                hitItems.add(node.getKey().string);
            }
            return false;
        });

        System.out.println(hitItems);
        System.out.println(stringTree.getHeight());
        System.out.println(ByteUtil.toString00(stringTree.getRootHash()));

//        assertThat(hitItems.size(), is(3));
//        assertThat(hitItems, hasItem("String1"));
//        assertThat(hitItems, hasItem("String2"));
//        assertThat(hitItems, hasItem("String3"));
    }

    @Test
    public void testSize() {
        assertThat(stringTree.size(), is(0));
        stringTree.add(new ByteableString("test"));
        assertThat(stringTree.size(), is(1));
    }

    @Test
    public void testHeight() {
        assertThat(stringTree.getHeight(), is(0));

        stringTree.add(new ByteableString("1"));
        assertThat(stringTree.getHeight(), is(0));

        stringTree.add(new ByteableString("2"));
        assertThat(stringTree.getHeight(), is(1));

        stringTree.add(new ByteableString("3"));
        stringTree.add(new ByteableString("4"));
        assertThat(stringTree.getHeight(), is(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAndRemove() {
        assertThat(stringTree.get(new ByteableString("test")), is(nullValue()));
        assertThat(stringTree.get(0), is(nullValue()));

        stringTree.add(new ByteableString("test"));
        assertThat(stringTree.get(new ByteableString("test")), is(notNullValue()));
        assertThat(stringTree.get(0), is(notNullValue()));

        stringTree.remove(new ByteableString("test")); // throws UnsupportedOperationException
        assertThat(stringTree.get(new ByteableString("test")), is(nullValue()));

    }

    @Test
    public void testGetRootHash() {
        assertThat(stringTree.contains(new ByteableString("test")), is(false));
        assertThat(stringTree.getRootHash(), is(nullValue()));

        stringTree.add(new ByteableString("test"));
        assertThat(ByteUtil.toString00(stringTree.getRootHash()), is("C9893DAFCECB4E9FF86BF16501397E4A2DC8B9E5"));

        assertThat(stringTree.getRoot().getKey(), is(new ByteableString("test")));

        assertThat(stringTree.contains(new ByteableString("test")), is(true));
        assertThat(stringTree.contains(new ByteableString("nottest")), is(false));
    }

    @Test
    public void testToPrettyString() {
        assertThat(stringTree.toPrettyString(), is("()"));
        stringTree.add(new ByteableString("1"));
        stringTree.add(new ByteableString("2"));
        stringTree.add(new ByteableString("3"));
        stringTree.add(new ByteableString("4"));
        stringTree.add(new ByteableString("5"));
        stringTree.add(new ByteableString("6"));
        stringTree.add(new ByteableString("7"));
        System.out.println(stringTree.toPrettyString());
//        assertThat(stringTree.toPrettyString(), is("(49 50)"));
    }

    @Test
    public void testHashWithCount() {

        HashWithCount hwc = longTree.getHashWithCount();
        assertThat(hwc.hash, is(nullValue()));
        assertThat(hwc.count, is(0));

        longTree.add(new ByteableLong(1L));

        hwc = longTree.getHashWithCount();
        assertThat(hwc.count, is(1));
        assertThat(ByteUtil.toString00(hwc.hash), is("8F7EA991CBC6B6FDCE27D02DC9A3297AF102F94B"));

        longTree.add(new ByteableLong(2L));

        hwc = longTree.getHashWithCount();
        assertThat(hwc.count, is(2));
        assertThat(ByteUtil.toString00(hwc.hash), is("E9CD625EBC02C8602B8E0DC8861B9E4B480757BA"));

    }

}

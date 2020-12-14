package com.xiong.merkle.merkletree;

import com.xiong.merkle.utils.ByteableFile;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MerkleTreeFileTest {

    private IMerkleTree<ByteableFile> fileTree;

    @Before
    public void setup() {
        fileTree = new MerkleTree<>();
    }

    @Test
    public void testFile() throws IOException {
        System.out.println(fileTree.size());
        fileTree.add(new ByteableFile("F:\\毕设\\testfile\\file1.txt"));
        System.out.println(fileTree.size());
    }
}

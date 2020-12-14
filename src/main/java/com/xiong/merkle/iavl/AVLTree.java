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
package com.xiong.merkle.iavl;

import com.xiong.merkle.iavl.IterateFunct.Loop;
import com.xiong.merkle.merkletree.HashWithCount;

import java.util.Arrays;

/**
 * Self balancing binary search tree
 */
public class AVLTree<K extends Comparable<K>> {

    private final Hashing<K> hashalgorithm;

    private Node<K> rootNode;

    public AVLTree(Hashing<K> hashalgorithm) {
        this.hashalgorithm = hashalgorithm;
    }

    /**
     * @return the tree size (elements in tree)
     */
    public int size() {
        return rootNode == null ? 0 : rootNode.getSize();
    }

    /**
     * @return the tree-height
     */
    public int getHeight() {
        return rootNode == null ? 0 : rootNode.getHeight();
    }

    /**
     * Check if an entry is already present
     * 
     * @param entry
     *            the non-null entry to lookup
     * @return true if this entry exists in the tree
     */
    public boolean contains(K entry) {
        return rootNode == null ? false : rootNode.contains(entry);
    }

    /**
     *
     * @param entry the non-null entry to get
     * @return an entry from the tree, uses {@link #equals(Object)} for equality-checks
     */
    public K get(K entry) {
        return rootNode == null ? null : rootNode.get(entry);
    }

    /**
     *
     * @param index the index to get
     * @return a KeyIndex representing the lookup result
     */
    public KeyIndex<K> get(int index) {
        return rootNode == null ? null : rootNode.get(index);
    }

    /**
     * Add a new entry
     * 
     * @param entry the non-null entry to add
     * @return <code>true</code> if tree was updated, <code>false</code> if simple add possible
     */
    public boolean add(K entry) {
        if (rootNode == null) {
            rootNode = new Node<K>().init(entry).setHashFunction(hashalgorithm);
            return false;
        } else {
            AddResult<K> result = rootNode.add(entry);
            rootNode = result.getNode();
            return result.wasUpdated();
        }
    }

    // /**
    // * Remove an entry
    // *
    // * @param entry
    // * @return
    // */
    // public RmResult<K> remove(K entry) {
    // if (rootNode == null) {
    // return null;
    // }
    // boolean result = rootNode.remove(entry);
    // RmResult<K> rs = new RmResult<K>(null, null, null, result);
    // return rs;
    // }

    /**
     * @return the Root-Hash and the amount of hashes
     */
    public HashWithCount getHashWithCount() {
        if (rootNode != null) {
            return rootNode.getHashWithCount();
        } else {
            return new HashWithCount(null, 0);
        }
    }

    /**
     * @return the root-hash. Maybe null
     */
    public byte[] getRootHash() {
        if (rootNode == null) {
            return null;
        } else {
            byte[] rootHash = rootNode.getHashWithCount().hash;
            return rootHash != null ? Arrays.copyOf(rootHash, rootHash.length) : null;
        }
    }

    /**
     * @return the Root-Node. Maybe null
     */
    public Node<K> getRoot() {
        return rootNode;
    }

    /**
     * @return a prettified representation of this tree for debugging: ((1 2) (3 4))
     */
    public String toPrettyString() {
        if (rootNode == null) {
            return "()";
        }
        return rootNode.toPrettyString();
    }

    /**
     * Iterate over every node. check Leafnodes with node.isLeafNode()
     *
     * @param function the function to apply to each node while iterating
     * @return <code>Run.STOP</code> when done
     */
    public Loop iterateNodes(IterateFunct<K> function) {
        if (rootNode != null) {
            return rootNode.iterateNodes(function);
        }
        return Loop.STOP;
    }

}

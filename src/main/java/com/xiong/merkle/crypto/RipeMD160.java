/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2017 - 2018
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
package com.xiong.merkle.crypto;

import com.xiong.merkle.crypto.ByteUtil.ByteFormat;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

/**
 * BouncyCastle RipeMD160
 *
 * @author wolfposd
 */
public class RipeMD160 implements HashFunction{

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Hashes a byte[] with RipeMD160
     * 
     * @param bytesToHash
     *            arbitrary byte array
     * @return byte[] containing hash
     */
    public static byte[] hash(byte[] bytesToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("RIPEMD160");
            return md.digest(bytesToHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Hashes a byte[] with RipeMD160
     * 
     * @param bytesToHash
     *            arbitrary byte array
     * @return Base64-encoded hash
     */
    public static String hashToBase64(byte[] bytesToHash) {
        byte[] hashed = hash(bytesToHash);
        return Base64.getEncoder().encodeToString(hashed);
    }

    /**
     * Hashes a byte[] with RipeMD160
     * 
     * @param bytesToHash
     *            arbitrary byte array
     * @return Hex-Encoded String hash
     */
    public static String hashToStringBytes(byte[] bytesToHash) {
        return ByteUtil.toString(hash(bytesToHash), ByteFormat.FORMAT_00);
    }

    /**
     * Hashes the byte[] of a TX, by appending TM's custom length+lengthOfLenght+byte[] format
     * 
     * @param tx
     *            Tendermint transaction byte[]
     * @return hash of tx
     */
    public static byte[] hashTx(byte[] tx) {
        int length = tx.length;

        BigInteger bigInt = BigInteger.valueOf(length);
        byte[] lengthlength = bigInt.toByteArray();

        byte b = Integer.valueOf(length).byteValue();

        ByteBuffer buff = ByteBuffer.allocate(1 + lengthlength.length + tx.length);

        buff.put(b);
        buff.put(lengthlength);
        buff.put(tx);

        return hash(buff.array());
    }

    /**
     * Hashes the byte[] of a TX, by appending TM's custom length+lengthOfLenght+byte[] format
     * 
     * @param tx
     *            Tendermint transaction byte[]
     * @return Hex-Encoded String hash
     */
    public static String hashTxToStringBytes(byte[] tx) {
        return ByteUtil.toString00(hashTx(tx));
    }

    @Override
    public byte[] hashBytes(byte[] byteArray) {
        return hash(byteArray);
    }

}

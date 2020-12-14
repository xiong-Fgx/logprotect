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
package com.xiong.merkle.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * A Byteable that holds a String
 *
 * @author wolfposd
 */
public class ByteableString implements IByteable<ByteableString> {

    public String string;

    public ByteableString(String s) {
        string = s;
    }

    ByteableString(byte[] bytes) {
        try {
            string = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 conversion failed.", e);
        }
    }

    @Override
    public byte[] toByteArray() {
        return string.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public int compareTo(final ByteableString other) {
        return string.compareTo(other.string);
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public int hashCode() {
        if (string == null) {
            return 0;
        } else {
            return string.hashCode();
        }
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof ByteableString) {
            ByteableString other = (ByteableString) arg0;
            return Objects.equals(this.string, other.string);
        }
        return false;
    }

}
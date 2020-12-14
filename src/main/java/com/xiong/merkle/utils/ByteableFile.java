package com.xiong.merkle.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ByteableFile implements IByteable<ByteableLong> {

    final String value;

    private final byte[] bytes;

    public ByteableFile(String filePath) throws IOException {
        this.value = filePath;
        this.bytes = calcByteArray(filePath);
    }

    private byte[] calcByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }


    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public int compareTo(ByteableLong other) {
        return 0;
    }
}

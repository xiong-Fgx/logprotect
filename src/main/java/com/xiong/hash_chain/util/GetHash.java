package com.xiong.hash_chain.util;

import java.security.MessageDigest;

/*
* 计算哈希值
* 函数：给定一个字符串，计算它的SHA-256哈希值
* */

public class GetHash {
    public static String getHashSHA256(String input){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for(int i = 0; i < hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

package com.xiong.hash_chain.util;

import java.security.Key;
import java.util.Base64;

/*获取key的字符串表示
* 输入key类型的数据，将其转换成String类型
* */

public class Key2String {
    public static String getStringFromKey(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}

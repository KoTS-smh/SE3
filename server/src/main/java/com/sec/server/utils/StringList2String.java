package com.sec.server.utils;

import java.util.List;

public class StringList2String {
    /**
     * 用于将字符串的List转换成字符串
     * @param join 分隔符号
     * @param stringList 字符串列表
     * @return
     */
    public static String join(String join, List<String> stringList) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0, len = stringList.size();i < len; ++i) {
            if(i == (len - 1))
                sb.append(stringList.get(i));
            else
                sb.append(stringList.get(i)).append(join);
        }

        return sb.toString();
    }
}

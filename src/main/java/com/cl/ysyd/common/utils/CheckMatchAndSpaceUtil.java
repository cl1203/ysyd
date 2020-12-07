package com.cl.ysyd.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMatchAndSpaceUtil {

    /**
     * 校验是否包含空格
      * @param str
     * @return
     */
    public static boolean checkBlankSpace(String str) {
        int i = str.indexOf(" ");
        return i == -1;
    }

    /**
     * @param regex
     * 正则表达式字符串
     * @param str
     * 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}

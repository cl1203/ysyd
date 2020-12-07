package com.cl.ysyd.common.utils;

import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 2018/6/8
 * Time: 13:09
 * To change this template use File | Settings | File Templates.
 * Description:
 */
public class MD5Util {
    private static MD5Util  md5Util  = null;

    private static final String MD5_SALT = "QWERTYUIOPASDFGHJKLZXCVBNM0123456789";

    public static synchronized MD5Util getInstance() {
        if (md5Util == null) {
            md5Util = new MD5Util();
        }
        return md5Util;
    }

    private MD5Util() {

    }

    public String encrypt(String dataStr) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte[] s = m.digest();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < s.length; i++) {
                result.append(Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6));
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * <pre>
     * md5(md5(str+salt))两次MD5,一次加盐
     * </pre>
     * @param dataStr
     * @return
     */
    public String encryptBySalt(String dataStr) {
        try {
            dataStr = dataStr + MD5_SALT;
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte[] s = m.digest();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < s.length; i++) {
                result.append(Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6));
            }
            String realResult = encrypt(result.toString()); // 第二次MD5
            return realResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

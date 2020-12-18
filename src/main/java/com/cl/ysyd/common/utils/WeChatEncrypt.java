package com.cl.ysyd.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 
/**
 * 微信公众号字符串加密工具类
 */
public class WeChatEncrypt
{
 
    public static final String MD5 = "MD5";
    public static final String SHA_1 = "SHA-1";
    public static final String SHA_256 = "SHA_256";
 
    /**
     * 使用指定的加密算法对字符串进行加密
     * 
     * @param srcStr
     *            要加密的字符串
     * @param encryptType
     *            MD5\SHA_1\SHA_256
     * @return 加密后的字符串
     */
    public static String encrypt(String srcStr, String encryptType)
    {
        String desStr = "";
        try
        {
            if ((encryptType == null) || ("".equals(encryptType)))
                encryptType = "MD5";
            MessageDigest md = MessageDigest.getInstance(encryptType);
            md.update(srcStr.getBytes());
            desStr = bytes2Hex(md.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            return srcStr;
        }
        return desStr;
    }
 
    /**
     * 将字节数组转换为16进制字符串
     * 
     * @param bts
     * @return
     */
    private static String bytes2Hex(byte[] bts)
    {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++)
        {
            tmp = Integer.toHexString(bts[i] & 0xFF);
            if (tmp.length() == 1)
            {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
 
    public static void main(String[] args)
    {
        String strSrc = "\u53EF\u4EE5\u52A0\u5BC6\u6C49\u5B57";
        System.out.println("Source String:" + strSrc);
        System.out.println("Encrypted String:");
        System.out.println("Use MD5:" + encrypt(strSrc, null));
        System.out.println("Use MD5:" + encrypt(strSrc, "MD5"));
        System.out.println("Use SHA:" + encrypt(strSrc, "SHA-1"));
        System.out.println("Use SHA-256:" + encrypt(strSrc, "SHA-256"));
    }
}


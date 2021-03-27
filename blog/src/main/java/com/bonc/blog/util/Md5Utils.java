package com.bonc.blog.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * MD5工具类
 *
 * @author 兰杰
 * @create 2019-09-23 15:08
 */
public class Md5Utils {

    private static final String SALT = "bonc";

    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    public static String md5(String str) {

        return new Md5Hash(str, SALT).toString();
    }

    public static void main(String[] args) {
        System.out.println(Md5Utils.md5("admin123"));
    }
}

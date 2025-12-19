package com.walkingpet.common.security.utils;

import cn.hutool.crypto.SecureUtil;

/**
 * 密码加密工具
 */
public class PasswordUtil {

    /**
     * 密码加密
     */
    public static String encrypt(String password) {
        return SecureUtil.md5(password);
    }

    /**
     * 密码验证
     */
    public static boolean verify(String rawPassword, String encodedPassword) {
        return encrypt(rawPassword).equals(encodedPassword);
    }
}

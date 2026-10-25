package com.xlqs.smallrag.utils;

import cn.dev33.satoken.secure.SaSecureUtil;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    /**
     * 生成 16 字节随机盐值。
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 计算密码摘要。
     */
    public static String encode(String rawPassword, String salt) {
        return SaSecureUtil.sha256(salt + rawPassword);
    }

    /**
     * 校验密码，使用恒定时间比较摘要。
     */
    public static boolean matches(
            String rawPassword,
            String salt,
            String encodedPassword) {

        if (rawPassword == null || salt == null || encodedPassword == null) {
            return false;
        }

        String calculated = encode(rawPassword, salt);

        return MessageDigest.isEqual(
                calculated.getBytes(java.nio.charset.StandardCharsets.US_ASCII),
                encodedPassword.getBytes(java.nio.charset.StandardCharsets.US_ASCII)
        );
    }
}
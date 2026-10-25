package com.xlqs.smallrag.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 20:08
 * @description ApiKeyCryptoService 类说明：TODO
 * @since 2026/10/25
 */
public class ApiKeyCryptoService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;

    private final SecretKeySpec secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    public ApiKeyCryptoService(String base64Key) {
        byte[] key = Base64.getDecoder().decode(base64Key);

        if (key.length != 32) {
            throw new IllegalArgumentException(
                    "MODEL_ENCRYPTION_KEY 必须解码为 32 字节");
        }

        this.secretKey = new SecretKeySpec(key, "AES");
    }

    public String encrypt(String plainText) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    secretKey,
                    new GCMParameterSpec(TAG_LENGTH, iv)
            );

            byte[] encrypted = cipher.doFinal(
                    plainText.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );

            // 格式：Base64(12字节IV + 密文 + GCM认证标签)
            byte[] result = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            throw new IllegalStateException("API Key 加密失败", e);
        }
    }

    public String decrypt(String cipherText) {
        try {
            byte[] data = Base64.getDecoder().decode(cipherText);

            if (data.length < IV_LENGTH + TAG_LENGTH / 8) {
                throw new IllegalArgumentException("密文格式不正确");
            }

            byte[] iv = java.util.Arrays.copyOfRange(data, 0, IV_LENGTH);
            byte[] encrypted = java.util.Arrays.copyOfRange(
                    data, IV_LENGTH, data.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey,
                    new GCMParameterSpec(TAG_LENGTH, iv)
            );

            byte[] plainText = cipher.doFinal(encrypted);
            return new String(
                    plainText,
                    java.nio.charset.StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "API Key 解密失败，请检查密文和加密密钥", e);
        }
    }
}

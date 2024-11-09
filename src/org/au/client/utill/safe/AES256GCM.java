package org.au.client.utill.safe;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AES256GCM {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128; // GCM 标签长度
    private static final int IV_LENGTH_BYTE = 12;  // GCM 非密初始化向量长度

    public static void main(String[] args) {
        try {
            // 生成密钥
            SecretKey secretKey = generateKey();

            // 要加密的数据
            String plaintext = "Hello, World!";
            byte[] plaintextBytes = plaintext.getBytes();

            // 加密
            byte[] ciphertext = encrypt(plaintextBytes, secretKey);
            System.out.println("加密后的数据: " + Base64.getEncoder().encodeToString(ciphertext));

            // 解密
            byte[] decryptedText = decrypt(ciphertext, secretKey);
            System.out.println("解密后的数据: " + new String(decryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(256); // AES-256
        return keyGen.generateKey();
    }

    public static byte[] encrypt(byte[] plaintext, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // 生成随机的 IV
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH_BYTE];
        secureRandom.nextBytes(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

        // 加密数据
        byte[] ciphertext = cipher.doFinal(plaintext);

        // 将 IV 和密文合并
        byte[] combined = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);

        return combined;
    }

    public static byte[] decrypt(byte[] combined, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // 分离 IV 和密文
        byte[] iv = new byte[IV_LENGTH_BYTE];
        byte[] ciphertext = new byte[combined.length - iv.length];
        System.arraycopy(combined, 0, iv, 0, iv.length);
        System.arraycopy(combined, iv.length, ciphertext, 0, ciphertext.length);

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

        // 解密数据
        return cipher.doFinal(ciphertext);
    }
}

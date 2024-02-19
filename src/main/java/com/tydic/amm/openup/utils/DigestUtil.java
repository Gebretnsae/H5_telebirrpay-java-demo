package com.tydic.amm.openup.utils;

import org.springframework.util.StringUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {
    public static final String SHA256 = "SHA-256";
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    public static String toSha256(String source) {
        return hash(SHA256, source, null, 0);
    }

    public static String toSha256(String source, String salt, int hashIterations) {
        return hash(SHA256, source, salt, hashIterations);
    }

    private static String hash(String algorithmName, String source, String salt, int hashIterations){
        if (StringUtils.isEmpty(source)) {
            return source;
        }
        if (!StringUtils.hasText(algorithmName)) {
            String msg = "algorithmName argument cannot be null or empty.";
            throw new RuntimeException("algorithmName argument cannot be null or empty.");
        } else {
            byte[] saltBytes = null;
            if (salt != null) {
                saltBytes = salt.getBytes();
            }
            return toHex(hash(algorithmName, source.getBytes(), saltBytes, hashIterations));
        }
    }

    private static byte[] hash(String algorithmName, byte[] bytes, byte[] salt, int hashIterations){
        MessageDigest digest = getDigest(algorithmName);
        if (salt != null) {
            digest.reset();
            digest.update(salt);
        }
        byte[] hashed = digest.digest(bytes);
        int iterations = Math.max(1, hashIterations) - 1;
        for (int i = 0; i < iterations; ++i) {
            digest.reset();
            hashed = digest.digest(hashed);
        }
        return hashed;
    }

    private static MessageDigest getDigest(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException var4) {
            String msg = "No native '" + algorithmName + "' MessageDigest instance available on the current JVM.";
            throw new RuntimeException(msg);
        }
    }

    private static String toHex(byte[] alreadyHashedBytes) {
        return encodeToString(alreadyHashedBytes);
    }

    private static String encodeToString(byte[] bytes) {
        char[] encodedChars = encode(bytes);
        return new String(encodedChars);
    }

    private static char[] encode(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;
        for (int var4 = 0; i < l; ++i) {
            out[var4++] = HEX_DIGITS[(240 & data[i]) >>> 4];
            out[var4++] = HEX_DIGITS[15 & data[i]];
        }
        return out;
    }
}

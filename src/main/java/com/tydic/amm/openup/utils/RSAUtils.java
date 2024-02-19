package com.tydic.amm.openup.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;


public class RSAUtils {

    private static final String KEY_ALGORITHM = "RSA";
    private static final Integer KEY_SIZE = 2048;

    private static final int MAX_ENCRYPT_BLOCK = 117;

    private static final int MAX_DECRYPT_BLOCK = KEY_SIZE / 8;



    public static String decryptByPublicKey(String encryptedData, String publicKey) {
        try{
            return new String(decryptByPublicKey(Base64.decodeBase64(encryptedData), publicKey));
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * public key decryption
     * </p>
     *
     * @param encryptedData encrypted data
     * @param publicKey Public key (BASE64 encoded)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    public static byte[] encryptByPublicKey(byte[] data, String publicKey){
        try {
            byte[] keyBytes = Base64.decodeBase64(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static String encryptByPublicKey(String data, String publicKey) {
        return Base64.encodeBase64String(encryptByPublicKey(data.getBytes(), publicKey));
    }
}

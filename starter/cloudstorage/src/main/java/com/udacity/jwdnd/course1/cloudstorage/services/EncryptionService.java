package com.udacity.jwdnd.course1.cloudstorage.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {
    private Logger logger = LoggerFactory.getLogger(EncryptionService.class);

    public String encryptValue(String data, String key) {
        byte[] encryptedValue = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedValue = cipher.doFinal(data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(e.getMessage());
        }

        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public String decryptValue(String data, String key) {
        byte[] decryptedValue = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedValue = cipher.doFinal(Base64.getDecoder().decode(data));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(e.getMessage());
        }

        return new String(decryptedValue);
    }

    public String genKey() {
        byte[] key = new byte[16];
        try {
            SecureRandom.getInstanceStrong().nextBytes(key);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(key);
    }

    public String encrypt(String data) {
        var key = genKey();
        return key + encryptValue(data, key);
    }

    public String decrypt(String data) {
        return decryptValue(data.substring(24), data.substring(0, 24));
    }
}

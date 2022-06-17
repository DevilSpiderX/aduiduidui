package com.dali3a215.aduiduidui.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AduiCipher {
    private static final Logger logger = LoggerFactory.getLogger(AduiCipher.class);

    public static String sha256Encrypt(String value) {
        StringBuilder resultBld = new StringBuilder();
        try {
            MessageDigest SHA256Digest = MessageDigest.getInstance("SHA-256");
            byte[] buff = SHA256Digest.digest(value.getBytes(StandardCharsets.UTF_8));
            for (byte b : buff) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    resultBld.append(0);
                }
                resultBld.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }
        return resultBld.toString();
    }
}

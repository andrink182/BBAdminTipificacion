/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bbadmintipificacion.utils;

/**
 *
 * @author crivera
 */

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author jguzman
 */
@Component
@PropertySource("classpath:application.properties")
public class GBNDEncrypt {
    private final String ENCRYPT_KEY;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    
        public GBNDEncrypt(@Value("${GBNBBKy}") String key){
        if(key == null || key.isEmpty()) throw new IllegalStateException("Encri not set");
        this.ENCRYPT_KEY = key;
    }
         public String encrypt(String text) throws Exception {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Key aesKey = new SecretKeySpec(ENCRYPT_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

        byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));

        // Combinar IV y datos encriptados
        byte[] encryptedWithIv = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, encryptedWithIv, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(encryptedWithIv);
    }

    public String decrypt(String encrypted) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encrypted);

        byte[] iv = new byte[GCM_IV_LENGTH];
        byte[] encryptedData = new byte[encryptedBytes.length - GCM_IV_LENGTH];
        System.arraycopy(encryptedBytes, 0, iv, 0, iv.length);
        System.arraycopy(encryptedBytes, iv.length, encryptedData, 0, encryptedData.length);

        Key aesKey = new SecretKeySpec(ENCRYPT_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
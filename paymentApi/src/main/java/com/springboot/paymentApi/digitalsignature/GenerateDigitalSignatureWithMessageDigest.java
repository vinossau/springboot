package com.springboot.paymentApi.digitalsignature;

import com.springboot.paymentApi.util.Utility;

import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.PrivateKey;

public class GenerateDigitalSignatureWithMessageDigest {

    private void createDigitalSignatureForPrivateKey() throws Exception{
        PrivateKey privateKey = Utility.getPrivateKey();

        byte[] messageBytes = Files.readAllBytes(Paths.get("message.txt"));

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageHash = md.digest(messageBytes);

        //Encrypting generated hash

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] digitalSignature = cipher.doFinal(messageHash);

        Files.write(Paths.get("digital_signature_1"), digitalSignature);


    }
}

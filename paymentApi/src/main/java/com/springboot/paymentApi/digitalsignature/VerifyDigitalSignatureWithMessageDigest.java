package com.springboot.paymentApi.digitalsignature;

import com.springboot.paymentApi.util.Utility;

import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Arrays;

public class VerifyDigitalSignatureWithMessageDigest {

    private void verifyDigitalSignatureWithPublicKey() throws Exception{

        PublicKey publicKey = Utility.getPublicKey();
        byte[] encryptedMessageHash =
                Files.readAllBytes(Paths.get("digital_signature_1"));
        //create a Cipher instance. Then we call the doFinal method:

        Cipher cipherFinal = Cipher.getInstance("RSA");
        cipherFinal.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedMessageHash = cipherFinal.doFinal(encryptedMessageHash);

        //Generate a new message hash from the received message:

        byte[] messageBytes1 = Files.readAllBytes(Paths.get("message.txt"));

        MessageDigest md1 = MessageDigest.getInstance("SHA-256");
        byte[] newMessageHash = md1.digest(messageBytes1);
        // check if the newly generated message hash matches the decrypted one:

        boolean isCorrect = Arrays.equals(decryptedMessageHash, newMessageHash);
    }
}


package com.springboot.paymentApi.digitalsignature;

import com.springboot.paymentApi.util.Utility;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class VerifySignature {
    public boolean verifySignatureWithPublicKey()throws Exception{

        generateSignatureForPrivateKey();
        PublicKey publicKey = Utility.getPublicKey();
        System.out.println("public key"+publicKey);
        byte[] sig = Files.readAllBytes(Paths.get("target/digital_signature_2"));

        Signature signature = Signature.getInstance(Utility.SIGNING_ALGORITHM);
        signature.initVerify(publicKey);

        byte[] messageBytes = Files.readAllBytes(Paths.get("src/test/resources/digitalsignature/message.txt"));

        signature.update(messageBytes);

        boolean isCorrect = signature.verify(sig);
        System.out.println("Signature " + (isCorrect ? "correct" : "incorrect"));
        return isCorrect;
    }

    private void generateSignatureForPrivateKey() throws Exception{
        PrivateKey privateKey = Utility.getPrivateKey();
        System.out.println("private key"+privateKey);
        Signature signature = Signature.getInstance(Utility.SIGNING_ALGORITHM);
        signature.initSign(privateKey);

        byte[] messageBytes = Files.readAllBytes(Paths.get("src/test/resources/digitalsignature/message.txt"));

        signature.update(messageBytes);
        byte[] digitalSignature = signature.sign();

        Files.write(Paths.get("target/digital_signature_2"), digitalSignature);
    }
}

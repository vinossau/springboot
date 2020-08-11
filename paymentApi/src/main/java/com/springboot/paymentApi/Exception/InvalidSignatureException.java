package com.springboot.paymentApi.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvalidSignatureException extends Exception{
    private static final long serialVersionUID = 1L;
    public InvalidSignatureException(){
        super("Invalid Signature Certificate");
    }
}
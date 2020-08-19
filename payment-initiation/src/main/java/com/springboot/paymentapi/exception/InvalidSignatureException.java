package com.springboot.paymentapi.exception;


public class InvalidSignatureException extends RuntimeException
{
    private static final long serialVersionUID = -3743351816087221324L;

    public InvalidSignatureException(final String message)
    {
        super(message);
    }

}

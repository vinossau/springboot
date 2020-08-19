package com.springboot.paymentapi.exception;

public class UnknownCertificateException extends RuntimeException
{

    private static final long serialVersionUID = 8444789794367798555L;

    public UnknownCertificateException(final String message)
    {
        super(message);
    }
}

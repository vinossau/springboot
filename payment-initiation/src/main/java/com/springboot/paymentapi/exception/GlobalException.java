package com.springboot.paymentapi.exception;

public class GlobalException extends RuntimeException
{
    private static final long serialVersionUID = -5004506432311101053L;

    public GlobalException(final String message)
    {
        super(message);
    }
}

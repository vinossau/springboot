package com.springboot.paymentapi.exception;


public class LimitExceededException extends RuntimeException
{
    private static final long serialVersionUID = 290619057379157935L;

    public LimitExceededException(final String message)
    {
        super(message);
    }

}

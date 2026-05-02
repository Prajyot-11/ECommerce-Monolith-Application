package com.ecommerce.project.exception;


public class APIException extends RuntimeException
{
    public APIException()
    {
    }

    public APIException(String message)
    {
        super(message);
    }
}

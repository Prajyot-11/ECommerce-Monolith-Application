package com.ecommerce.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

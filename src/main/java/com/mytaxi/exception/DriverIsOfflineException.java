package com.mytaxi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DriverIsOfflineException extends Exception
{
    private static final long serialVersionUID = 3730567542050193793L;


    public DriverIsOfflineException(String message)
    {
        super(message);
    }

}

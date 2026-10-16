package com.example.usermanagement.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String msg){
        super(msg);
    }
}

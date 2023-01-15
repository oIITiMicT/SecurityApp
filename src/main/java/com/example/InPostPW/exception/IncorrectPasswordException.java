package com.example.InPostPW.exception;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException(String message, Exception e) {
        super(message, e);
    }

    public IncorrectPasswordException(String message) {
        super(message);
    }

}

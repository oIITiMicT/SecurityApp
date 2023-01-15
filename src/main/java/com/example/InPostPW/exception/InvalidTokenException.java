package com.example.InPostPW.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message, Exception e) {
        super(message, e);
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}

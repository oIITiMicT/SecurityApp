package com.example.InPostPW.exception;

public class UserNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User not found: ";
    private String username;

    public UserNotFoundException(String username) {
        this(DEFAULT_MESSAGE + username, username);
    }

    public UserNotFoundException(String message, String username) {
        super(message);
        this.username = username;
    }

    public UserNotFoundException(String message, String username, Exception e) {
        super(message, e);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
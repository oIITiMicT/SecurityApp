package com.example.InPostPW.exception;

public class RoleNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Role not found: ";
    private String name;

    public RoleNotFoundException(String name) {
        this(DEFAULT_MESSAGE + name, name);
    }

    public RoleNotFoundException(String message, String name) {
        super(message);
        this.name = name;
    }

    public RoleNotFoundException(String message, String name, Exception e) {
        super(message, e);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
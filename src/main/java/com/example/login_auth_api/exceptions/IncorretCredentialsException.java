package com.example.login_auth_api.exceptions;

public class IncorretCredentialsException extends RuntimeException {

    public IncorretCredentialsException() {
        super("Username or password is incorrect!");
    }

    public IncorretCredentialsException(String message) {
        super(message);
    }
}
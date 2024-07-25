package com.example.login_auth_api.exceptions;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException() {
        super("User not authenticated!");
    }

    public UserNotAuthenticatedException(String message) {
        super(message);
    }
}

package com.example.login_auth_api.exceptions;

public class UserAlreadyRegistredException extends RuntimeException {

    public UserAlreadyRegistredException() {
        super("User is already registrated!");
    }

    public UserAlreadyRegistredException(String message) {
        super(message);
    }
}

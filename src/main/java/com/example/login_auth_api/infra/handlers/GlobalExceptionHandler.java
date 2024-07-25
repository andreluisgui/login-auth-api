package com.example.login_auth_api.infra.handlers;

import com.example.login_auth_api.exceptions.IncorretCredentialsException;
import com.example.login_auth_api.exceptions.UserAlreadyRegistredException;
import com.example.login_auth_api.exceptions.UserNotAuthenticatedException;
import com.example.login_auth_api.exceptions.UserNotFoundException;
import com.example.login_auth_api.utils.requests.RestErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestErrorMessage> handleUserNotFoundException(UserNotFoundException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return new ResponseEntity<>(restErrorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<RestErrorMessage> handleUserNotAuthenticatedException(UserNotAuthenticatedException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.UNAUTHORIZED, exception.getMessage());
        return new ResponseEntity<>(restErrorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyRegistredException.class)
    public ResponseEntity<RestErrorMessage> handleUserAlreadyRegistredException(UserAlreadyRegistredException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
        return new ResponseEntity<>(restErrorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncorretCredentialsException.class)
    public ResponseEntity<RestErrorMessage> handleIncorrectCredentialsException(IncorretCredentialsException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(restErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorMessage> handleAllExceptions(Exception exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        return new ResponseEntity<>(restErrorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
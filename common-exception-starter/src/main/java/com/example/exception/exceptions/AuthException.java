package com.example.exception.exceptions;

/**
 * AuthException class extends RuntimeException class.
 * An exception is thrown in case of an authentication error, for example, an invalid token
 * or the absence of a user in the database
 * @author Artur Malashkov
 */
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

}

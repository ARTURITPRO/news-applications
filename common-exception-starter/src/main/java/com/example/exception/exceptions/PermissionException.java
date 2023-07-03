package com.example.exception.exceptions;

/**
 * PermissionException class extends RuntimeException class.
 * An exception is thrown when an attempt is made to perform an operation with no permissions.
 * @author Artur Malashkov
 */
public class PermissionException extends RuntimeException {

    public PermissionException(String message) {
        super(message);
    }
}

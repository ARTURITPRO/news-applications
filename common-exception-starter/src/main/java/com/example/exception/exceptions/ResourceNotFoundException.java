package com.example.exception.exceptions;

/**
 * ResourceNotFoundException class extends RuntimeException class.
 * Class is thrown in case of not found entity
 * @author Artur Malashkov
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}

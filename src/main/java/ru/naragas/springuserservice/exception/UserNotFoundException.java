package ru.naragas.springuserservice.exception;


import lombok.experimental.StandardException;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/17/2025
 */
@StandardException
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer id) {
        super("User not found. Id: " + id);
    }
}

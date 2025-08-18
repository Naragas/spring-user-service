package ru.naragas.springuserservice.exception;


import lombok.experimental.StandardException;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/18/2025
 */
@StandardException
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email already Exists in DB" + email);
    }
}

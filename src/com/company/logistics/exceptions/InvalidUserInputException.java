package com.company.logistics.exceptions;

public class InvalidUserInputException extends RuntimeException {
    public InvalidUserInputException(String message) {
        super(message);
    }
}

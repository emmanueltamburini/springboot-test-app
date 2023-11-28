package com.emmanueltamburini.test.springboot.app.springboot_test.exceptions;

public class InsufficientMoneyException extends RuntimeException {
    public InsufficientMoneyException(String message) {
        super(message);
    }
}

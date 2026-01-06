package com.banking.exception;

public class AccountException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public AccountException(String message) {
        super(message);
    }
    
    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
package com.banking.exception;

public class InsufficientBalanceException extends AccountException {
    
    private static final long serialVersionUID = 1L;
    
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
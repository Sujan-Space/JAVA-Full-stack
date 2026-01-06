package com.banking.exception;
public class AccountNotFoundException extends AccountException {
    
    private static final long serialVersionUID = 1L;
    
    public AccountNotFoundException(String message) {
        super(message);
    }
    
    public AccountNotFoundException(Long accountId) {
        super("Account not found with ID: " + accountId);
    }
    
    public AccountNotFoundException(String field, String value) {
        super("Account not found with " + field + ": " + value);
    }
}
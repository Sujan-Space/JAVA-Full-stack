package com.banking.exception;

/**
 * Exception thrown for duplicate account numbers
 */
public class DuplicateAccountException extends AccountException {
    
    private static final long serialVersionUID = 1L;
    
    public DuplicateAccountException(String accountNumber) {
        super("Account already exists with account number: " + accountNumber);
    }
}
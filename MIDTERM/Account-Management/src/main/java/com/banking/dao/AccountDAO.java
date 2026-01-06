package com.banking.dao;

import com.banking.entity.Account;
import com.banking.exception.*;
import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {
    
    
    Account createAccount(Account account) throws DatabaseException, DuplicateAccountException;
    
    Account getAccountById(Long accountId) throws AccountNotFoundException, DatabaseException;
   
    Account getAccountByNumber(String accountNumber) throws AccountNotFoundException, DatabaseException;
    
    List<Account> getAllAccounts() throws DatabaseException;
    
    List<Account> getActiveAccounts() throws DatabaseException;
    
    Account updateAccount(Account account) throws AccountNotFoundException, DatabaseException;
 
    boolean deleteAccount(Long accountId) throws AccountNotFoundException, DatabaseException;

    boolean permanentDeleteAccount(Long accountId) throws AccountNotFoundException, DatabaseException;
    
    Account deposit(Long accountId, BigDecimal amount) 
        throws AccountNotFoundException, TransactionException, IllegalArgumentException;
   
    Account withdraw(Long accountId, BigDecimal amount) 
        throws AccountNotFoundException, InsufficientBalanceException, TransactionException, IllegalArgumentException;
 
    boolean transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount) 
        throws AccountNotFoundException, InsufficientBalanceException, TransactionException, IllegalArgumentException;
}
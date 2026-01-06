package com.banking.dao;

import com.banking.entity.Account;
import com.banking.exception.*;
import com.banking.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of AccountDAO interface
 * Handles all database operations for Account entity
 */
public class AccountDAOImpl implements AccountDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(AccountDAOImpl.class);
    
    @Override
    public Account createAccount(Account account) throws DatabaseException, DuplicateAccountException {
        Transaction transaction = null;
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            logger.info("Creating new account: {}", account.getAccountNumber());
            
            // Check if account number already exists
            Query<Long> query = session.createQuery(
                "SELECT COUNT(a) FROM Account a WHERE a.accountNumber = :accountNumber", 
                Long.class
            );
            query.setParameter("accountNumber", account.getAccountNumber());
            Long count = query.uniqueResult();
            
            if (count > 0) {
                throw new DuplicateAccountException(account.getAccountNumber());
            }
            
            session.persist(account);
            transaction.commit();
            
            logger.info("Account created successfully with ID: {}", account.getAccountId());
            return account;
            
        } catch (DuplicateAccountException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Duplicate account number: {}", account.getAccountNumber());
            throw e;
        } catch (ConstraintViolationException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Constraint violation while creating account", e);
            throw new DuplicateAccountException(account.getAccountNumber());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error creating account", e);
            throw new DatabaseException("Failed to create account: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public Account getAccountById(Long accountId) throws AccountNotFoundException, DatabaseException {
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            logger.info("Retrieving account with ID: {}", accountId);
            
            Account account = session.get(Account.class, accountId);
            
            if (account == null) {
                throw new AccountNotFoundException(accountId);
            }
            
            logger.info("Account found: {}", account.getAccountNumber());
            return account;
            
        } catch (AccountNotFoundException e) {
            logger.error("Account not found with ID: {}", accountId);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving account by ID", e);
            throw new DatabaseException("Failed to retrieve account: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public Account getAccountByNumber(String accountNumber) throws AccountNotFoundException, DatabaseException {
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            logger.info("Retrieving account with number: {}", accountNumber);
            
            Query<Account> query = session.createQuery(
                "FROM Account WHERE accountNumber = :accountNumber", 
                Account.class
            );
            query.setParameter("accountNumber", accountNumber);
            
            Account account = query.uniqueResult();
            
            if (account == null) {
                throw new AccountNotFoundException("accountNumber", accountNumber);
            }
            
            logger.info("Account found with ID: {}", account.getAccountId());
            return account;
            
        } catch (AccountNotFoundException e) {
            logger.error("Account not found with number: {}", accountNumber);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving account by number", e);
            throw new DatabaseException("Failed to retrieve account: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public List<Account> getAllAccounts() throws DatabaseException {
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            logger.info("Retrieving all accounts");
            
            Query<Account> query = session.createQuery("FROM Account", Account.class);
            List<Account> accounts = query.list();
            
            logger.info("Retrieved {} accounts", accounts.size());
            return accounts;
            
        } catch (Exception e) {
            logger.error("Error retrieving all accounts", e);
            throw new DatabaseException("Failed to retrieve accounts: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public List<Account> getActiveAccounts() throws DatabaseException {
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            logger.info("Retrieving active accounts");
            
            Query<Account> query = session.createQuery(
                "FROM Account WHERE isActive = true", 
                Account.class
            );
            List<Account> accounts = query.list();
            
            logger.info("Retrieved {} active accounts", accounts.size());
            return accounts;
            
        } catch (Exception e) {
            logger.error("Error retrieving active accounts", e);
            throw new DatabaseException("Failed to retrieve active accounts: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public Account updateAccount(Account account) throws AccountNotFoundException, DatabaseException {
        Transaction transaction = null;
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            logger.info("Updating account with ID: {}", account.getAccountId());
            
            // Check if account exists
            Account existingAccount = session.get(Account.class, account.getAccountId());
            if (existingAccount == null) {
                throw new AccountNotFoundException(account.getAccountId());
            }
            
            Account updatedAccount = session.merge(account);
            transaction.commit();
            
            logger.info("Account updated successfully: {}", updatedAccount.getAccountId());
            return updatedAccount;
            
        } catch (AccountNotFoundException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Account not found for update: {}", account.getAccountId());
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating account", e);
            throw new DatabaseException("Failed to update account: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public boolean deleteAccount(Long accountId) throws AccountNotFoundException, DatabaseException {
        Transaction transaction = null;
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            logger.info("Soft deleting account with ID: {}", accountId);
            
            Account account = session.get(Account.class, accountId);
            if (account == null) {
                throw new AccountNotFoundException(accountId);
            }
            
           
            session.merge(account);
            transaction.commit();
            
            logger.info("Account soft deleted successfully: {}", accountId);
            return true;
            
        } catch (AccountNotFoundException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Account not found for deletion: {}", accountId);
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting account", e);
            throw new DatabaseException("Failed to delete account: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public boolean permanentDeleteAccount(Long accountId) throws AccountNotFoundException, DatabaseException {
        Transaction transaction = null;
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            logger.info("Permanently deleting account with ID: {}", accountId);
            
            Account account = session.get(Account.class, accountId);
            if (account == null) {
                throw new AccountNotFoundException(accountId);
            }
            
            session.remove(account);
            transaction.commit();
            
            logger.info("Account permanently deleted: {}", accountId);
            return true;
            
        } catch (AccountNotFoundException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Account not found for permanent deletion: {}", accountId);
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error permanently deleting account", e);
            throw new DatabaseException("Failed to permanently delete account: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public Account deposit(Long accountId, BigDecimal amount) 
            throws AccountNotFoundException, TransactionException, IllegalArgumentException {
        Transaction transaction = null;
        Session session = null;
        
        try {
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Deposit amount must be positive");
            }
            
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            logger.info("Depositing {} to account ID: {}", amount, accountId);
            
            Account account = session.get(Account.class, accountId);
            if (account == null) {
                throw new AccountNotFoundException(accountId);
            }
            
           
            
            account.deposit(amount);
            session.merge(account);
            transaction.commit();
            
            logger.info("Deposit successful. New balance: {}", account.getBalance());
            return account;
            
        } catch (AccountNotFoundException | IllegalArgumentException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Deposit failed", e);
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Transaction error during deposit", e);
            throw new TransactionException("Deposit transaction failed: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public Account withdraw(Long accountId, BigDecimal amount) 
            throws AccountNotFoundException, InsufficientBalanceException, TransactionException, IllegalArgumentException {
        Transaction transaction = null;
        Session session = null;
        
        try {
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be positive");
            }
            
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            logger.info("Withdrawing {} from account ID: {}", amount, accountId);
            
            Account account = session.get(Account.class, accountId);
            if (account == null) {
                throw new AccountNotFoundException(accountId);
            }
            
          
            
            if (account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientBalanceException(
                    "Insufficient balance. Available: " + account.getBalance() + ", Required: " + amount
                );
            }
            
            account.withdraw(amount);
            session.merge(account);
            transaction.commit();
            
            logger.info("Withdrawal successful. New balance: {}", account.getBalance());
            return account;
            
        } catch (AccountNotFoundException | IllegalArgumentException | InsufficientBalanceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Withdrawal failed", e);
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Transaction error during withdrawal", e);
            throw new TransactionException("Withdrawal transaction failed: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public boolean transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount) 
            throws AccountNotFoundException, InsufficientBalanceException, TransactionException, IllegalArgumentException {
        Transaction transaction = null;
        Session session = null;
        
        try {
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive");
            }
            
            if (fromAccountId.equals(toAccountId)) {
                throw new IllegalArgumentException("Cannot transfer to the same account");
            }
            
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            logger.info("Transferring {} from account {} to account {}", amount, fromAccountId, toAccountId);
            
            // Get both accounts
            Account fromAccount = session.get(Account.class, fromAccountId);
            if (fromAccount == null) {
                throw new AccountNotFoundException("Source account not found: " + fromAccountId);
            }
            
            Account toAccount = session.get(Account.class, toAccountId);
            if (toAccount == null) {
                throw new AccountNotFoundException("Destination account not found: " + toAccountId);
            }
           
            
            // Check balance
            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientBalanceException(
                    "Insufficient balance in source account. Available: " + 
                    fromAccount.getBalance() + ", Required: " + amount
                );
            }
            
            // Perform transfer
            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
            
            session.merge(fromAccount);
            session.merge(toAccount);
            
            transaction.commit();
            
            logger.info("Transfer successful. From balance: {}, To balance: {}", 
                       fromAccount.getBalance(), toAccount.getBalance());
            return true;
            
        } catch (AccountNotFoundException | IllegalArgumentException | InsufficientBalanceException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Transfer failed", e);
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Transaction error during transfer", e);
            throw new TransactionException("Transfer transaction failed: " + e.getMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
package com.bank.dao;

import com.bank.model.Account;
import com.bank.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data Access Object for Account entity.
 * Provides CRUD operations using both Hibernate Session and JPA EntityManager.
 */
public class AccountDao {

    private static final Logger logger = LoggerFactory.getLogger(AccountDao.class);

    // ==================== HIBERNATE SESSION APPROACH ====================

    /**
     * Create a new account using Hibernate Session
     */
    public boolean createAccount(Account account) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            session.save(account);
            
            transaction.commit();
            logger.info("Account created successfully: {}", account.getAccountNumber());
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to create account", e);
            return false;
        }
    }

    /**
     * Retrieve an account by account number using Hibernate Session
     */
    public Account getAccount(String accountNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Account account = session.get(Account.class, accountNumber);
            if (account != null) {
                logger.info("Account retrieved: {}", accountNumber);
            } else {
                logger.warn("Account not found: {}", accountNumber);
            }
            return account;
        } catch (Exception e) {
            logger.error("Failed to retrieve account", e);
            return null;
        }
    }

    /**
     * Update account balance using Hibernate Session
     */
    public boolean updateBalance(String accountNumber, BigDecimal newBalance) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Account account = session.get(Account.class, accountNumber);
            if (account != null) {
                account.setBalance(newBalance);
                session.update(account);
                transaction.commit();
                logger.info("Balance updated for account {}: {}", accountNumber, newBalance);
                return true;
            } else {
                logger.warn("Account not found for update: {}", accountNumber);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update balance", e);
            return false;
        }
    }

    /**
     * Perform transaction (deposit/withdrawal) using Hibernate Session
     */
    public boolean performTransaction(String accountNumber, BigDecimal amount, String transactionType) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Account account = session.get(Account.class, accountNumber);
            if (account != null) {
                BigDecimal currentBalance = account.getBalance();
                BigDecimal newBalance;
                
                if ("DEPOSIT".equalsIgnoreCase(transactionType)) {
                    newBalance = currentBalance.add(amount);
                } else if ("WITHDRAW".equalsIgnoreCase(transactionType)) {
                    if (currentBalance.compareTo(amount) < 0) {
                        logger.warn("Insufficient balance for withdrawal");
                        return false;
                    }
                    newBalance = currentBalance.subtract(amount);
                } else {
                    logger.error("Invalid transaction type: {}", transactionType);
                    return false;
                }
                
                account.setBalance(newBalance);
                session.update(account);
                transaction.commit();
                logger.info("{} transaction completed for account {}: {}", 
                           transactionType, accountNumber, amount);
                return true;
            } else {
                logger.warn("Account not found: {}", accountNumber);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Transaction failed", e);
            return false;
        }
    }

    /**
     * Delete an account using Hibernate Session
     */
    public boolean deleteAccount(String accountNumber) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            Account account = session.get(Account.class, accountNumber);
            if (account != null) {
                session.delete(account);
                transaction.commit();
                logger.info("Account deleted: {}", accountNumber);
                return true;
            } else {
                logger.warn("Account not found for deletion: {}", accountNumber);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to delete account", e);
            return false;
        }
    }

    /**
     * Get all accounts using Hibernate Session
     */
    public List<Account> getAllAccounts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Account> accounts = session.createQuery("FROM Account", Account.class).list();
            logger.info("Retrieved {} accounts", accounts.size());
            return accounts;
        } catch (Exception e) {
            logger.error("Failed to retrieve all accounts", e);
            return null;
        }
    }

    // ==================== JPA ENTITY MANAGER APPROACH ====================

    /**
     * Create a new account using JPA EntityManager
     */
    public boolean createAccountJPA(Account account) {
        EntityTransaction transaction = null;
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            
            entityManager.persist(account);
            
            transaction.commit();
            logger.info("Account created successfully (JPA): {}", account.getAccountNumber());
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Failed to create account (JPA)", e);
            return false;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    /**
     * Retrieve an account by account number using JPA EntityManager
     */
    public Account getAccountJPA(String accountNumber) {
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
            Account account = entityManager.find(Account.class, accountNumber);
            if (account != null) {
                logger.info("Account retrieved (JPA): {}", accountNumber);
            } else {
                logger.warn("Account not found (JPA): {}", accountNumber);
            }
            return account;
        } catch (Exception e) {
            logger.error("Failed to retrieve account (JPA)", e);
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    /**
     * Update account balance using JPA EntityManager
     */
    public boolean updateBalanceJPA(String accountNumber, BigDecimal newBalance) {
        EntityTransaction transaction = null;
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            
            Account account = entityManager.find(Account.class, accountNumber);
            if (account != null) {
                account.setBalance(newBalance);
                entityManager.merge(account);
                transaction.commit();
                logger.info("Balance updated (JPA) for account {}: {}", accountNumber, newBalance);
                return true;
            } else {
                logger.warn("Account not found for update (JPA): {}", accountNumber);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Failed to update balance (JPA)", e);
            return false;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    /**
     * Delete an account using JPA EntityManager
     */
    public boolean deleteAccountJPA(String accountNumber) {
        EntityTransaction transaction = null;
        EntityManager entityManager = null;
        try {
            entityManager = HibernateUtil.getEntityManagerFactory().createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            
            Account account = entityManager.find(Account.class, accountNumber);
            if (account != null) {
                entityManager.remove(account);
                transaction.commit();
                logger.info("Account deleted (JPA): {}", accountNumber);
                return true;
            } else {
                logger.warn("Account not found for deletion (JPA): {}", accountNumber);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Failed to delete account (JPA)", e);
            return false;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}
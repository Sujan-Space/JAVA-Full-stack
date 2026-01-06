package com.bank;

import com.bank.dao.AccountDao;
import com.bank.model.Account;
import com.bank.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class demonstrating CRUD operations on bank accounts
 * using Hibernate ORM in a BFSI context.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final AccountDao accountDAO = new AccountDao();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        logger.info("=== Bank Account Management System Started ===");

        try {
            // Initialize Hibernate
            HibernateUtil.getSessionFactory();

            // Run demonstration
            demonstrateCRUDOperations();

            // Interactive menu
            boolean exit = false;
            while (!exit) {
                displayMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        createNewAccount();
                        break;
                    case 2:
                        viewAccountDetails();
                        break;
                    case 3:
                        performTransaction();
                        break;
                    case 4:
                        viewAllAccounts();
                        break;
                    case 5:
                        closeAccount();
                        break;
                    case 6:
                        demonstrateJPAOperations();
                        break;
                    case 0:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            logger.error("Application error", e);
        } finally {
            // Cleanup
            scanner.close();
            HibernateUtil.shutdown();
            logger.info("=== Bank Account Management System Stopped ===");
        }
    }
    private static void demonstrateCRUDOperations() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMONSTRATING HIBERNATE CRUD OPERATIONS");
        System.out.println("=".repeat(60));

        // CREATE - Create new accounts
        System.out.println("\n1. CREATE Operation - Creating new accounts...");
        Account acc1 = new Account("ACC001", "John Doe", "SAVINGS", new BigDecimal("5000.00"));
        Account acc2 = new Account("ACC002", "Jane Smith", "CURRENT", new BigDecimal("10000.00"));
        Account acc3 = new Account("ACC003", "Robert Johnson", "FIXED_DEPOSIT", new BigDecimal("50000.00"));

        accountDAO.createAccount(acc1);
        accountDAO.createAccount(acc2);
        accountDAO.createAccount(acc3);
        System.out.println("✓ Three accounts created successfully");

        // READ - Retrieve account details
        System.out.println("\n2. READ Operation - Retrieving account details...");
        Account retrievedAccount = accountDAO.getAccount("ACC001");
        if (retrievedAccount != null) {
            System.out.println("✓ Account Retrieved: " + retrievedAccount);
        }

        // UPDATE - Update account balance after transaction
        System.out.println("\n3. UPDATE Operation - Performing transactions...");
        System.out.println("   - Depositing $2000 to ACC001");
        accountDAO.performTransaction("ACC001", new BigDecimal("2000.00"), "DEPOSIT");
        
        System.out.println("   - Withdrawing $500 from ACC002");
        accountDAO.performTransaction("ACC002", new BigDecimal("500.00"), "WITHDRAW");

        Account updatedAccount = accountDAO.getAccount("ACC001");
        System.out.println("✓ Updated Account Balance: " + updatedAccount.getBalance());

        // READ ALL - List all accounts
        System.out.println("\n4. READ ALL Operation - Listing all accounts...");
        List<Account> allAccounts = accountDAO.getAllAccounts();
        if (allAccounts != null) {
            allAccounts.forEach(acc -> System.out.println("   " + acc));
        }

        // DELETE - Close an account
        System.out.println("\n5. DELETE Operation - Closing account ACC003...");
        accountDAO.deleteAccount("ACC003");
        System.out.println("✓ Account closed successfully");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMONSTRATION COMPLETED");
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Display interactive menu
     */
    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("BANK ACCOUNT MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1. Create New Account");
        System.out.println("2. View Account Details");
        System.out.println("3. Perform Transaction (Deposit/Withdraw)");
        System.out.println("4. View All Accounts");
        System.out.println("5. Close Account");
        System.out.println("6. Demonstrate JPA Operations");
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
    }

    /**
     * Create a new account interactively
     */
    private static void createNewAccount() {
        System.out.println("\n--- Create New Account ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        
        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine();
        
        System.out.print("Enter Account Type (SAVINGS/CURRENT/FIXED_DEPOSIT): ");
        String accountType = scanner.nextLine();
        
        BigDecimal initialBalance = getBigDecimalInput("Enter Initial Balance: ");

        Account account = new Account(accountNumber, customerName, accountType, initialBalance);
        
        if (accountDAO.createAccount(account)) {
            System.out.println("✓ Account created successfully!");
        } else {
            System.out.println("✗ Failed to create account.");
        }
    }

    /**
     * View account details
     */
    private static void viewAccountDetails() {
        System.out.println("\n--- View Account Details ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        Account account = accountDAO.getAccount(accountNumber);
        if (account != null) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Customer Name: " + account.getCustomerName());
            System.out.println("Account Type: " + account.getAccountType());
            System.out.println("Balance: $" + account.getBalance());
            System.out.println("Status: " + account.getStatus());
            System.out.println("Created Date: " + account.getCreatedDate());
            System.out.println("=".repeat(50));
        } else {
            System.out.println("✗ Account not found.");
        }
    }

    /**
     * Perform transaction
     */
    private static void performTransaction() {
        System.out.println("\n--- Perform Transaction ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        
        System.out.print("Enter Transaction Type (DEPOSIT/WITHDRAW): ");
        String transactionType = scanner.nextLine();
        
        BigDecimal amount = getBigDecimalInput("Enter Amount: ");

        if (accountDAO.performTransaction(accountNumber, amount, transactionType)) {
            System.out.println("✓ Transaction completed successfully!");
            Account account = accountDAO.getAccount(accountNumber);
            if (account != null) {
                System.out.println("New Balance: $" + account.getBalance());
            }
        } else {
            System.out.println("✗ Transaction failed.");
        }
    }

    /**
     * View all accounts
     */
    private static void viewAllAccounts() {
        System.out.println("\n--- All Accounts ---");
        List<Account> accounts = accountDAO.getAllAccounts();
        
        if (accounts != null && !accounts.isEmpty()) {
            System.out.println(String.format("%-15s %-25s %-20s %-15s %-10s", 
                "Account No", "Customer Name", "Type", "Balance", "Status"));
            System.out.println("=".repeat(90));
            
            for (Account account : accounts) {
                System.out.println(String.format("%-15s %-25s %-20s $%-14s %-10s",
                    account.getAccountNumber(),
                    account.getCustomerName(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.getStatus()));
            }
        } else {
            System.out.println("No accounts found.");
        }
    }

    /**
     * Close an account
     */
    private static void closeAccount() {
        System.out.println("\n--- Close Account ---");
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        if (accountDAO.deleteAccount(accountNumber)) {
            System.out.println("✓ Account closed successfully!");
        } else {
            System.out.println("✗ Failed to close account.");
        }
    }

    /**
     * Demonstrate JPA EntityManager operations
     */
    private static void demonstrateJPAOperations() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEMONSTRATING JPA ENTITY MANAGER OPERATIONS");
        System.out.println("=".repeat(60));

        // CREATE using JPA
        System.out.println("\n1. CREATE using JPA...");
        Account jpaAccount = new Account("JPA001", "Michael Brown", "SAVINGS", new BigDecimal("7500.00"));
        accountDAO.createAccountJPA(jpaAccount);
        System.out.println("✓ Account created using JPA");

        // READ using JPA
        System.out.println("\n2. READ using JPA...");
        Account retrievedJPA = accountDAO.getAccountJPA("JPA001");
        if (retrievedJPA != null) {
            System.out.println("✓ Account Retrieved: " + retrievedJPA);
        }

        // UPDATE using JPA
        System.out.println("\n3. UPDATE using JPA...");
        accountDAO.updateBalanceJPA("JPA001", new BigDecimal("10000.00"));
        Account updatedJPA = accountDAO.getAccountJPA("JPA001");
        System.out.println("✓ Updated Balance: " + updatedJPA.getBalance());

        // DELETE using JPA
        System.out.println("\n4. DELETE using JPA...");
        accountDAO.deleteAccountJPA("JPA001");
        System.out.println("✓ Account deleted using JPA");

        System.out.println("\n" + "=".repeat(60));
        System.out.println("JPA DEMONSTRATION COMPLETED");
        System.out.println("=".repeat(60) + "\n");
    }

    
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Invalid input. " + prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); 
        return value;
    }

    private static BigDecimal getBigDecimalInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextBigDecimal()) {
            scanner.next();
            System.out.print("Invalid input. " + prompt);
        }
        BigDecimal value = scanner.nextBigDecimal();
        scanner.nextLine(); 
        return value;
    }
}
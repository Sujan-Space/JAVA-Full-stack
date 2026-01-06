package com.banking.main;

import com.banking.dao.AccountDAO;
import com.banking.dao.AccountDAOImpl;
import com.banking.entity.Account;
import com.banking.exception.*;
import com.banking.util.HibernateUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;


public class BankingApp {
    
    private static AccountDAO accountDAO = new AccountDAOImpl();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("\n");
        System.out.println("======== BANKING ACCOUNT MANAGEMENT SYSTEM (ORM) ========   ");
        System.out.println("\n");
        
        try {
            // Initialize Hibernate
            HibernateUtil.getSessionFactory();
            System.out.println("Database connection established successfully!\n");
            
            boolean running = true;
            
            while (running) {
                displayMenu();
                
                try {
                    int choice = Integer.parseInt(scanner.nextLine());
                    System.out.println();
                    
                    switch (choice) {
                        case 1:
                            createAccount();
                            break;
                        case 2:
                            viewAccountById();
                            break;
                        case 3:
                            viewAccountByNumber();
                            break;
                        case 4:
                            viewAllAccounts();
                            break;
                        case 5:
                            updateAccount();
                            break;
                        case 6:
                            depositMoney();
                            break;
                        case 7:
                            withdrawMoney();
                            break;
                        case 8:
                            transferFunds();
                            break;
                        case 9:
                            deleteAccount();
                            break;
                        case 10:
                            demonstrateTransactionRollback();
                            break;
                        case 0:
                            running = false;
                            System.out.println("Thank you for using Banking System. Goodbye!");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                    
                    if (running) {
                        System.out.println("\nPress Enter to continue...");
                        scanner.nextLine();
                    }
                    
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Critical error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cleanup
            scanner.close();
            HibernateUtil.shutdown();
            System.out.println("\nResources released successfully.");
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n==============================================");
        System.out.println("                 MAIN MENU                    ");
        System.out.println("1.  Create New Account");
        System.out.println("2.  View Account by ID");
        System.out.println("3.  View Account by Account Number");
        System.out.println("4.  View All Accounts");
        System.out.println("5.  Update Account Details");
        System.out.println("6.  Deposit Money");
        System.out.println("7.  Withdraw Money");
        System.out.println("8.  Transfer Funds");
        System.out.println("9.  Delete Account");
        System.out.println("10. Demonstrate Transaction Rollback");
        System.out.println("0.  Exit");
        System.out.println("==============================================");
        System.out.print("Enter your choice: ");
    }
    
    private static void createAccount() {
        System.out.println("--- CREATE NEW ACCOUNT ---");
        
        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            
            System.out.print("Enter Phone Number: ");
            String phone = scanner.nextLine().trim();
            
            System.out.print("Enter Initial Balance: ");
            BigDecimal balance = new BigDecimal(scanner.nextLine().trim());
            
            System.out.print("Enter Account Type (SAVINGS/CURRENT): ");
            String accountType = scanner.nextLine().trim().toUpperCase();
            
            // Create account object
            Account account = new Account(accountNumber, name, phone, balance, accountType);
            
            // Save to database
            Account createdAccount = accountDAO.createAccount(account);
            
            System.out.println("\nAccount created successfully!");
            displayAccountDetails(createdAccount);
            
        } catch (DuplicateAccountException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter valid numbers.");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    
    private static void viewAccountById() {
        System.out.println("--- VIEW ACCOUNT BY ID ---");
        
        try {
            System.out.print("Enter Account ID: ");
            Long accountId = Long.parseLong(scanner.nextLine().trim());
            
            Account account = accountDAO.getAccountById(accountId);
            displayAccountDetails(account);
            
        } catch (AccountNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format. Please enter a valid number.");
        }
    }
    
    private static void viewAccountByNumber() {
        System.out.println("--- VIEW ACCOUNT BY NUMBER ---");
        
        try {
            System.out.print("Enter Account Number: ");
            String accountNumber = scanner.nextLine().trim();
            
            Account account = accountDAO.getAccountByNumber(accountNumber);
            displayAccountDetails(account);
            
        } catch (AccountNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
        }
    }
    
    private static void viewAllAccounts() {
        System.out.println("--- ALL ACCOUNTS ---");
        
        try {
            List<Account> accounts = accountDAO.getAllAccounts();
            
            if (accounts.isEmpty()) {
                System.out.println("No accounts found in the database.");
            } else {
                System.out.println("\nTotal Accounts: " + accounts.size());
                System.out.println("─".repeat(100));
                System.out.printf("%-5s %-15s %-25s %-15s %-15s %-15s%n",
                    "ID", "Acc Number", "Name", "Phone", "Balance", "Type");
                System.out.println("─".repeat(100));
                
                for (Account account : accounts) {
                    System.out.printf("%-5d %-15s %-25s %-15s %-15s %-15s%n",
                        account.getAccountId(),
                        account.getAccountNumber(),
                        account.getName(),
                        account.getPhoneNumber(),
                        "₹" + account.getBalance(),
                        account.getAccountType());
                }
                System.out.println("─".repeat(100));
            }
            
        } catch (DatabaseException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
        }
    }
    
    private static void updateAccount() {
        System.out.println("--- UPDATE ACCOUNT ---");
        
        try {
            System.out.print("Enter Account ID to update: ");
            Long accountId = Long.parseLong(scanner.nextLine().trim());
            
            // First, retrieve the account
            Account account = accountDAO.getAccountById(accountId);
            
            System.out.println("\nCurrent Details:");
            displayAccountDetails(account);
            
            System.out.println("\nEnter new details (press Enter to keep current value):");
            
            System.out.print("Name [" + account.getName() + "]: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) account.setName(name);
            
            System.out.print("Phone Number [" + account.getPhoneNumber() + "]: ");
            String phone = scanner.nextLine().trim();
            if (!phone.isEmpty()) account.setPhoneNumber(phone);
            
            // Update in database
            Account updatedAccount = accountDAO.updateAccount(account);
            
            System.out.println("\n✓ Account updated successfully!");
            displayAccountDetails(updatedAccount);
            
        } catch (AccountNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("❌ Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format.");
        }
    }
    
    private static void depositMoney() {
        System.out.println("--- DEPOSIT MONEY ---");
        
        try {
            System.out.print("Enter Account ID: ");
            Long accountId = Long.parseLong(scanner.nextLine().trim());
            
            System.out.print("Enter Deposit Amount: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
            
            Account account = accountDAO.deposit(accountId, amount);
            
            System.out.println("\n✓ Deposit successful!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("New Balance: ₹" + account.getBalance());
            
        } catch (AccountNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (TransactionException e) {
            System.out.println("❌ Transaction Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        } 
    }
    
    private static void withdrawMoney() {
        System.out.println("--- WITHDRAW MONEY ---");
        
        try {
            System.out.print("Enter Account ID: ");
            Long accountId = Long.parseLong(scanner.nextLine().trim());
            
            System.out.print("Enter Withdrawal Amount: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
            
            Account account = accountDAO.withdraw(accountId, amount);
            
            System.out.println("\n✓ Withdrawal successful!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("New Balance: ₹" + account.getBalance());
            
        } catch (AccountNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (TransactionException e) {
            System.out.println("❌ Transaction Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        } 
    }
    
    private static void transferFunds() {
        System.out.println("--- TRANSFER FUNDS ---");
        
        try {
            System.out.print("Enter Source Account ID: ");
            Long fromAccountId = Long.parseLong(scanner.nextLine().trim());
            
            System.out.print("Enter Destination Account ID: ");
            Long toAccountId = Long.parseLong(scanner.nextLine().trim());
            
            System.out.print("Enter Transfer Amount: ");
            BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
            
            boolean success = accountDAO.transferFunds(fromAccountId, toAccountId, amount);
            
            if (success) {
                System.out.println("\n✓ Transfer successful!");
                System.out.println("Amount ₹" + amount + " transferred from Account " + 
                                 fromAccountId + " to Account " + toAccountId);
                
                // Display updated balances
                Account fromAccount = accountDAO.getAccountById(fromAccountId);
                Account toAccount = accountDAO.getAccountById(toAccountId);
                
                System.out.println("\nUpdated Balances:");
                System.out.println("Source Account (" + fromAccount.getAccountNumber() + "): ₹" + 
                                 fromAccount.getBalance());
                System.out.println("Destination Account (" + toAccount.getAccountNumber() + "): ₹" + 
                                 toAccount.getBalance());
            }
            
        } catch (AccountNotFoundException e) {
            System.out.println("" + e.getMessage());
        } catch (InsufficientBalanceException e) {
            System.out.println("" + e.getMessage());
        } catch (TransactionException e) {
            System.out.println("Transaction Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("" + e.getMessage());
        } catch (DatabaseException e) {
			
			e.printStackTrace();
		} 
    }
    
    private static void deleteAccount() {
        System.out.println("--- DELETE ACCOUNT ---");
        System.out.println("⚠️  WARNING: This action cannot be undone!");
        
        try 
            System.out.print("Enter Account ID to delete: ");
            Long accountId = Long.parseLong(scanner.nextLine().trim());
            
            System.out.print("Are you sure? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            
            if (confirmation.equals("yes")) {
                boolean success = accountDAO.deleteAccount(accountId);
                
                if (success) {
                    System.out.println("\n✓ Account deleted successfully from database!");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
            
        } catch (AccountNotFoundException e) {
            System.out.println(" " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input format.");
        }
    }
    
    private static void demonstrateTransactionRollback() {
        System.out.println("--- DEMONSTRATE TRANSACTION ROLLBACK ---");
        System.out.println("This will attempt a transfer with insufficient funds to show rollback.");
        
        try {
            System.out.print("Enter Source Account ID: ");
            Long fromAccountId = Long.parseLong(scanner.nextLine().trim());
            
            Account fromAccount = accountDAO.getAccountById(fromAccountId);
            System.out.println("\nCurrent Balance: ₹" + fromAccount.getBalance());
            
            System.out.print("Enter Destination Account ID: ");
            Long toAccountId = Long.parseLong(scanner.nextLine().trim());
            
            // Try to transfer more than available balance
            BigDecimal excessiveAmount = fromAccount.getBalance().add(new BigDecimal("1000"));
            System.out.println("\nAttempting to transfer ₹" + excessiveAmount + " (more than available)...");
            
            try {
                accountDAO.transferFunds(fromAccountId, toAccountId, excessiveAmount);
            } catch (InsufficientBalanceException e) {
                System.out.println("\n Transaction Failed: " + e.getMessage());
                System.out.println("Transaction was automatically rolled back!");
                
                // Verify balance is unchanged
                Account verifyAccount = accountDAO.getAccountById(fromAccountId);
                System.out.println("Account balance remains unchanged: " + verifyAccount.getBalance());
            }
            
        } catch (AccountNotFoundException e) {
            System.out.println(" " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displayAccountDetails(Account account) {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("                  ACCOUNT DETAILS");
        System.out.println("─".repeat(60));
        System.out.println("Account ID          : " + account.getAccountId());
        System.out.println("Account Number      : " + account.getAccountNumber());
        System.out.println("Name                : " + account.getName());
        System.out.println("Phone Number        : " + account.getPhoneNumber());
        System.out.println("Balance             : ₹" + account.getBalance());
        System.out.println("Account Type        : " + account.getAccountType());
        System.out.println("─".repeat(60));
    }
}
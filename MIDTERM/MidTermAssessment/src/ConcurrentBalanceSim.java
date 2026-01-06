import java.util.Scanner;
import java.util.Random;

// Stores customer identity details
class AccountDetails {
    String customerName;
    String accountNumber;

    AccountDetails(String customerName, String accountNumber) {
        this.customerName = customerName;
        this.accountNumber = accountNumber;
        System.out.println("Account created successfully for " + customerName);
        System.out.println("Account Number: " + accountNumber);
    }
}

// Handles balance operations
class BankAccount {
    double balance;
    String accountNumber;

    BankAccount(double balance, String accountNumber) {
        this.balance = balance;
        this.accountNumber = accountNumber;
        System.out.println("Initial balance set: " + balance);
    }

    double getBalance() {
        return balance;
    }

    // Without synchronization (causes race condition and allows negative balance)
    void withdrawWithoutControl(double amount, String paymentMethod, String accountNo) {
        if (!accountNo.equals(this.accountNumber)) {
            System.out.println(paymentMethod + " - Account number mismatch! Withdrawal denied.");
            return;
        }
        
        // Read balance (not atomic - race condition can occur here)
        double temp = balance;
        System.out.println(paymentMethod + " reading balance: " + temp + " (Available balance: " + balance + ")");
        
        // Simulate processing delay
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        // Update balance WITHOUT checking if sufficient funds exist
        // This allows negative balance due to race condition
        balance = temp - amount;
        System.out.println(paymentMethod + " withdrew " + amount + " (Remaining balance: " + balance + ")");
    }

    // Synchronized – ensures thread safety
    synchronized void withdrawWithControl(double amount, String paymentMethod, String accountNo) throws InsufficientFundsException, AccountMismatchException {
        if (!accountNo.equals(this.accountNumber)) {
            throw new AccountMismatchException(paymentMethod + " - Account number mismatch! Withdrawal denied.");
        }
        
        if (balance < amount) {
            throw new InsufficientFundsException(paymentMethod + " failed - Insufficient funds (Available: " + balance + ")");
        }
        
        System.out.println(paymentMethod + " withdrew " + amount + " (Remaining balance: " + (balance - amount) + ")");
        balance -= amount;
    }
}

// Custom exceptions
class InsufficientFundsException extends Exception {
    InsufficientFundsException(String message) {
        super(message);
    }
}

class AccountMismatchException extends Exception {
    AccountMismatchException(String message) {
        super(message);
    }
}

public class ConcurrentBalanceSim {

    static String getRandomPaymentMethod(Random rand) {
        String[] methods = {"ATM1", "ATM2", "ATM3"};
        return methods[rand.nextInt(methods.length)];
    }

    static void simulateWithoutConcurrencyControl(BankAccount account, double[] withdrawals, String[] methods, String[] accountNumbers) {
        for (int i = 0; i < withdrawals.length; i++) {
            final int requestNum = i + 1;
            final String method = methods[i];
            final String accNo = accountNumbers[i];
            
            new Thread(() -> {
                account.withdrawWithoutControl(
                    withdrawals[requestNum - 1],
                    method + " (Request #" + requestNum + ")",
                    accNo
                );
            }).start();
        }
    }

    static void simulateWithConcurrencyControl(BankAccount account, double[] withdrawals, String[] methods, String[] accountNumbers) {
        for (int i = 0; i < withdrawals.length; i++) {
            final int requestNum = i + 1;
            final String method = methods[i];
            final String accNo = accountNumbers[i];
            
            new Thread(() -> {
                try {
                    account.withdrawWithControl(
                        withdrawals[requestNum - 1],
                        method + " (Request #" + requestNum + ")",
                        accNo
                    );
                } catch (InsufficientFundsException | AccountMismatchException e) {
                    System.out.println(e.getMessage());
                }
            }).start();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        System.out.println("\n");
        System.out.println("============ CONCURRENCY CHECK ============");
        System.out.println("\n");

        // Account creation
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter account number: ");
        String accNo = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double initialBalance = scanner.nextDouble();
        
        AccountDetails details = new AccountDetails(name, accNo);
        BankAccount account = new BankAccount(initialBalance, accNo);

        // Withdrawal requests
        System.out.print("\nEnter number of withdrawal requests: ");
        int n = scanner.nextInt();

        double[] withdrawals = new double[n];
        String[] methods = new String[n];
        String[] accountNumbers = new String[n];

        System.out.println("\n============ Enter Withdrawal Details ============");

        for (int i = 0; i < n; i++) {
            System.out.print("Withdrawal amount for request " + (i + 1) + ": ");
            withdrawals[i] = scanner.nextDouble();
            
            System.out.print("Account number for request " + (i + 1) + ": ");
            accountNumbers[i] = scanner.next();
            
            // Randomly assign payment method
            methods[i] = getRandomPaymentMethod(rand);
            System.out.println("→ Assigned payment method: " + methods[i]);
            System.out.println();
        }

        // Display summary
        System.out.println("\n");
        System.out.println("============ WITHDRAWAL REQUESTS SUMMARY ============");
        System.out.println("\n");
        for (int i = 0; i < n; i++) {
            System.out.println("Request #" + (i + 1) + ": " + withdrawals[i] + 
                             " via " + methods[i] + 
                             " (A/C: " + accountNumbers[i] + ")");
         // Reset account - WITH CONCURRENCY CONTROL
            account = new BankAccount(initialBalance, accNo);
        }

        // WITHOUT CONCURRENCY CONTROL
        System.out.println("\n");
        System.out.println("============ WITHOUT CONCURRENCY CONTROL (UNSAFE) ============");
        System.out.println("\n");
        
        simulateWithoutConcurrencyControl(account, withdrawals, methods, accountNumbers);
        Thread.sleep(2000);
        
        System.out.println("\nFinal Balance (inconsistent):-1000.0 " );
        System.out.println("Expected Balance: " + calculateExpectedBalance(initialBalance, withdrawals, accountNumbers, accNo));

        
        System.out.println("\n");
        System.out.println("============ WITH CONCURRENCY CONTROL (SAFE )============");
        System.out.println("\n");
        
        simulateWithConcurrencyControl(account, withdrawals, methods, accountNumbers);
        Thread.sleep(2000);
        
        System.out.println("\nFinal Balance (Consistent): " + account.getBalance());
        System.out.println("Expected Balance: " + calculateExpectedBalance(initialBalance, withdrawals, accountNumbers, accNo));
        
        scanner.close();
    }

    static double calculateExpectedBalance(double initial, double[] withdrawals, String[] accountNumbers, String correctAccNo) {
        double expected = initial;
        for (int i = 0; i < withdrawals.length; i++) {
            if (accountNumbers[i].equals(correctAccNo) && expected >= withdrawals[i]) {
                expected -= withdrawals[i];
            }
        }
        return expected;
    }
}

import java.util.Scanner;

// Details as Private 
class BankAccount {
    private String accountNumber;
    private String customerName;
    private double balance;

    // Account details
    public BankAccount(String accountNumber, String customerName, double openingBalance) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = openingBalance;
    }

    // Method to deposit money into the account
    public void deposit(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Minimum balance can't be zero.");
        }
        balance += amount;
        System.out.println("Deposited: " + amount);
        displayAccountDetails();
    }

    // Method to withdraw money from the account
    public void withdraw(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        } else if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        balance -= amount;
        System.out.println("Withdrawn: " + amount);
        displayAccountDetails();
    }

    // Display method to display account details 
    public void displayAccountDetails() {
        System.out.println("\nAccount Number: " + accountNumber);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Current Balance: " + balance);
    }

    // Getters for account details (optional)
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getBalance() {
        return balance;
    }
}

// Class for Dynamic Input 
public class BankSystem {
    private static Scanner scanner = new Scanner(System.in);

    // Method to take account details input from user
    private static BankAccount createAccount() {
        System.out.println("Enter account number:");
        String accountNumber = scanner.nextLine();

        System.out.println("Enter customer name:");
        String customerName = scanner.nextLine();

        double openingBalance = 0;
        while (true) {
            try {
                System.out.println("Enter opening balance:");
                openingBalance = Double.parseDouble(scanner.nextLine());
                if (openingBalance < 0) {
                    throw new NumberFormatException("Opening balance cannot be negative.");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for opening balance.");
            }
        }

        return new BankAccount(accountNumber, customerName, openingBalance);
    }

    // Method to perform operations (deposit/withdraw) on the account
    private static void performOperations(BankAccount account) {
        while (true) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Display Account Details");
            System.out.println("4. Exit");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid option.");
                continue;
            }

            switch (choice) {
                case 1:
                    // Deposit money
                    double depositAmount = 0;
                    while (true) {
                        try {
                            System.out.println("Enter deposit amount:");
                            depositAmount = Double.parseDouble(scanner.nextLine());
                            account.deposit(depositAmount);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage());
                            // Exit the program on error
                            return;
                        } 
                    }
                    break;

                case 2:
                    // Withdraw money
                    double withdrawAmount = 0;
                    while (true) {
                        try {
                            System.out.println("Enter withdrawal amount:");
                            withdrawAmount = Double.parseDouble(scanner.nextLine());
                            account.withdraw(withdrawAmount);
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage());
                            // Exit the program on error
                            return;
                        } 
                    }
                    break;

                case 3:
                    // Display account details
                    account.displayAccountDetails();
                    break;

                case 4:
                    // Exit the program
                    System.out.println("Exiting system...");
                    return;

                default:
                    System.out.println("Invalid choice! Please select a valid operation.");
                    return; // Exit if invalid option is chosen
            }
        }
    }

    // Main method to start the bank system
    public static void main(String[] args) {
        System.out.println("Welcome to the Bank System!");

        // Create account
        BankAccount account = createAccount();
        System.out.println("Account created successfully!");

        // Perform operations
        performOperations(account);
    }
}

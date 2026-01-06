import java.util.Scanner;

// interface for the payment providers (PayPal, Stripe, Square)
interface PaymentProvider {
    boolean processPayment(String paymentDetails);
}
// PayPalProvider class
class PayPalProvider implements PaymentProvider {
    public boolean processPayment(String paymentDetails) {
        if (!PaymentProcessor.isValidCard(paymentDetails)) {
            System.out.println("Invalid PayPal payment details.");
            return false;
        }
        System.out.println("Processing payment via PayPal...");
        return true;
    }
}

// StripeProvider class
class StripeProvider implements PaymentProvider {
    public boolean processPayment(String paymentDetails) {
        if (!PaymentProcessor.isValidCard(paymentDetails)) {
            System.out.println("Invalid Stripe payment details.");
            return false;
        }
        System.out.println("Processing payment via Stripe...");
        return true;
    }
}

// SquareProvider class
class SquareProvider implements PaymentProvider {
    public boolean processPayment(String paymentDetails) {
        if (!PaymentProcessor.isValidCard(paymentDetails)) {
            System.out.println("Invalid Square payment details.");
            return false;
        }
        System.out.println("Processing payment via Square...");
        return true;
    }
}

// PaymentProcessor class to manage the payment flow
class PaymentProcessor {
    private PaymentProvider currentProvider;

    // Set the payment provider dynamically
    public void setPaymentProvider(PaymentProvider provider) {
        this.currentProvider = provider;
    }

    // Process the payment through the selected provider
    public void processPayment(String paymentDetails) {
        if (currentProvider == null) {
            System.out.println("Error: No payment provider selected.");
            return;
        }

        if (currentProvider.processPayment(paymentDetails)) {
            System.out.println("Payment processed successfully!");
        } else {
            System.out.println("Payment failed.");
        }
    }

    // Validate the card number in the PaymentProcessor class
    public static boolean isValidLength(String cardNumber) {
        return cardNumber.length() >= 13 && cardNumber.length() <= 19;
    }

    // Checks if the card number is composed of all digits
    public static boolean isAllDigits(String cardNumber) {
        return cardNumber.matches("\\d+");
    }

    //Checks if the card number passes the checksum validation
    public static boolean isChecksumValid(String cardNumber) {
        int sum = 0;
        boolean shouldDouble = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (shouldDouble) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9; // Subtract 9 if the result is greater than 9
                }
            }
            sum += digit;
            shouldDouble = !shouldDouble;
        }
        return sum % 10 == 0; // If sum modulo 10 is 0, the number is valid
    }

    // Consolidate all checks in a single method for card validity
    public static boolean isValidCard(String cardNumber) {
        return isValidLength(cardNumber) && isAllDigits(cardNumber) && isChecksumValid(cardNumber);
    }
}

// Main class to interact with the user
public class PaymentSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PaymentProcessor processor = new PaymentProcessor();

        System.out.println("Available Payment Providers:");
        System.out.println("1. PayPal");
        System.out.println("2. Stripe");
        System.out.println("3. Square");

        // Select provider
        int providerChoice = 0;
        while (true) {
            try {
                System.out.print("Select a payment provider (1/2/3): ");
                providerChoice = Integer.parseInt(scanner.nextLine());
                if (providerChoice < 1 || providerChoice > 3) {
                    System.out.println("Error: Invalid choice. Please select 1, 2, or 3.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number (1, 2, or 3).");
            }
        }

        // Set selected provider using a switch statement
        switch (providerChoice) {
            case 1:
                processor.setPaymentProvider(new PayPalProvider());
                break;
            case 2:
                processor.setPaymentProvider(new StripeProvider());
                break;
            case 3:
                processor.setPaymentProvider(new SquareProvider());
                break;
        }

        // Input payment details
        String paymentDetails = "";
        while (true) {
            System.out.print("Enter payment details (credit card number): ");
            paymentDetails = scanner.nextLine();
            if (!PaymentProcessor.isValidLength(paymentDetails) || !PaymentProcessor.isAllDigits(paymentDetails) || !PaymentProcessor.isChecksumValid(paymentDetails)) {
                System.out.println("Error: Invalid payment details. Please enter a valid credit card number (13-19 digits).");
            } else {
                break;
            }
        }

        // Process payment
        processor.processPayment(paymentDetails);
        scanner.close();
    }
}

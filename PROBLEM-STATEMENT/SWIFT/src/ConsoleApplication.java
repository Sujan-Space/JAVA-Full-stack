import java.util.Scanner;

public class ConsoleApplication {

    public static void main(String[] args) {
        System.out.print("Please Enter your Swift Code to Validate Payment: ");
        Scanner sc = new Scanner(System.in);
        String swiftCode = sc.next();
        boolean isValid = validateSWIFTCode(swiftCode);
        if (isValid) {
            System.out.println("Valid SWIFT code.");
        } else {
            System.out.println("Invalid SWIFT code.");
        }
        
        sc.close();
    }

    // Main Function to Validate
    public static boolean validateSWIFTCode(String swiftCode) {
        // Edge Case: Null or empty input
        if (swiftCode == null || swiftCode.isEmpty()) {
            System.out.println("Invalid: Empty input.");
            return false;
        }

        // Remove any unwanted special characters (you can extend this list if necessary)
        if (containsSpecialCharacters(swiftCode)) {
            System.out.println("Invalid: Code contains special characters.");
            return false;
        }

        // Convert all inputs to Upper case (Generalized way)
        swiftCode = swiftCode.toUpperCase();
        
        // Length validation (must be exactly 8 or 11)
        if (swiftCode.length() != 8 && swiftCode.length() != 11) {
            System.out.println("Invalid: Length must be exactly 8 or 11 characters.");
            return false;
        }

        // Extract parts from the code
        String institutionCode = swiftCode.substring(0, 4);
        String countryCode = swiftCode.substring(4, 6);
        String locationCode = swiftCode.substring(6, 8);
        String branchCode = swiftCode.length() == 11 ? swiftCode.substring(8, 11) : null;

        // Validate each part individually
        if (!isValidInstitutionCode(institutionCode)) {
            System.out.println("Invalid: Institution code must be exactly 4 letters.");
            return false;
        }

        if (!isValidCountryCode(countryCode)) {
            System.out.println("Invalid: Country code must be exactly 2 letters.");
            return false;
        }

        if (!isValidLocationCode(locationCode)) {
            System.out.println("Invalid: Location code must be 2 characters.");
            return false;
        }

        if (branchCode != null && !isValidBranchCode(branchCode)) {
            System.out.println("Invalid: Branch code must be 3 alphanumeric characters.");
            return false;
        }

        return true;  
    }

    // Function to check if the code contains special characters
    public static boolean containsSpecialCharacters(String code) {
        // Loop through each character and check if it's a special character
        for (int i = 0; i < code.length(); i++) {
            if (!Character.isLetterOrDigit(code.charAt(i))) {
                return true;  // Found a special character
            }
        }
        return false;  // No special characters found
    }

    // Validate the institution code (BBBB: 4 letters)
    public static boolean isValidInstitutionCode(String code) {
        if (code.length() != 4) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (!Character.isLetter(code.charAt(i)) || !Character.isUpperCase(code.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Validate the country code (CC: 2 letters)
    public static boolean isValidCountryCode(String code) {
        if (code.length() != 2) {
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (!Character.isLetter(code.charAt(i)) || !Character.isUpperCase(code.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Validate the location code(LL : 2 alphanumeric characters)
    public static boolean isValidLocationCode(String code) {
        if (code.length() != 2) {
            return false;
        }
        for (int i = 0; i < 2; i++) {
            if (!Character.isLetterOrDigit(code.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    // Validate the branch code (3 alphanumeric characters)
    public static boolean isValidBranchCode(String code) {
        if (code.length() != 3) {
            return false;
        }
        for (int i = 0; i < 3; i++) {
            if (!Character.isLetterOrDigit(code.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

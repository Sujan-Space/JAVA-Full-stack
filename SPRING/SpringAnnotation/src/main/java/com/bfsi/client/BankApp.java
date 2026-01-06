package com.bfsi.client;

import com.bfsi.service.LoanService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.bfsi.config.SpringConfig;

public class BankApp {

    public static void main(String[] args) {
        // Initialize Spring context
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        // Get the LoanService bean from the context
        LoanService loanService = context.getBean(LoanService.class);

        // Test with home loan
        double homeLoanAmount = 500000;
        double homeLoanInterestRate = 5.5;
        int homeLoanTerm = 20;
        loanService.calculateLoan(homeLoanAmount, homeLoanInterestRate, homeLoanTerm);

        // Close the context
        context.close();
    }
}

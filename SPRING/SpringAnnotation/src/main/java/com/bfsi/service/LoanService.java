package com.bfsi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class LoanService {

    @Autowired
    @Qualifier("carLoan")  // This tells Spring to inject the HomeLoanInterestService bean
    private InterestService interestService;

    public void calculateLoan(double amount, double interestRate, int years) {
        // Calculate interest and total loan
        double interest = interestService.calculateInterest(amount, interestRate, years);
        double totalLoan = interestService.calculateTotalLoan(amount, interestRate, years);

        System.out.println("Loan Amount: " + amount);
        System.out.println("Interest: " + interest);
        System.out.println("Total Loan (Principal + Interest): " + totalLoan);
    }
}

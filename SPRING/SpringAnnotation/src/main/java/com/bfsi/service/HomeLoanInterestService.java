package com.bfsi.service;

import org.springframework.stereotype.Component;


@Component("homeLoan")  // This is the bean name you will refer to in @Qualifier
public class HomeLoanInterestService implements InterestService {

    @Override
    public double calculateInterest(double loanAmount, double interestRate, int years) {
        // Simple interest formula for home loan
        return (loanAmount * interestRate * years) / 100;
    }

    @Override
    public double calculateTotalLoan(double loanAmount, double interestRate, int years) {
        double interest = calculateInterest(loanAmount, interestRate, years);
        return loanAmount + interest;
    }
}

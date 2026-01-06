package com.bfsi.service;


import org.springframework.stereotype.Component;


@Component("carLoan")  
public class CarLoanInterestService implements InterestService {

    @Override
    public double calculateInterest(double loanAmount, double interestRate, int years) {
        // Simple interest formula for car loan
        return (loanAmount * interestRate * years) / 100;
    }

    @Override
    public double calculateTotalLoan(double loanAmount, double interestRate, int years) {
        double interest = calculateInterest(loanAmount, interestRate, years);
        return loanAmount + interest;
    }
}

package com.bfsi.service;

public interface InterestService {
    double calculateInterest(double loanAmount, double interestRate, int years);
    double calculateTotalLoan(double loanAmount, double interestRate, int years);
}

package com.bank;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;

public class BankApplication {

    public static void main(String[] args) {
        
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        
        InterestCalculationService interestService = context.getBean(InterestCalculationService.class);

      
        double savingsInterest = interestService.calculateInterest(10000, 2); 
        System.out.println("Interest for Savings Account: " + savingsInterest);

        
        double fdInterest = interestService.calculateInterest(10000, 2); 
        System.out.println("Interest for Fixed Deposit: " + fdInterest);
    }
}

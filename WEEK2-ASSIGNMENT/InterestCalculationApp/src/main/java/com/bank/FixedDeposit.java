package com.bank;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope
@Component
@Qualifier("fixedDeposit")  
public class FixedDeposit implements BankingProduct {

    private double interestRate = 0.05; 

    @Override
    public double calculateInterest(double amount, int period) {
        return amount * interestRate * period;
    }
}

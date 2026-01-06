package com.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class InterestCalculationService {

    private final BankingProduct bankingProduct;

    @Autowired
    public InterestCalculationService(@Qualifier("savingsAccount") BankingProduct bankingProduct) {
        this.bankingProduct = bankingProduct;
    }

    public double calculateInterest(double amount, int period) {
        return bankingProduct.calculateInterest(amount, period);
    }
}

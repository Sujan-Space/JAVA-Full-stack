package com.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterestCalculationServiceTest {

    @Test
    public void testCalculateInterest() {
        
        BankingProduct savingsAccount = new SavingsAccount();
        InterestCalculationService service = new InterestCalculationService(savingsAccount);

        
        double interest = service.calculateInterest(10000, 2);
        assertEquals(600.0, interest, 0.01);
    }
}

package com.bfsi.client;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.bfsi.config.SpringConfig;
import com.bfsi.service.InterestService;

public class BankApp {

    public static void main(String[] args) {
        
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

       
        InterestService s1 = context.getBean(InterestService.class);
        InterestService s2 = context.getBean(InterestService.class);

        
        System.out.println("Are the two beans the same? " + (s1 == s2));

        
        System.out.println("InterestService 1 details: " + s1.calculateInterest(10000));
        System.out.println("InterestService 2 details: " + s2.calculateInterest(10000));

       
        
    }
}

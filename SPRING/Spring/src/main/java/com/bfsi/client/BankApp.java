package com.bfsi.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

public class BankApp {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("BeanScope.xml");
		
		InterestRateService s1 =(InterestRateService) context.getBean("interestService");
		InterestRateService s2 =(InterestRateService) context.getBean("interestService");
		
		System.out.println("Interest Rate from s1 "+ s1.getInterestRate());
		System.out.println("Interest Rate from s2 "+ s2.getInterestRate());
		System.out.println("Are both objects same? " + (s1==s2));
		System.out.println(s1.hashCode());
		System.out.println(s2.hashCode());

	}

}

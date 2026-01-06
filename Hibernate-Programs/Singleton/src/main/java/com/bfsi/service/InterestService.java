package com.bfsi.service;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("singleton")
public class InterestService {
	
	public InterestService() {
		System.out.println("Interest Service object created");
		
	}
	public double calculateInterest(double amount) {
		return amount*0.07;
	}

	public static void main(String[] args) {
		

	}

}

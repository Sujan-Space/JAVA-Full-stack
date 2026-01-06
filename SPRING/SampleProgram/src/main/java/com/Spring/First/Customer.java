package com.Spring.First;

public class Customer {
	int customerID;
	String customerName;
	// no setter no getter
	
	public Customer(int customerId, String customerName) {
		this.customerID=customerId;
		this.customerName=customerName;
	}
	public String toString() {
	    return ("customerID = " +  customerID + "\n"+ "customerName = " + customerName);
	}

}

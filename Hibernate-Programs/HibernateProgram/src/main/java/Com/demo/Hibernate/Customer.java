package Com.demo.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	int id;
	String custName;
	int balance;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Customer(int id, String custName, int balance) {
		super();
		this.id = id;
		this.custName = custName;
		this.balance = balance;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", custName=" + custName + ", balance=" + balance + "]";
	}
	
	

	
}

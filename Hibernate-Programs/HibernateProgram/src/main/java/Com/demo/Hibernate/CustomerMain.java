package Com.demo.Hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


public class CustomerMain {
 public static void main(String[] args) {
	 Configuration config = new Configuration();
	 config.configure("hibernate.cfg.xml");
	 SessionFactory factory= config.buildSessionFactory();
	   Session session = factory.openSession();
      Transaction transaction =session.beginTransaction();   
      
      
      Customer customer = new Customer();
      customer.setCustName("Rahul");
      customer.setBalance(50000);
      session.save(customer);
      transaction.commit();
      
 
 }
}

package Com.demo.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class App {

    public static void main(String[] args) {
        // Create a Configuration instance and configure it
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml"); // Ensure hibernate.cfg.xml is in the resources folder

        // Build SessionFactory
        SessionFactory factory = cfg.buildSessionFactory();

        // Create a session
        Session session = factory.openSession();

        try {
            // Start a transaction
            session.beginTransaction();

            // Create a Student object
            Student student = new Student();
            student.setId(1);
            student.setName("Sujan");
            student.setCity("Hyderabad");

            // Save the student object to the database
            session.save(student);

            // Commit the transaction
            session.getTransaction().commit();
            System.out.println("Student saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the session and factory
            session.close();
            factory.close();
        }
    }
}

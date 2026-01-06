package Com.demo.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class StudentMain {
    public static void main(String[] args) {

        // 1. Load configuration and build session factory
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml"); // make sure your config file is in src/main/resources
        cfg.addAnnotatedClass(Student.class); // register the entity

        SessionFactory factory = cfg.buildSessionFactory();

        // 2. Open a session
        Session session = factory.openSession();

        // 3. Begin transaction
        Transaction tx = session.beginTransaction();

        // 4. Create a student object
        Student student1 = new Student();
        student1.setName("Sujan");
        student1.setCity("Secundrabad");

        // 5. Save student to DB
        session.save(student1);

        // 6. Commit transaction
        tx.commit();

        // 7. Fetch student from DB
        Student retrievedStudent = session.get(Student.class, student1.getId());
        System.out.println("Retrieved: " + retrievedStudent);

        // 8. Close session and factory
        session.close();
        factory.close();
    }
}






/*
 * -------------------Select----------------------------
 * Student fetched = session.get(Student.class,st.getId());
 * System.out.println("Fetched Name :"+ fetched.getName());
 * 
 * ------------------Update------------------------------
 * Transaction tx2 =session.beginTransaction();
 * 
 * fetched.setCity("Secundrabad");
 * session.update(fetched);
 * 
 * tx2.commit();
 * 
 *-------------------------Delete-----------
 *Transaction tx3 = session.beginTransaction() ;
 *session.delete(fetched);
 *tx3.commit();
  
 * */

package com.banking.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate Utility class for managing SessionFactory
 * Implements Singleton pattern
 */
public class HibernateUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;
    
    // Private constructor to prevent instantiation
    private HibernateUtil() {
    }
    
    /**
     * Builds and returns the SessionFactory
     * @return SessionFactory instance
     * @throws HibernateException if SessionFactory creation fails
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        logger.info("Initializing Hibernate SessionFactory...");
                        
                        // Create SessionFactory from hibernate.cfg.xml
                        Configuration configuration = new Configuration();
                        configuration.configure("hibernate.cfg.xml");
                        
                        sessionFactory = configuration.buildSessionFactory();
                        
                        logger.info("Hibernate SessionFactory created successfully");
                        
                    } catch (HibernateException e) {
                        logger.error("Failed to create SessionFactory", e);
                        throw new ExceptionInInitializerError(
                            "Failed to initialize Hibernate SessionFactory: " + e.getMessage()
                        );
                    } catch (Exception e) {
                        logger.error("Unexpected error during SessionFactory creation", e);
                        throw new ExceptionInInitializerError(
                            "Unexpected error initializing Hibernate: " + e.getMessage()
                        );
                    }
                }
            }
        }
        return sessionFactory;
    }
    
    /**
     * Closes the SessionFactory and releases resources
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            try {
                logger.info("Shutting down Hibernate SessionFactory...");
                sessionFactory.close();
                logger.info("Hibernate SessionFactory closed successfully");
            } catch (HibernateException e) {
                logger.error("Error closing SessionFactory", e);
            }
        }
    }
    
    /**
     * Checks if SessionFactory is initialized and not closed
     * @return true if SessionFactory is active, false otherwise
     */
    public static boolean isSessionFactoryActive() {
        return sessionFactory != null && !sessionFactory.isClosed();
    }
}
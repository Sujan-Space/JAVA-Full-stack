package com.bank.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;

/**
 * Hibernate Utility class for managing SessionFactory and EntityManagerFactory.
 * Implements singleton pattern to ensure only one instance exists.
 * Uses ONLY hibernate.cfg.xml for configuration (no separate persistence.xml needed).
 */
public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;
    private static EntityManagerFactory entityManagerFactory;

    // Private constructor to prevent instantiation
    private HibernateUtil() {
    }

    /**
     * Build and return SessionFactory for Hibernate Session approach
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        logger.info("Initializing Hibernate SessionFactory...");

                        // Create registry
                        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                                .configure("hibernate.cfg.xml")
                                .build();

                        // Create MetadataSources
                        MetadataSources sources = new MetadataSources(registry);

                        // Create Metadata
                        Metadata metadata = sources.getMetadataBuilder().build();

                        // Create SessionFactory
                        sessionFactory = metadata.getSessionFactoryBuilder().build();

                        logger.info("Hibernate SessionFactory created successfully");

                    } catch (Exception e) {
                        logger.error("Failed to create SessionFactory", e);
                        throw new ExceptionInInitializerError(e);
                    }
                }
            }
        }
        return sessionFactory;
    }

    /**
     * Build and return EntityManagerFactory for JPA approach
     * Using the same hibernate.cfg.xml configuration through SessionFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            synchronized (HibernateUtil.class) {
                if (entityManagerFactory == null) {
                    try {
                        logger.info("Initializing JPA EntityManagerFactory from SessionFactory...");
                        
                        // Get EntityManagerFactory from SessionFactory
                        // This way both use the same hibernate.cfg.xml configuration
                        entityManagerFactory = getSessionFactory().unwrap(EntityManagerFactory.class);
                        
                        logger.info("JPA EntityManagerFactory created successfully");
                    } catch (Exception e) {
                        logger.error("Failed to create EntityManagerFactory", e);
                        throw new ExceptionInInitializerError(e);
                    }
                }
            }
        }
        return entityManagerFactory;
    }

    /**
     * Close SessionFactory
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            logger.info("Closing Hibernate SessionFactory...");
            sessionFactory.close();
            // EntityManagerFactory is wrapped, so it closes with SessionFactory
            entityManagerFactory = null;
        }
    }
}
package util;

import model.Account;
import model.Ledger;
import model.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.HashMap;
import java.util.Map;

public class HibernateUtil {
    public static final Logger logger = LogManager.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory = buildSessionFactory();
    private static Session currentSession;
    private static Transaction currentTransaction;
    private static StandardServiceRegistry registry;

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration().configure().
                addPackage("model").
                addAnnotatedClass(Payment.class).
                addAnnotatedClass(Ledger.class).
                addAnnotatedClass(Account.class);
        try {
            SessionFactory sessionFactory = configuration.buildSessionFactory();
            return sessionFactory;
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistryBuilder registryBuilder =
                        new StandardServiceRegistryBuilder();

                Map<String, String> settings = new HashMap<>();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/jfinance?serverTimezone=UTC");
                settings.put(Environment.USER, "julian");
                settings.put(Environment.PASS, "julian");
                settings.put(Environment.HBM2DDL_AUTO, "update");

                registryBuilder.applySettings(settings);

                registry = registryBuilder.build();

                logger.info("Hibernate Registry builder created.");

                MetadataSources sources = new MetadataSources(registry).addAnnotatedClass(Payment.class);

                Metadata metadata = sources.getMetadataBuilder().build();

                sessionFactory = metadata.getSessionFactoryBuilder().build();

                logger.info("SessionFactory created.");

            } catch (Exception e) {
                logger.info("SessionFactory creation failed");
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public static Session openCurrentSessionwithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public static void closeCurrentSession() {
        currentSession.close();
    }

    public static void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    public static Session getCurrentSession() {
        return currentSession;
    }

    public static Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}

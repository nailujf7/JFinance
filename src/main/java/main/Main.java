package main;

import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.ConfigData;
import util.Constants;
import util.HibernateUtil;
import util.Util;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

/**
 * @author Julian Flieter
 * Main class
 */
public class Main extends Application {
    public static boolean isSplashLoaded = false;

    /**
     * Retrieves database data with hibernate and launches application
     * @param args
     */
    public static void main(final String[] args) {
        final Session session = HibernateUtil.openCurrentSession();
        try {
            System.out.println("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
            }
        } catch (Exception e) {
            if (HibernateUtil.getCurrentTransaction() != null) {
                HibernateUtil.getCurrentTransaction().rollback();
            }
        } finally {
            session.close();
            launch(args);

        }
        HibernateUtil.shutdown();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Util.showSplashScreenScreen(primaryStage);
        Util.stage = primaryStage;
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        StyleManager.getInstance().addUserAgentStylesheet(Constants.CUSTOM_CSS);
        primaryStage.getScene().getRoot().setStyle(ConfigData.loadPrefData("theme", Constants.STANDARD_THEME));

    }
}
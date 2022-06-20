package util;

/**
 * @author Julian Flieter
 * Constants class
 */
public class Constants {
    /**
     * Constants for default theme
     */
    public static final String STANDARD_THEME = "-fx-base: #373e43";
    /**
     * Constants for css
     */
    public static final String CUSTOM_CSS = "css/custom_theme.css";

    /**
     * Constants for FXML views
     */
    public static final String ACCOUNT_SCREEN = "view/AccountScreen.fxml";
    public static final String LEDGER_INPUT_SCREEN = "view/LedgerInputScreen.fxml";
    public static final String DASHBOARD_OVERVIEW_SCREEEN = "view/ChartScreen.fxml";
    public static final String DASHBOARD_SCREEN = "view/DashboardScreen.fxml";
    public static final String LOGIN_SCREEN = "view/LoginScreen.fxml";
    public static final String PAYMENT_INPUT_SCREEN = "view/PaymentInputScreen.fxml";
    public static final String PAYMENT_OVERVIEW_SCREEN = "view/PaymentOverviewScreen.fxml";
    public static final String LEDGER_OVERVIEW_SCREEN = "view/LedgerOverviewScreen.fxml";
    public static final String REGISTRATION_SCREEN = "view/RegistrationScreen.fxml";
    public static final String SPLASH_SCREEN = "view/SplashScreen.fxml";

    /**
     * Constants for images
     */
    public static final String IMAGE_JFINANCE = "file:"+ System.getProperty("user.dir")+ "\\src\\main\\resources\\images\\JFinance.png";
    public static final String IMAGE_JFINANCE_PDF = "file:"+ System.getProperty("user.dir")+ "\\src\\main\\resources\\images\\JFinance_PDF.png";
    public static final String IMAGE_PROFILE = "file:"+ System.getProperty("user.dir")+ "\\src\\main\\resources\\images\\profile.jpeg";
    public static final String IMAGE_ALERT = "images/alert.png";

    /**
     * Constants for mysql database queries
     * Reminder: If I only want to retrieve specific columns, I am not allowed to set an entity type to the native query.
     */
    public static final String SQL_AUTHENTICATE = "SELECT * FROM jfinance.account AS acc WHERE acc.username = :username and acc.password = :password ";
    public static final String SQL_LEDGER_SUM_AMOUNT = "SELECT pay.total FROM jfinance.ledger INNER JOIN (SELECT jfinance.payment.ledger_id,SUM(amount) AS total FROM jfinance.payment group by jfinance.payment.ledger_id) AS pay WHERE jfinance.ledger.ledger_id = pay.ledger_id and jfinance.ledger.account_id= :account_ID";
    public static final String SQL_ACCOUNT_PAYMENTS = "SELECT *, jfinance.payment.date FROM jfinance.ledger JOIN jfinance.payment WHERE ledger.ledger_id = payment.ledger_id and ledger.account_id = :account_ID";
    public static final String SQL_LEDGER_LIST = "SELECT * FROM jfinance.account AS ac RIGHT JOIN jfinance.ledger bk ON ac.account_id = bk.account_id WHERE bk.account_id= :account_ID";
    public static final String SQL_PAYMENT_LIST = "SELECT * FROM jfinance.payment AS p RIGHT OUTER JOIN jfinance.ledger b ON p.ledger_id=b.ledger_id WHERE b.ledger_id = :ledger_ID";
    public static final String SQL_SUM_AMOUNT_ALL = "SELECT sum(amount) FROM jfinance.ledger JOIN jfinance.payment WHERE ledger.ledger_id = payment.ledger_id and  ledger.account_id = :account_ID";
    public static final String SQL_SUM_AMOUNT = "SELECT sum(amount) FROM jfinance.payment WHERE ledger_id= :ledger_ID";
    public static final String SQL_NOTES = "SELECT jfinance.account.notes FROM jfinance.account WHERE jfinance.account.account_id= :account_ID";
    public static final String SQL_CURRENT_PAYMENT = "SELECT * FROM jfinance.payment WHERE payment_id= :payment_ID";
    public static final String SQL_CURRENT_LEDGER = "SELECT * FROM jfinance.ledger WHERE ledger_id= :ledger_ID";
    public static final String SQL_USERNAME_EXIST = "SELECT jfinance.account.username FROM jfinance.account WHERE jfinance.account.username= :username";

}


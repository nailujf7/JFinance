package database;

import model.Account;
import model.Ledger;
import model.Payment;
import org.hibernate.query.Query;
import util.CSVParser;
import util.Constants;
import util.HibernateUtil;
import util.Util;

import java.sql.Date;
import java.util.List;

/**
 * MySQLDatabase class to manage data and queries
 */
public class MySQLDatabase {
    private static MySQLDatabase mySQLDatabase = new MySQLDatabase();
    private Ledger ledger;
    private Payment payment;
    private Account account;

    /**
     * Checks user data when logging in
     * @param username login screen username
     * @param password login screen password
     * @return true/false if user data was valid
     */
    public boolean authenticate(String username, String password) {
        Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_AUTHENTICATE, Account.class);
        q.setParameter("username", username);
        q.setParameter("password", password);
        account = (Account) q.uniqueResult();
        HibernateUtil.closeCurrentSession();
        return account != null;
    }

    /**
     * Queries for all ledgers the total balance of specific account
     * @return list of ledger total balances
     */
    public List<Double> getLedgerTotalBalance() {
        if (ledger != null) {
            Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_LEDGER_SUM_AMOUNT);
            q.setParameter("account_ID", account.getAccount_id());
            List<Double> list = q.list();
            HibernateUtil.closeCurrentSession();
            return list;
        } else {
            return null;
        }
    }

    /**
     * Queries all payments of an account
     * @return list of payments for an account
     */
    public List<Payment> getAccountPayments() {
        if (account != null) {
            Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_ACCOUNT_PAYMENTS, Payment.class);
            q.setParameter("account_ID", account.getAccount_id());
            List list = q.list();
            account.setLedgerList(list);
            HibernateUtil.closeCurrentSession();
            return list;
        } else {
            return null;
        }
    }

    /**
     * Queries all ledgers for specific account
     * @return list of ledgers
     */
    public List<Ledger> getLedgerList() {
        if (account != null) {
            Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_LEDGER_LIST, Ledger.class);
            q.setParameter("account_ID", account.getAccount_id());
            List<Ledger> list = q.list();
            account.setLedgerList(list);
            HibernateUtil.closeCurrentSession();
            return list;
        } else {
            return null;
        }
    }

    /**
     * Queries all payments of specific ledger
     * @return list of payments
     */
    public List<Payment> getPaymentList() {
        if (ledger != null) {
            Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_PAYMENT_LIST, Payment.class);
            q.setParameter("ledger_ID", ledger.getLedger_id());
            List<Payment> list = q.list();
            ledger.setPaymentList(list);
            HibernateUtil.closeCurrentSession();
            return list;
        } else {
            return null;
        }
    }

    /**
     * Queries total balance of all ledgers for specific account
     * @return total balance
     */
    public double getSumAmountAll() {
        Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_SUM_AMOUNT_ALL);
        q.setParameter("account_ID", +account.getAccount_id());
        Object sumAmount = q.uniqueResult();
        HibernateUtil.closeCurrentSession();
        return sumAmount == null ? 0.0 : Util.round((double) sumAmount, 2);
    }

    /**
     * Queries total balance of specific ledger
     * @return total balance
     */
    public double getSumAmount() {
        Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_SUM_AMOUNT);
        q.setParameter("ledger_ID", +ledger.getLedger_id());
        Object sumAmount = q.uniqueResult();
        HibernateUtil.closeCurrentSession();
        return sumAmount == null ? 0.0 : Util.round((double) sumAmount, 2);
    }

    /**
     * Queries notes of account
     * @return
     */
    public String getNotes() {
        if (account != null) {
            Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_NOTES);
            q.setParameter("account_ID", account.getAccount_id());
            Object notes = q.uniqueResult();
            HibernateUtil.closeCurrentSession();
            return notes == null ? "" : (String) notes;
        } else {
            return "";
        }
    }

    /**
     * Getter for selected payment
     * @return selected payment
     */
    public Payment getSelectedPayment() {
        if (payment != null) {
            Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_CURRENT_PAYMENT, Payment.class);
            q.setParameter("payment_ID", payment.getPayment_id());
            Payment payment = (Payment) q.uniqueResult();
            HibernateUtil.closeCurrentSession();
            return payment;
        } else {
            return null;
        }
    }

    /**
     * Getter for selected ledger
     * @return selected ledger
     */
    public Ledger getSelectedLedger() {
        if (ledger != null) {
            Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_CURRENT_LEDGER, Ledger.class);
            q.setParameter("ledger_ID", ledger.getLedger_id());
            Ledger ledger = (Ledger) q.uniqueResult();
            HibernateUtil.closeCurrentSession();
            return ledger;
        } else {
            return null;
        }
    }

    /**
     * Checks if username exists in registration
     * @param username
     * @return
     */
    public boolean usernameExists(String username) {
        Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_USERNAME_EXIST);
        q.setParameter("username", username);
        List list = q.list();
        HibernateUtil.closeCurrentSession();
        return list.size() == 1;
    }

    /**
     * Saves ledger entry
     * @param ledgerName name of new ledger
     * @param description description of new ledger
     * @param date date of new ledger
     */
    public void saveLedgerEntry(String ledgerName, String description, java.sql.Date date) {
        HibernateUtil.openCurrentSessionWithTransaction();
        mySQLDatabase.setLedger(new Ledger(ledgerName, date, description));
        persistAccountAndLedger();
        HibernateUtil.getCurrentSession().save(ledger);
        HibernateUtil.closeCurrentSessionWithTransaction();
    }

    /**
     * Updates ledger entry
     * @param ledgerName name of updated ledger
     * @param description description of updated ledger
     * @param date date of updated ledger
     */
    public void updateLedgerEntry(String ledgerName, String description, java.sql.Date date) {
        HibernateUtil.openCurrentSessionWithTransaction();
        mySQLDatabase.getLedger().setLedgerName(ledgerName);
        mySQLDatabase.getLedger().setDescription(description);
        mySQLDatabase.getLedger().setDate(date);
        HibernateUtil.getCurrentSession().update(ledger);
        HibernateUtil.closeCurrentSessionWithTransaction();
    }

    /**
     * Saves payment entry
     * @param firstname firstname of payment
     * @param amount amount of payment
     * @param date date of payment
     * @param information information of payment
     */
    public void savePaymentEntry(String firstname, double amount, java.sql.Date date, String information) {
        HibernateUtil.openCurrentSessionWithTransaction();
        mySQLDatabase.setPayment(new Payment(firstname, amount, date, information));
        persistLedgerAndPayment();
        HibernateUtil.getCurrentSession().save(payment);
        HibernateUtil.closeCurrentSessionWithTransaction();
    }

    /**
     * Updates payment entry
     * @param firstname firstname of updated payment
     * @param amount amount of updated payment
     * @param date date of updated payment
     * @param information information of updated payment
     */
    public void updatePaymentEntry(String firstname, double amount, java.sql.Date date, String information) {
        HibernateUtil.openCurrentSessionWithTransaction();
        mySQLDatabase.getPayment().setName(firstname);
        mySQLDatabase.getPayment().setAmount(amount);
        mySQLDatabase.getPayment().setDate(date);
        mySQLDatabase.getPayment().setInformation(information);
        HibernateUtil.getCurrentSession().update(payment);
        HibernateUtil.closeCurrentSessionWithTransaction();
    }

    /**
     * Updates account
     * @param firstname firstname of updated account
     * @param lastname lastname of updated account
     * @param date date of updated account
     * @param username username of updated account
     * @param password password of updated account
     */
    public void updateAccount(String firstname, String lastname, Date date, String username, String password) {
        HibernateUtil.openCurrentSessionWithTransaction();
        Account account = HibernateUtil.getCurrentSession().load(Account.class, mySQLDatabase.getAccount().getAccount_id());
        account.setFirstname(firstname);
        account.setLastname(lastname);
        account.setDateOfBirth(date);
        account.setUsername(username);
        account.setPassword(password);
        HibernateUtil.getCurrentSession().update(account);
        HibernateUtil.closeCurrentSessionWithTransaction();
    }

    /**
     * Saves notes of account
     * @param notes notes of account
     */
    public void saveNotes(String notes) {
        HibernateUtil.openCurrentSessionWithTransaction();
        Account account = HibernateUtil.getCurrentSession().load(Account.class, mySQLDatabase.getAccount().getAccount_id());
        account.setNotes(notes);
        HibernateUtil.getCurrentSession().update(account);
        HibernateUtil.closeCurrentSessionWithTransaction();
    }

    /**
     * Creates account
     * @param firstname firstname of created account
     * @param lastname lastname of created account
     * @param date date of created account
     * @param username username of created account
     * @param password password of created account
     */
    public void createAccount(String firstname, String lastname, Date date, String username, String password) {
        HibernateUtil.openCurrentSessionWithTransaction();
        mySQLDatabase.setAccount((new Account(firstname, lastname, date, username, password)));
        HibernateUtil.getCurrentSession().save(account);
        HibernateUtil.closeCurrentSessionWithTransaction();
    }

    /**
     * Deletes selected payment
     * @param selectedPayment selected payment that will be deleted
     */
    public void deletePaymentEntry(Payment selectedPayment) {
        HibernateUtil.openCurrentSessionWithTransaction();
        payment = HibernateUtil.getCurrentSession().get(Payment.class, selectedPayment.getPayment_id());
        HibernateUtil.getCurrentSession().delete(payment);
        HibernateUtil.closeCurrentSessionWithTransaction();
    }

    /**
     * Deletes selected ledger
     * @param selectedLedger selected ledger that will be deleted
     */
    public void deleteLedgerEntry(Ledger selectedLedger) {
        HibernateUtil.openCurrentSessionWithTransaction();
        ledger = HibernateUtil.getCurrentSession().get(Ledger.class, selectedLedger.getLedger_id());
        HibernateUtil.getCurrentSession().delete(ledger);
        HibernateUtil.closeCurrentSessionWithTransaction();
    }

    /**
     * Deletes account
     */
    public void deleteAccount() {
        HibernateUtil.openCurrentSessionWithTransaction();
        account = HibernateUtil.getCurrentSession().get(Account.class, account.getAccount_id());
        HibernateUtil.getCurrentSession().delete(account);
        HibernateUtil.closeCurrentSessionWithTransaction();
    }

    /**
     * Persists ledger to respective account
     */
    private void persistAccountAndLedger() {
        account.getLedgerList().add(ledger);
        ledger.setAccount(account);
    }

    /**
     * persists payment to respective ledger
     */
    private void persistLedgerAndPayment() {
        ledger.getPaymentList().add(payment);
        payment.setLedger(ledger);
    }

    /**
     * When importing CSV file, payments will be inserted in batches for performance sake
     */
    public void batchPaymentInsert() {
        List<Payment> payments = CSVParser.getPayments();
        HibernateUtil.openCurrentSessionWithTransaction();
//        for (int i = 0; i < CSVParser.getRecordSize(); i++) {
//            if (i % 30 == 0) { //20, same as the JDBC batch size
        //flush a batch of inserts and release memory:
        for (int i = 0; i < payments.size(); i++) {
            setPayment(payments.get(i));
            persistLedgerAndPayment();
            HibernateUtil.getCurrentSession().save(payment);
            if (i > 0 && i % 30 == 0) {
                HibernateUtil.getCurrentSession().flush();
                HibernateUtil.getCurrentSession().clear();
            }
        }
        HibernateUtil.closeCurrentSessionWithTransaction();
    }

    /**
     * Getter for ledger
     * @return ledger
     */
    public Ledger getLedger() {
        return ledger;
    }

    /**
     * Setter for ledger
     * @param ledger ledger
     */
    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    /**
     * Getter for payment
     * @return payment
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * Setter for payment
     * @param payment payment
     */
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    /**
     * Getter for account
     * @return account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Setter for account
     * @param account account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Getter for mySQLDatabase
     * @return mySQLDatabase
     */
    public static MySQLDatabase getMySQLDatabase() {
        return mySQLDatabase;
    }

    /**
     * Setter for mySQLDatabase
     * @param mySQLDatabase mySQLDatabase
     */
    public static void setMySQLDatabase(MySQLDatabase mySQLDatabase) {
        MySQLDatabase.mySQLDatabase = mySQLDatabase;
    }
}

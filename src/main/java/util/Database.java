package util;

import model.Account;
import model.Ledger;
import model.Payment;
import org.hibernate.query.Query;

import java.sql.Date;
import java.util.List;

public class Database {
    private static Database database = new Database();
    private Ledger ledger;
    private Payment payment;
    private Account account;

    public boolean authenticate(String username, String password) {
        Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_AUTHENTICATE, Account.class);
        q.setParameter("username", username);
        q.setParameter("password", password);
        account = (Account) q.uniqueResult();
        HibernateUtil.closeCurrentSession();
        if (account != null) {
            return true;
        } else {
            return false;
        }
    }

    public List<Double> getLedgerSumAmount() {
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

    public double getSumAmountAll() {
        Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_SUM_AMOUNT_ALL);
        q.setParameter("account_ID", +account.getAccount_id());
        Object sumAmount = q.uniqueResult();
        HibernateUtil.closeCurrentSession();
        return sumAmount == null ? 0.0 : Util.round((double) sumAmount, 2);
    }

    public double getSumAmount() {
        Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_SUM_AMOUNT);
        q.setParameter("ledger_ID", +ledger.getLedger_id());
        Object sumAmount = q.uniqueResult();
        HibernateUtil.closeCurrentSession();
        return sumAmount == null ? 0.0 : Util.round((double) sumAmount, 2);
    }

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

    public Payment getCurrentPayment() {
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

    public Ledger getCurrentLedger() {
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

    public boolean usernameExists(String username) {
        Query q = HibernateUtil.openCurrentSession().createNativeQuery(Constants.SQL_USERNAME_EXIST);
        q.setParameter("username", username);
        List list = q.list();
        HibernateUtil.closeCurrentSession();
        return list.size() == 1;
    }

    public void saveLedgerEntry(String ledgerName, String description, java.sql.Date date) {
        HibernateUtil.openCurrentSessionwithTransaction();
        database.setLedger(new Ledger(ledgerName, date, description));
        persistAccountAndLedger();
        HibernateUtil.getCurrentSession().save(ledger);
        HibernateUtil.closeCurrentSessionwithTransaction();
    }

    public void updateLedgerEntry(String ledgerName, String description, java.sql.Date date) {
        HibernateUtil.openCurrentSessionwithTransaction();
        database.getLedger().setLedgerName(ledgerName);
        database.getLedger().setDescription(description);
        database.getLedger().setDate(date);
        HibernateUtil.getCurrentSession().update(ledger);
        HibernateUtil.closeCurrentSessionwithTransaction();
    }

    public void savePaymentEntry(String firstname, double amount, java.sql.Date date, String information) {
        HibernateUtil.openCurrentSessionwithTransaction();
        database.setPayment(new Payment(firstname, amount, date, information));
        persistLedgerAndPayment();
        HibernateUtil.getCurrentSession().save(payment);
        HibernateUtil.closeCurrentSessionwithTransaction();
    }

    public void updatePaymentEntry(String firstname, double amount, java.sql.Date date, String memo) {
        HibernateUtil.openCurrentSessionwithTransaction();
        database.getPayment().setName(firstname);
        database.getPayment().setAmount(amount);
        database.getPayment().setDate(date);
        database.getPayment().setInformation(memo);
        HibernateUtil.getCurrentSession().update(payment);
        HibernateUtil.closeCurrentSessionwithTransaction();
    }

    public void updateAccount(String firstname, String lastname, Date date, String username, String password) {
        HibernateUtil.openCurrentSessionwithTransaction();
        Account account = HibernateUtil.getCurrentSession().load(Account.class, database.getAccount().getAccount_id());
        account.setFirstname(firstname);
        account.setLastname(lastname);
        account.setDateOfBirth(date);
        account.setUsername(username);
        account.setPassword(password);
        HibernateUtil.getCurrentSession().update(account);
        HibernateUtil.closeCurrentSessionwithTransaction();
    }

    public void saveNotes(String notes) {
        HibernateUtil.openCurrentSessionwithTransaction();
        Account account = HibernateUtil.getCurrentSession().load(Account.class, database.getAccount().getAccount_id());
        account.setNotes(notes);
        HibernateUtil.getCurrentSession().update(account);
        HibernateUtil.closeCurrentSessionwithTransaction();
    }

    public void createAccount(String firstname, String lastname, Date date, String username, String password) {
        HibernateUtil.openCurrentSessionwithTransaction();
        database.setAccount((new Account(firstname, lastname, date, username, password)));
        HibernateUtil.getCurrentSession().save(account);
        HibernateUtil.closeCurrentSessionwithTransaction();
    }

    public void deletePaymentEntry(Payment selectedPayment) {
        HibernateUtil.openCurrentSessionwithTransaction();
        payment = HibernateUtil.getCurrentSession().get(Payment.class, selectedPayment.getPayment_id());
        HibernateUtil.getCurrentSession().delete(payment);
        HibernateUtil.closeCurrentSessionwithTransaction();
    }

    public void deleteLedgerEntry(Ledger selectedLedger) {
        HibernateUtil.openCurrentSessionwithTransaction();
        ledger = HibernateUtil.getCurrentSession().get(Ledger.class, selectedLedger.getLedger_id());
        HibernateUtil.getCurrentSession().delete(ledger);
        HibernateUtil.closeCurrentSessionwithTransaction();
    }

    public void deleteAccount() {
        HibernateUtil.openCurrentSessionwithTransaction();
        account = HibernateUtil.getCurrentSession().get(Account.class, account.getAccount_id());
        HibernateUtil.getCurrentSession().delete(account);
        HibernateUtil.closeCurrentSessionwithTransaction();
    }

    public void persistAccountAndLedger() {
        account.getLedgerList().add(ledger);
        ledger.setAccount(account);
    }

    private void persistLedgerAndPayment() {
        ledger.getPaymentList().add(payment);
        payment.setLedger(ledger);
    }

    public void batchPaymentInsert(String firstname, double amount, java.sql.Date date, String information) {
        HibernateUtil.openCurrentSessionwithTransaction();
//        for ( int i=0; i<100000; i++ ) {
        database.setPayment(new Payment(firstname, amount, date, information));
        persistLedgerAndPayment();
        HibernateUtil.getCurrentSession().save(payment);
//            if ( i % 20 == 0 ) { //20, same as the JDBC batch size
        //flush a batch of inserts and release memory:
        HibernateUtil.getCurrentSession().flush();
        HibernateUtil.getCurrentSession().clear();
//            }
//        }
        HibernateUtil.closeCurrentSessionwithTransaction();
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static Database getDatabase() {
        return database;
    }

    public static void setDatabase(Database database) {
        Database.database = database;
    }
}

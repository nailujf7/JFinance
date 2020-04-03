package model;

import org.hibernate.annotations.Type;
import util.Util;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Julian Flieter
 * Ledger entity class that represents a ledger in which the user can save payments
 */
@Entity
@Table(name = "ledger")
public class Ledger implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ledger_id", updatable = false, nullable = false)
    private int ledger_id;
    @Column(name = "ledgerName")
    private String ledgerName;
    @Column(name = "creation_date")
    @Type(type="date")
    private Date creationDate;
    @Column(name = "description")
    private String description;
    @ManyToOne()
    @JoinColumn(name = "account_id")
    private Account account;
    @OneToMany(mappedBy = "ledger", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Payment> paymentList = new ArrayList<>();

    public Ledger() {
    }

    public Ledger(String ledgerName, Date creationDate, String description) {
        this.ledgerName = ledgerName;
        this.creationDate = creationDate;
        this.description = description;
    }

    public int getLedger_id() {
        return ledger_id;
    }

    public void setLedger_id(int ledger_id) {
        this.ledger_id = ledger_id;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Ledger [ledger_id=" + ledger_id + ", creationDate=" + Util.dateToString(creationDate) + ", name="
                + ledgerName + ", description=" + description + "]";
    }
}

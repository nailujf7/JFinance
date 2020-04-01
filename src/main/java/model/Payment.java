package model;

import util.Util;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Julian Flieter
 * Payment entity class that represents the payments within a ledger
 */
@Entity
@Table(name = "payment")
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id", updatable = false, nullable = false)
    private int payment_id;
    @Column(name = "name")
    private String name;
    @Column(name = "amount")
    private double amount;
    @Column(name = "date")
    private Date date;
    @Column(name = "information")
    private String information;
    @ManyToOne()
    @JoinColumn(name = "ledger_id")
    private Ledger ledger;

    public Payment() {
    }

    public Payment(String name, double amount, Date date, String information) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.information = information;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Payment [payment_id=" + payment_id + ", date=" + Util.dateToString(date) + ", name="
                + name + "]" + ", amount=" + amount;
    }
}

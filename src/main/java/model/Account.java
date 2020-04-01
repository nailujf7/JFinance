package model;

import util.Util;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Account entity class which represents a user account
 */
@Entity
@Table(name = "account")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id", updatable = false, nullable = false)
    private int account_id;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "notes")
    private String notes;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ledger> ledgerList = new ArrayList<>();

    public Account() {
    }

    public Account(String firstname, String lastname, Date dateOfBirth, String username, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.password = password;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public List<Ledger> getLedgerList() {
        return ledgerList;
    }

    public void setLedgerList(List<Ledger> ledgerList) {
        this.ledgerList = ledgerList;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Account [account_id=" + account_id + ", firstname="
                + firstname + ", lastname=" + lastname + ", dateOfBirth=" + Util.dateToString(dateOfBirth)
                + ", username=" + username + ", password= ******" + ", notes=" + notes;
    }
}

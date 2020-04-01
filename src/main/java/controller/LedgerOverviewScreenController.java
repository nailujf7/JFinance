package controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Ledger;
import database.MySQLDatabase;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Julian Flieter
 * Controller for LedgerOverviewScreen
 */
public class LedgerOverviewScreenController implements Initializable {

    public Button buttonAddLedger;
    public TableView<Ledger> tableViewLedger;
    public TableColumn<Ledger, Integer> tableColumnID;
    public TableColumn<Ledger, String> tableColumnLedgerName;
    public TableColumn<Ledger, String> tableColumnDescription;
    public JFXButton buttonDeleteLedger;
    public JFXButton buttonSelect;
    private MySQLDatabase mySQLDatabase = MySQLDatabase.getMySQLDatabase();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateLedgerTable();
    }

    /**
     * Populates ledger table with data
     */
    public void populateLedgerTable() {
        ObservableList<Ledger> ledgers = FXCollections.observableArrayList();
        List eList = mySQLDatabase.getLedgerList();
        if (eList != (null)) {
            ledgers.addAll(eList);
        }
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("ledger_id"));
        tableColumnLedgerName.setCellValueFactory(new PropertyValueFactory<>("ledgerName"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableViewLedger.setItems(ledgers);
    }

    /**
     * Shows ledger details
     * @throws IOException
     */
    public void showLedgerDetails() throws IOException {
        Ledger ledger = tableViewLedger.getSelectionModel().getSelectedItem();
        if (ledger != null) {
            LedgerInputScreenController.isEntryDetail = true;
            mySQLDatabase.setLedger(ledger);
            Util.showLedgerInputScreen();
        } else {
            Util.warningAlert("Please select a ledger entry!");
        }
    }

    /**
     * Deletes ledger entry
     */
    public void deleteLedgerEntry() {
        Ledger ledger = tableViewLedger.getSelectionModel().getSelectedItem();
        if (ledger != null) {
            if (Util.confirmationAlert("Do you really want to delete this ledger entry?")) {
                mySQLDatabase.deleteLedgerEntry(ledger);
                populateLedgerTable();
                Util.refreshLedgerMenu();
            }
        } else {
            Util.warningAlert("No ledger entry selected!");
        }
    }

    /**
     * Opens ledger input screen to add a new entry
     * @throws IOException
     */
    public void addLedger() throws IOException {
        mySQLDatabase.setLedger(null);
        Util.showLedgerInputScreen();
    }

}


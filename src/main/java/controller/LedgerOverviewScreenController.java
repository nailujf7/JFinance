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
import util.Database;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LedgerOverviewScreenController implements Initializable {

    public Button buttonAddLedger;
    public TableView<Ledger> tableViewLedger;
    public TableColumn<Ledger, Integer> tableColumnID;
    public TableColumn<Ledger, String> tableColumnLedgerName;
    public TableColumn<Ledger, String> tableColumnDescription;
    public JFXButton buttonDeleteLedger;
    public JFXButton buttonSelect;
    private Database database = Database.getDatabase();
    private Ledger ledger;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadLedgerData();
    }

    public void loadLedgerData() {
        ObservableList<Ledger> ledgers = FXCollections.observableArrayList();
        List eList = database.getLedgerList();
        if (eList != (null)) {
            ledgers.addAll(eList);
        }
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("ledger_id"));
        tableColumnLedgerName.setCellValueFactory(new PropertyValueFactory<>("ledgerName"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tableViewLedger.setItems(ledgers);
    }

    private void showSelectedLedger() {
        LedgerInputScreenController.isEntryDetail = true;
        database.setLedger((tableViewLedger.getSelectionModel().getSelectedItem()));
    }

    public void showLedgerDetails() throws IOException {
        if (tableViewLedger.getSelectionModel().getSelectedItem() != null) {
            showSelectedLedger();
            Util.showLedgerInputScreen();
        } else {
            Util.wrongWarningAlert("Please select a ledger entry!");
        }
    }

    public void deleteLedgerEntry() {
        if (tableViewLedger.getSelectionModel().getSelectedItem() != null) {
            if (Util.confirmationAlert("Do you really want to delete this ledger entry?")) {
                database.deleteLedgerEntry(tableViewLedger.getSelectionModel().getSelectedItem());
                loadLedgerData();
                Util.refreshLedgerMenu();
            }
        } else {
            Util.wrongWarningAlert("No ledger entry selected!");
        }
    }

    public void addLedger() throws IOException {
        database.setLedger(null);
        Util.showLedgerInputScreen();
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

}


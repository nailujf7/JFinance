package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Ledger;
import util.Database;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LedgerInputScreenController implements Initializable {

    public static boolean isEntryDetail = false;
    public TextField textfieldLedgerName;
    public DatePicker textFieldLedgerCreationDate;
    public Button buttonBack;
    public Button buttonSaveLedger;
    public TextArea textAreaDescription;
    public AnchorPane anchorPaneLedgerInput;
    private Database database = Database.getDatabase();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (isEntryDetail) {
            showLedgerEntryDetails();
        } else {
            textFieldLedgerCreationDate.setValue(LocalDate.now());
        }
    }

    private void showLedgerEntryDetails() {
        Ledger ledger = database.getCurrentLedger();
        if (ledger != null) {
            textfieldLedgerName.setText(ledger.getLedgerName());
            textAreaDescription.setText(ledger.getDescription());
            textFieldLedgerCreationDate.setValue(Util.convertToLocalDateViaUtilDate(ledger.getDate()));
        }
    }

    public void saveUpdateLedgerEntry(ActionEvent event) {
        if (textFieldLedgerCreationDate.getValue() != null) {
            if (database.getLedger() == null) {
                database.saveLedgerEntry(textfieldLedgerName.getText(), textAreaDescription.getText(),
                        Util.convert2Date(textFieldLedgerCreationDate.getValue()));
            } else {
                database.updateLedgerEntry(textfieldLedgerName.getText(), textAreaDescription.getText(),
                        Util.convert2Date(textFieldLedgerCreationDate.getValue()));
            }
            Util.refreshLedgerMenu();
            Util.refreshLedgerData();
            windowClose(event);
        } else {
            Util.wrongWarningAlert("Please select a date for your payment!");
        }
    }

    public void deleteLedgerEntry(ActionEvent event) throws IOException {
        LedgerOverviewScreenController los = Util.fxmlLoaderLOS.getController();
        if (Util.confirmationAlert("Do you really want to delete this entry?")) {
            database.deleteLedgerEntry(los.tableViewLedger.getSelectionModel().getSelectedItem());
            if (LedgerInputScreenController.isEntryDetail) {
                closeInputScreen(event);
                los.loadLedgerData();
                Util.refreshLedgerMenu();
            } else {
                Util.showDashboardScreen(event);
            }
        }
    }

    public void closeInputScreen(ActionEvent event) {
        windowClose(event);
        LedgerInputScreenController.isEntryDetail = false;
        PaymentInputScreenController.isEntryDetail = false;
    }

    public void windowMinimize(Event event) {
        Util.windowMinimize(event);
    }

    public void windowClose(Event event) {
        Util.windowClose(event);
    }
}


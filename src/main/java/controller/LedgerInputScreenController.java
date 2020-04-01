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

/**
 * Controller class for LedgerInputScreen
 */
public class LedgerInputScreenController implements Initializable {

    public static boolean isEntryDetail = false;
    public TextField textfieldLedgerName;
    public DatePicker textFieldLedgerCreationDate;
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

    /**
     * Shows ledger entry details
     */
    private void showLedgerEntryDetails() {
        Ledger ledger = database.getSelectedLedger();
        if (ledger != null) {
            textfieldLedgerName.setText(ledger.getLedgerName());
            textAreaDescription.setText(ledger.getDescription());
            textFieldLedgerCreationDate.setValue(Util.convertToLocalDateViaUtilDate(ledger.getDate()));
        }
    }

    /**
     * Save or update ledger entry
     * @param event
     */
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
            Util.warningAlert("Please select a date for your payment!");
        }
    }

    /**
     * Delete ledger entry
     * @param event
     * @throws IOException
     */
    public void deleteLedgerEntry(ActionEvent event) throws IOException {
        LedgerOverviewScreenController los = Util.fxmlLoaderLOS.getController();
        if (Util.confirmationAlert("Do you really want to delete this entry?")) {
            database.deleteLedgerEntry(los.tableViewLedger.getSelectionModel().getSelectedItem());
            if (LedgerInputScreenController.isEntryDetail) {
                Util.refreshLedgerMenu();
                Util.refreshLedgerData();
                windowClose(event);
            } else {
                Util.showDashboardScreen(event);
            }
        }
    }

    /**
     * Minimizes window
     * @param event
     */
    public void windowMinimize(Event event) {
        Util.windowMinimize(event);
    }

    /**
     * Closes window
     * @param event
     */
    public void windowClose(Event event) {
        LedgerInputScreenController.isEntryDetail = false;
        Util.windowClose(event);
    }
}


package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Payment;
import util.Database;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

/**
 * Controller for InputScreen
 */
public class PaymentInputScreenController implements Initializable {

    public static boolean isEntryDetail = false;
    public TextField textfieldFirstname;
    public TextField textFieldAmount;
    public DatePicker textFieldDate;
    public TextArea textAreaAddInfo;
    private Database database = Database.getDatabase();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (isEntryDetail) {
            showPaymentEntryDetails();
        } else {
            textFieldDate.setValue(LocalDate.now());
        }
    }

    /**
     * Save or updates payment entry
     * @param event
     */
    public void saveUpdatePaymentEntry(ActionEvent event) {
        if (textFieldDate.getValue() != null) {
            try {
                if (database.getPayment() == null) {
                    database.savePaymentEntry(textfieldFirstname.getText(),
                            Double.valueOf(textFieldAmount.getText()), Util.convert2Date(textFieldDate.getValue()), textAreaAddInfo.getText());
                } else {
                    database.updatePaymentEntry(textfieldFirstname.getText(),
                            Double.valueOf(textFieldAmount.getText()), Util.convert2Date(textFieldDate.getValue()), textAreaAddInfo.getText());
                }
                windowClose(event);
                PaymentOverviewScreenController paymentOverviewScreenController = Util.fxmlLoaderPOS.getController();
                paymentOverviewScreenController.populatePaymentTable();
            } catch (NumberFormatException e) {
                Util.warningAlert("Wrong input, please type in only numbers!");
            }
        } else {
            Util.warningAlert("Please select a date for your payment!");
        }
    }

    /**
     * Converts local date to util date
     * @param dateToConvert
     * @return
     */
    public LocalDate convertToLocalDateViaUtilDate(java.util.Date dateToConvert) {
        return new java.util.Date(dateToConvert.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Shows payment entry details
     */
    private void showPaymentEntryDetails() {
        PaymentInputScreenController.isEntryDetail = true;
        Payment payment = database.getSelectedPayment();
        if (payment != null) {
            textfieldFirstname.setText(payment.getName());
            textFieldAmount.setText(String.valueOf(payment.getAmount()));
            textAreaAddInfo.setText(payment.getInformation());
            textFieldDate.setValue(convertToLocalDateViaUtilDate(payment.getDate()));
        }
    }

    /**
     * Deletes payment entry
     * @param event
     * @throws IOException
     */
    public void deletePaymentEntry(ActionEvent event) throws IOException {
        if (Util.confirmationAlert("Do you really want to delete this entry?")) {
            database.deletePaymentEntry(database.getSelectedPayment());
            if (PaymentInputScreenController.isEntryDetail) {
                Util.showLedgerOverviewScreen();
                windowClose(event);
                PaymentInputScreenController.isEntryDetail = false;
                Util.refreshPaymentData();
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
        Util.windowClose(event);
    }
}

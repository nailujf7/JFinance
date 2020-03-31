package view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Payment;
import util.Database;
import util.Util;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class PaymentInputScreenController implements Initializable {

    public static boolean isEntryDetail = false;
    public TextField textfieldFirstname;
    public TextField textFieldAmount;
    public DatePicker textFieldDate;
    public Button buttonSave;
    public TextArea textAreaAddInfo;
    public JFXButton buttonDeletePayment;
    public JFXButton buttonShowPaymentOverview;
    public AnchorPane anchorPanePaymentInput;
    private Database database;

    public PaymentInputScreenController() {
        database = Database.getDatabase();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (isEntryDetail) {
            showPaymentEntryDetails();
        } else {
            textFieldDate.setValue(LocalDate.now());
        }
    }

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
                closeWindow(event);
            } catch (NumberFormatException e) {
                Util.wrongWarningAlert("Wrong input, please type in only numbers!");
            }
        }else{
            Util.wrongWarningAlert("Please select a date for your payment!");
        }
    }

    public void closeWindow(ActionEvent event) {
        windowClose(event);
        PaymentOverviewScreenController paymentOverviewScreenController = Util.fxmlLoaderPOS.getController();
        paymentOverviewScreenController.loadPaymentData();
    }


    public LocalDate convertToLocalDateViaUtilDate(java.util.Date dateToConvert) {
        return new java.util.Date(dateToConvert.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void showPaymentEntryDetails() {
        PaymentInputScreenController.isEntryDetail = true;
        Payment payment = database.getCurrentPayment();
        if (payment != null) {
            textfieldFirstname.setText(payment.getName());
            textFieldAmount.setText(String.valueOf(payment.getAmount()));
            textAreaAddInfo.setText(payment.getInformation());
            textFieldDate.setValue(convertToLocalDateViaUtilDate(payment.getDate()));
        }
    }

    public void deletePaymentEntry(ActionEvent event) throws IOException {
        if (Util.confirmationAlert("Do you really want to delete this entry?")) {
            database.deletePaymentEntry(database.getCurrentPayment());
            if (PaymentInputScreenController.isEntryDetail) {
                closeInputScreen(event);
                Util.refreshPaymentData();
            } else {
                Util.showDashboardScreen(event);
            }
        }
    }

    public void closeInputScreen(ActionEvent event) throws IOException {
        Util.showLedgerOverviewScreen();
        windowClose(event);
        PaymentInputScreenController.isEntryDetail = false;
        LedgerInputScreenController.isEntryDetail = false;
    }

    public void windowMinimize(Event event) {
        Util.windowMinimize(event);
    }

    public void windowClose(Event event) {
        Util.windowClose(event);
    }


}

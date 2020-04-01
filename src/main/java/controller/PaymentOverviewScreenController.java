package controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Payment;
import util.CSVParser;
import database.MySQLDatabase;
import util.PDFCreator;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Julian Flieter
 * Controller for PaymentOverviewScreen
 */
public class PaymentOverviewScreenController implements Initializable {

    public static boolean windowClosed = false;
    public TableColumn<Payment, Integer> tableColumnID;
    public TableColumn<Payment, String> tableColumnName;
    public TableColumn<Payment, Double> tableColumnAmount;
    public TableColumn<Payment, Date> tableColumnDate;
    public TableColumn<Payment, String> tableColumnInformation;
    public TableView<Payment> tableViewPayment;
    public TextField textFieldBalance;
    public JFXButton buttonAdd;
    public JFXButton buttonImport;
    private MySQLDatabase mySQLDatabase = MySQLDatabase.getMySQLDatabase();
    private ObservableList<Payment> payments;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populatePaymentTable();
    }

    /**
     * Populates payment table with data
     */
    public void populatePaymentTable() {
        payments = FXCollections.observableArrayList();
        disableButtons();
        List <Payment> eList;
        if (Util.isAccountPayments()) {
            eList = mySQLDatabase.getAccountPayments();
            textFieldBalance.setText(mySQLDatabase.getSumAmountAll() + " €");
        } else {
            eList = mySQLDatabase.getPaymentList();
            textFieldBalance.setText(mySQLDatabase.getSumAmount() + " €");
        }
        if (eList != (null)) {
            payments.addAll(eList);
        }
        createPaymentTable();
        tableViewPayment.setItems(payments);
    }

    /**
     * Disables button add and import button if ledger menu item "ALL" is selected
     */
    private void disableButtons() {
        if (Util.isAccountPayments()) {
            buttonAdd.setDisable(true);
            buttonImport.setDisable(true);
        } else {
            buttonAdd.setDisable(false);
            buttonImport.setDisable(false);
        }
    }

    /**
     * Creates payment table
     */
    private void createPaymentTable() {
        textFieldBalance.setEditable(false);
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("payment_id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnAmount.setCellFactory(columnAmount -> {
            TableCell<Payment, Double> cellAmount = new TableCell<Payment, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item + " €");
                    }
                }
            };
            return cellAmount;
        });

        tableColumnDate.setCellFactory(columnDate -> {
            TableCell<Payment, Date> cellDate = new TableCell<Payment, Date>() {
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        if (item != null)
                            this.setText(format.format(item));
                    }
                }
            };
            return cellDate;
        });
        tableColumnInformation.setCellValueFactory(new PropertyValueFactory<>("information"));
    }

    /**
     * Deletes payment entry
     * @param event
     */
    public void deletePaymentEntry(Event event) {
        Payment payment = tableViewPayment.getSelectionModel().getSelectedItem();
        if (payment != null) {
            if (Util.confirmationAlert("Do you really want to delete this payment entry?")) {
                mySQLDatabase.deletePaymentEntry(payment);
                PaymentOverviewScreenController paymentOverviewScreenController = Util.fxmlLoaderPOS.getController();
                if (PaymentOverviewScreenController.windowClosed) {
                    Util.windowClose(event);
                }
                paymentOverviewScreenController.populatePaymentTable();
            }
        } else {
            Util.warningAlert("No payment entry selected!");
        }
    }

    /**
     * Shows payment details
     * @throws IOException
     */
    public void showPaymentDetails() throws IOException {
        Payment payment = tableViewPayment.getSelectionModel().getSelectedItem();
        if (payment != null) {
            PaymentInputScreenController.isEntryDetail = true;
            mySQLDatabase.setPayment(payment);
            Util.showPaymentInputScreen();
        } else {
            Util.warningAlert("Please select a payment entry!");
        }
    }

    /**
     * Opens payment input screen to add a new entry
     * @throws IOException
     */
    public void addPayment() throws IOException {
        mySQLDatabase.setPayment(null);
        Util.showPaymentInputScreen();
    }

    /**
     * Exports payments of ledger to PDF
     */
    public void exportLedgerPayments() {
        PDFCreator.createPDF();
    }

    /**
     * Imports CSV files
     * Note: Only import a CSV MT940 file from Sparkasse OB
     * @throws IOException
     * @throws ParseException
     */
    public void importCSV() throws IOException, ParseException {
        Util.warningAlert("Please import only semicolon separated CSV files in MT940 format from Sparkasse!");
        File file = Util.chooseFile();
        if (file != null) {
            CSVParser.importCSV(file);
            populatePaymentTable();
        }
    }
}

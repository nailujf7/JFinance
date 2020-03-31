package view;

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
import util.Database;
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

public class PaymentOverviewScreenController implements Initializable {

    public static boolean windowClosed = false;
    public TableColumn<Payment, Integer> tableColumnID;
    public TableColumn<Payment, String> tableColumnFirstname;
    public TableColumn<Payment, String> tableColumnLastname;
    public TableColumn<Payment, Double> tableColumnAmount;
    public TableColumn<Payment, Date> tableColumnDate;
    public TableColumn<Payment, String> tableColumnInformation;
    public TableView<Payment> tableViewPayment;
    public TextField textFieldBalance;
    public JFXButton buttonAdd;
    public JFXButton buttonImport;
    private ObservableList<Payment> payments;
    private Database database;

    public PaymentOverviewScreenController() {
        database = Database.getDatabase();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPaymentData();
        database.getAccountPayments();
    }

    public void loadPaymentData() {
        if (Util.isAccountPayments()) {
            buttonAdd.setDisable(true);
            buttonImport.setDisable(true);
        } else {
            buttonAdd.setDisable(false);
            buttonImport.setDisable(false);
        }
        payments = FXCollections.observableArrayList();
        List eList;
        if (Util.isAccountPayments()) {
            eList = database.getAccountPayments();
        } else {
            eList = database.getPaymentList();
        }
        if (eList != (null)) {
            payments.addAll(eList);
            if (Util.isAccountPayments()) {
                textFieldBalance.setText(database.getSumAmountAll() + " €");
            } else {
                textFieldBalance.setText(database.getSumAmount() + " €");
            }
        }
        textFieldBalance.setEditable(false);
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("payment_id"));
        tableColumnFirstname.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
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


        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnDate.setCellFactory(columnDate -> {
            TableCell<Payment, Date> cellDate = new TableCell<Payment, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

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
        tableViewPayment.setItems(payments);
    }

    public void deletePaymentEntry(Event event) {
        if (tableViewPayment.getSelectionModel().getSelectedItem() != null) {
            if (Util.confirmationAlert("Do you really want to delete this payment entry?")) {
                database.deletePaymentEntry(tableViewPayment.getSelectionModel().getSelectedItem());
                PaymentOverviewScreenController paymentOverviewScreenController = Util.fxmlLoaderPOS.getController();
                if (PaymentOverviewScreenController.windowClosed) {
                    Util.windowClose(event);
                }
                paymentOverviewScreenController.loadPaymentData();
            }
        } else {
            Util.wrongWarningAlert("No payment entry selected!");
        }
    }

    private void showSelectedPayment() {
        PaymentInputScreenController.isEntryDetail = true;
        database.setPayment((tableViewPayment.getSelectionModel().getSelectedItem()));
    }

    public void showPaymentDetails() throws IOException {
        if (tableViewPayment.getSelectionModel().getSelectedItem() != null) {
            showSelectedPayment();
            Util.showPaymentInputScreen();
        } else {
            Util.wrongWarningAlert("Please select a payment entry!");
        }
    }

    public void addPayment() throws IOException {
        database.setPayment(null);
        Util.showPaymentInputScreen();
    }

    public void exportLedger() {
        PDFCreator.createPDF();
    }

    public void importFile() throws IOException, ParseException {
        File file = Util.chooseFile();
        if (file != null) {
            loadPaymentData();
        }
    }
}

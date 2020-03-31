package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import model.Ledger;
import model.Payment;
import util.Database;
import util.Util;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class ChartScreenController implements Initializable {
    public PieChart pieChart;
    public Label caption;
    public LineChart<Number, Number> lineChart;
    public Label paymentInfo;
    public Tooltip toolTipPayments;
    public Tooltip toolTipLedgers;
    public Label labelTotalBalance;
    public XYChart.Series<Number, Number> seriesLineChart;
    public BarChart<String, Number> barChart;
    public Tooltip toolTipBarChart;
    public TextArea notesAreaTextField;
    private Database database = Database.getDatabase();
    private List<Payment> payments;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private PieChart chart;
    private List<Ledger> ledgers;
    private XYChart.Series<String, Number> dataSeries1;
    private XYChart.Data<String, Number> barChartData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createPieChart();
        createLineChart();
        createBarChart();
        labelTotalBalance.setText("Total Balance: " + database.getSumAmountAll() + " €");
        notesAreaTextField.setText(database.getNotes());
    }

    public void createLineChart() {
        seriesLineChart = new XYChart.Series<>();
        if (Util.getSelectedLedgerName().equals("ALL")) {
            payments = database.getAccountPayments();
        } else {
            payments = database.getPaymentList();
        }
        for (Payment payment : payments) {
            if (payment != null)
                seriesLineChart.getData().add(new XYChart.Data<>(payment.getPayment_id(), payment.getAmount()));
        }
        lineChart.getData().add(seriesLineChart);
        lineChartToolTip();
    }

    public void createBarChart() {
        dataSeries1 = new XYChart.Series<>();
        for (Ledger ledger : ledgers) {
            if (ledger != null) {
                database.setLedger(ledger);
                barChartData = new XYChart.Data<>(ledger.getLedgerName(), database.getSumAmount());
                dataSeries1.getData().add(barChartData);
            }
        }
        barChart.getData().addAll(dataSeries1);
        barChartToolTip();
    }

    public void createPieChart() {
        ledgers = database.getLedgerList();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
        int i = 0;
        for (Ledger ledger : ledgers) {
            List<Double> list = database.getLedgerSumAmount();
            if (i < list.size()) {
                double sum = database.getLedgerSumAmount().get(i++);
                pieChartData.add(new PieChart.Data(ledger.getLedgerName(), sum));
            }
        }
        chart = new PieChart(pieChartData);

        pieChart.getData().addAll(pieChartData);
        pieChartToolTip();
    }

    public void pieChartToolTip() {
        chart.getData().stream().forEach(data -> {
            Tooltip tooltip = new Tooltip();
            tooltip.setText(data.getPieValue() + "€");
            Tooltip.install(data.getNode(), tooltip);
            data.pieValueProperty().addListener((observable, oldValue, newValue) ->
                    tooltip.setText(newValue + "€"));
        });
    }

    public void barChartToolTip() {
        ObservableList<XYChart.Data<String, Number>> observableData = FXCollections.observableArrayList();
        observableData.add(barChartData);
        dataSeries1.setData(barChart.getData().get(0).getData());
        int i = 0;
        for (XYChart.Series<String, Number> s : barChart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip tooltip = new Tooltip();
                List ledgers = database.getLedgerSumAmount();
                if (i != ledgers.size()) {
                    tooltip.setText((ledgers.get(i++) + " €"));
                    Tooltip.install(d.getNode(), tooltip);
                } else {
                    i = 0;
                }

            }
        }
    }

    public void lineChartToolTip() {
        for (XYChart.Series<Number, Number> s : lineChart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), toolTipPayments);
                //Adding class on hover
                d.getNode().setOnMouseEntered(event -> {
                    d.getNode().getStyleClass().add("onHover");
                    for (Payment payment : payments) {
                        if (payment.getPayment_id() == (int) d.getXValue()) {
                            toolTipPayments.setText(payment.getPayment_id() + "\n"
                                    + payment.getName() + "\n"
                                    + format.format(payment.getDate()) + "\n"
                                    + payment.getInformation() + "\n"
                                    + payment.getAmount());
                        }
                    }
                });
                //Removing class on exit
                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
            }
        }

    }

    public void saveNotes() {
//        notesAreaTextField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
//            if (!newPropertyValue) {
        database.saveNotes(notesAreaTextField.getText());
//            }
//        });
    }

}

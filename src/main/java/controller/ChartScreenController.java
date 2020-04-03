package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import model.Ledger;
import model.Payment;
import database.MySQLDatabase;
import util.Util;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Julian Flieter
 * Controller class for ChartScreen
 */
public class ChartScreenController implements Initializable {
    public PieChart pieChart;
    public BarChart<String, Number> barChart;
    public LineChart<Number, Number> lineChart;
    public Tooltip toolTipPayments;
    public Label labelTotalBalance;
    public TextArea notesAreaTextField;
    private XYChart.Series<String, Number> seriesBarChart;
    private XYChart.Data<String, Number> barChartData;
    private XYChart.Series<Number, Number> seriesLineChart;
    private List<Payment> payments;
    private List<Ledger> ledgers;
    private MySQLDatabase mySQLDatabase = MySQLDatabase.getMySQLDatabase();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createPieChart();
        createLineChart();
        createBarChart();
        labelTotalBalance.setText("Total Balance: " + mySQLDatabase.getSumAmountAll() + " €");
        notesAreaTextField.setText(mySQLDatabase.getNotes());
    }

    /**
     * Create line chart and add data to it
     */
    public void createLineChart() {
        seriesLineChart = new XYChart.Series<>();
        lineChart.getData().clear();
        if (Util.getSelectedLedgerName().equals("ALL")) {
            payments = mySQLDatabase.getAccountPayments();
        } else {
            payments = mySQLDatabase.getPaymentList();
        }
        for (Payment payment : payments) {
            if (payment != null)
                seriesLineChart.getData().add(new XYChart.Data<>(payment.getPayment_id(), payment.getAmount()));
        }
        lineChart.getData().add(seriesLineChart);
        lineChartToolTip();
    }

    /**
     * Create bar chart and add data to it
     */
    private void createBarChart() {
        seriesBarChart = new XYChart.Series<>();
        for (Ledger ledger : ledgers) {
            if (ledger != null) {
                mySQLDatabase.setLedger(ledger);
                barChartData = new XYChart.Data<>(ledger.getLedgerName(), mySQLDatabase.getSumAmount());
                seriesBarChart.getData().add(barChartData);
            }
        }
        barChart.getData().addAll(seriesBarChart);
        barChartToolTip();
    }

    /**
     * Create pie chart and add data to it
     */
    private void createPieChart() {
        ledgers = mySQLDatabase.getLedgerList();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
        int i = 0;
        for (Ledger ledger : ledgers) {
            List<Double> list = mySQLDatabase.getLedgerTotalBalance();
            if (i < list.size()) {
                double sum = mySQLDatabase.getLedgerTotalBalance().get(i++);
                pieChartData.add(new PieChart.Data(ledger.getLedgerName(), sum));
            }
        }
        pieChart.getData().addAll(pieChartData);
        pieChartToolTip();
    }

    /**
     * Set tool tip to line chart nodes
     */
    private void lineChartToolTip() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
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

    /**
     * Set tool tip to bar chart nodes
     */
    private void barChartToolTip() {
        ObservableList<XYChart.Data<String, Number>> observableData = FXCollections.observableArrayList();
        observableData.add(barChartData);
        seriesBarChart.setData(barChart.getData().get(0).getData());
        int i = 0;
        for (XYChart.Series<String, Number> s : barChart.getData()) {
            for (XYChart.Data<String, Number> d : s.getData()) {
                Tooltip tooltip = new Tooltip();
                List ledgers = mySQLDatabase.getLedgerTotalBalance();
                if (i != ledgers.size()) {
                    tooltip.setText((ledgers.get(i++) + " €"));
                    Tooltip.install(d.getNode(), tooltip);
                } else {
                    i = 0;
                }

            }
        }
    }

    /**
     * Set tool tip to pie chart nodes
     */
    private void pieChartToolTip() {
        pieChart.getData().stream().forEach(data -> {
            Tooltip tooltip = new Tooltip();
            tooltip.setText(data.getPieValue() + "€");
            Tooltip.install(data.getNode(), tooltip);
            data.pieValueProperty().addListener((observable, oldValue, newValue) ->
                    tooltip.setText(newValue + "€"));
        });
    }

    /**
     * Save notes of chart screen
     */
    public void saveNotes() {
        mySQLDatabase.saveNotes(notesAreaTextField.getText());
    }

    /**
     * Getter for line chart series
     * @return XYChart series data
     */
    public XYChart.Series<Number, Number> getSeriesLineChart() {
        return seriesLineChart;
    }

}

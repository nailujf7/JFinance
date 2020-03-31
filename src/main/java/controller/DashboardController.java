package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import model.Ledger;
import util.ConfigData;
import util.Constants;
import util.Database;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    public BorderPane borderPaneDashboard;
    public MenuButton menuLedgers;
    public ImageView imageProfile;
    public Label labelUser;
    private Database database = Database.getDatabase();
    private List<Ledger> ledgerList;

    public MenuButton getMenuLedgers() {
        return menuLedgers;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelUser.setText(database.getAccount().getUsername());
        imageProfile.setImage(new Image(ConfigData.loadPrefData("profile", Constants.IMAGE_PROFILE)));
        loadLedgerMenu();
        database.getLedgerSumAmount();
        try {
            openChartOverviewScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLedgerMenu() {
        menuLedgers.getItems().clear();
        ledgerList = new ArrayList<>();
        Ledger item = new Ledger();
        item.setLedgerName("ALL");
        ledgerList.add(item);
        ledgerList.addAll(database.getLedgerList());

        List<MenuItem> menuItemList = new ArrayList<>();
        for (Ledger ledger : ledgerList) {
            menuItemList.add(new MenuItem(ledger.getLedgerName()));
        }

        menuLedgers.getItems().addAll(menuItemList);

        for (MenuItem menuItem : menuLedgers.getItems()) {
            menuItem.setOnAction(e -> {
                for (int i = 0; i < ledgerList.size(); i++) {
                    if (ledgerList.get(i).getLedgerName().equals(menuItem.getText())) {
                        if (!menuItem.getText().equals("ALL")) {
                            database.setLedger(ledgerList.get(i));
                        } else {
                            database.setLedger(ledgerList.get(i));
                        }
                        menuLedgers.setText(menuItem.getText());
                    }


                }
                refreshData();
            });
        }

        if (!ledgerList.isEmpty()) {
            Ledger ledger = ledgerList.get(0);
            database.setLedger(ledger);
            menuLedgers.setText(ledger.getLedgerName());
        } else {
            menuLedgers.setText("");
        }
    }

    private void refreshData() {
        if (Util.fxmlLoaderPOS != null) {
            Object object = Util.fxmlLoaderPOS.getController();
            if (object instanceof PaymentOverviewScreenController) {
                ((PaymentOverviewScreenController) object).loadPaymentData();
            }
        }

        if (Util.fxmlLoaderCS != null) {
            Object object = Util.fxmlLoaderCS.getController();
            if (object instanceof ChartScreenController) {
                ChartScreenController cs = (ChartScreenController) object;
                if (cs.getSeriesLineChart() != null) {
                    cs.getSeriesLineChart().getData().clear();
                    cs.createLineChart();
                }
            }

        }

    }

    public void openLedgerOverviewScreen() throws IOException {
        Util.showLedgerOverviewScreen();
        borderPaneDashboard.setCenter(Util.parent);
    }

    public void openPaymentOverviewScreen() throws IOException {
        Util.showPaymentOverviewScreen();
        borderPaneDashboard.setCenter(Util.parent);
    }

    public void openChartOverviewScreen() throws IOException {
        Util.showChartOverviewScreen();
        borderPaneDashboard.setCenter(Util.parent);
    }

    public void openDashboardAccountScreen() throws IOException {
        Util.showAccountScreen();
        borderPaneDashboard.setCenter(Util.parent);
    }

    public Label getLabelUser() {
        return labelUser;
    }

    public void openSettingsScreen() {
    }

    public void signOut(ActionEvent event) throws IOException {
        Util.showLoginScreen(event);
    }

    public void windowMinimize(Event event) {
        Util.windowMinimize(event);
    }

    public void windowClose(Event event) {
        Util.windowClose(event);
    }
}

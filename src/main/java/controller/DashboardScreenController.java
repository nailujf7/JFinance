package controller;

import database.MySQLDatabase;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.Ledger;
import util.ConfigData;
import util.Constants;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Julian Flieter
 * Controller class for DashboardScreen
 */
public class DashboardScreenController implements Initializable {

    public BorderPane borderPaneDashboard;
    public MenuButton menuLedgers;
    public ImageView imageProfile;
    public Label labelUser;
    private MySQLDatabase mySQLDatabase = MySQLDatabase.getMySQLDatabase();
    private List<Ledger> ledgerList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Util.draggable(borderPaneDashboard);
        labelUser.setText(mySQLDatabase.getAccount().getUsername());
        imageProfile.setImage(new Image(ConfigData.loadPrefData("profile", Constants.IMAGE_PROFILE)));
        loadLedgerMenu();
        mySQLDatabase.getLedgerTotalBalance();
        try {
            openChartOverviewScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads ledger names to UI menu button and items
     */
    public void loadLedgerMenu() {
        menuLedgers.getItems().clear();
        ledgerList = new ArrayList<>();
        Ledger ledgerItem = new Ledger();
        ledgerItem.setLedgerName("ALL");
        ledgerList.add(ledgerItem);
        ledgerList.addAll(mySQLDatabase.getLedgerList());

        List<MenuItem> menuItemList = new ArrayList<>();
        for (Ledger ledger : ledgerList) {
            menuItemList.add(new MenuItem(ledger.getLedgerName()));
        }

        menuLedgers.getItems().addAll(menuItemList);
        initializeMenuItems();

        if (!ledgerList.isEmpty()) {
            mySQLDatabase.setLedger(ledgerItem);
            menuLedgers.setText(ledgerItem.getLedgerName());
        }
    }

    /**
     * Initializes the menu items with names of existing ledgers
     */
    private void initializeMenuItems() {
        for (MenuItem menuItem : menuLedgers.getItems()) {
            menuItem.setOnAction(e -> {
                for (Ledger ledger : ledgerList) {
                    if (ledger.getLedgerName().equals(menuItem.getText())) {
                        if (!menuItem.getText().equals("ALL")) {
                            mySQLDatabase.setLedger(ledger);
                        } else {
                            mySQLDatabase.setLedger(ledger);
                        }
                        menuLedgers.setText(menuItem.getText());
                    }


                }
                refreshData();
            });
        }
    }

    /**
     * Refreshes data of payment table after switching to a different ledger
     */
    private void refreshData() {
        if (Util.fxmlLoaderPOS != null) {
            Object object = Util.fxmlLoaderPOS.getController();
            if (object instanceof PaymentOverviewScreenController) {
                ((PaymentOverviewScreenController) object).populatePaymentTable();
            }
        }

        if (Util.fxmlLoaderCS != null) {
            Object object = Util.fxmlLoaderCS.getController();
            if (object instanceof ChartScreenController) {
                ChartScreenController cs = (ChartScreenController) object;
                if (cs.getSeriesLineChart() != null) {
                    cs.createLineChart();
                }
            }

        }

    }

    /**
     * Opens ledger overview screen
     *
     * @throws IOException
     */
    public void openLedgerOverviewScreen() throws IOException {
        Util.showLedgerOverviewScreen();
        borderPaneDashboard.setCenter(Util.parent);
    }

    /**
     * Opens payment overview screen
     *
     * @throws IOException
     */
    public void openPaymentOverviewScreen() throws IOException {
        Util.showPaymentOverviewScreen();
        borderPaneDashboard.setCenter(Util.parent);
    }

    /**
     * Opens chart overview screen
     *
     * @throws IOException
     */
    public void openChartOverviewScreen() throws IOException {
        Util.showChartOverviewScreen();
        borderPaneDashboard.setCenter(Util.parent);
    }

    /**
     * Opens account screen
     *
     * @throws IOException
     */
    public void openAccountScreen() throws IOException {
        Util.showAccountScreen();
        borderPaneDashboard.setCenter(Util.parent);
    }

    /**
     * Getter for UI label
     *
     * @return label
     */
    public Label getLabelUser() {
        return labelUser;
    }

    /**
     * Signs out from account
     *
     * @param event
     * @throws IOException
     */
    public void signOut(ActionEvent event) throws IOException {
        Util.showLoginScreen(event);
    }

    /**
     * Getter for UI menu button
     *
     * @return
     */
    public MenuButton getMenuLedgers() {
        return menuLedgers;
    }

    /**
     * Minimizes window
     *
     * @param event
     */
    public void windowMinimize(Event event) {
        Util.windowMinimize(event);
    }

    /**
     * Closes window
     *
     * @param event
     */
    public void windowClose(Event event) {
        Util.windowClose(event);
    }

    /**
     * Maximizes window
     *
     * @param mouseEvent
     */
    public void windowMaximize(MouseEvent mouseEvent) {
        Util.windowMaximize(mouseEvent);
    }
}

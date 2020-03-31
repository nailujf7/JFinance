package util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.DashboardController;
import controller.LedgerOverviewScreenController;
import controller.PaymentOverviewScreenController;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class Util {
    public static FXMLLoader fxmlLoaderCS;
    public static FXMLLoader fxmlLoaderPOS;
    public static FXMLLoader fxmlLoaderLOS;
    public static FXMLLoader fxmlLoaderLS;
    public static FXMLLoader fxmlLoaderRS;
    public static FXMLLoader fxmlLoaderDS;
    public static FXMLLoader fxmlLoaderIS;
    public static FXMLLoader fxmlLoaderSPS;
    public static FXMLLoader fxmlLoaderAS;
    public static Parent parent;
    public static Stage stage;
    private static Scene scene;
    private static File file;

    public static void showChartOverviewScreen() throws IOException {
        fxmlLoaderCS = new FXMLLoader(Util.class.getClassLoader().getResource(Constants.DASHBOARD_OVERVIEW_SCREEEN));
        parent = fxmlLoaderCS.load();
        parent.setStyle(ConfigData.loadPrefData("theme", Constants.STANDARD_THEME));
    }

    public static void showPaymentOverviewScreen() throws IOException {
        fxmlLoaderPOS = new FXMLLoader(Util.class.getClassLoader().getResource(Constants.PAYMENT_OVERVIEW_SCREEN));
        parent = fxmlLoaderPOS.load();
        parent.setStyle(ConfigData.loadPrefData("theme", Constants.STANDARD_THEME));
    }

    public static void showLedgerOverviewScreen() throws IOException {
        fxmlLoaderLOS = new FXMLLoader(Util.class.getClassLoader().getResource(Constants.LEDGER_OVERVIEW_SCREEN));
        parent = fxmlLoaderLOS.load();
        parent.setStyle(ConfigData.loadPrefData("theme", Constants.STANDARD_THEME));
    }

    public static void showAccountScreen() throws IOException {
        fxmlLoaderAS = new FXMLLoader(Util.class.getClassLoader().getResource(Constants.ACCOUNT_SCREEN));
        parent = fxmlLoaderAS.load();
        parent.setStyle(ConfigData.loadPrefData("theme", Constants.STANDARD_THEME));
    }

    public static void showPaymentInputScreen() throws IOException {
        loadInputScreen(Constants.PAYMENT_INPUT_SCREEN);
        parent.setStyle(ConfigData.loadPrefData("theme", Constants.STANDARD_THEME));
    }

    public static void showLedgerInputScreen() throws IOException {
        loadInputScreen(Constants.LEDGER_INPUT_SCREEN);
        parent.setStyle(ConfigData.loadPrefData("theme", Constants.STANDARD_THEME));
    }

    private static void loadInputScreen(String fxml) throws IOException {
        fxmlLoaderIS = new FXMLLoader(Util.class.getClassLoader().getResource(fxml));
        parent = fxmlLoaderIS.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(new Scene(parent));
        stage.show();
    }

    public static void showLoginScreen(Event event) throws IOException {
        fxmlLoaderLS = new FXMLLoader(Util.class.getClassLoader().getResource(Constants.LOGIN_SCREEN));
        parent = fxmlLoaderLS.load();
        changeWindow(event, parent);
        parent.setStyle(ConfigData.loadPrefData("theme", Constants.STANDARD_THEME));
    }

    public static void showRegistrationScreen(Event event) throws IOException {
        fxmlLoaderRS = new FXMLLoader(Util.class.getClassLoader().getResource(Constants.REGISTRATION_SCREEN));
        parent = fxmlLoaderRS.load();
        changeWindow(event, parent);
        parent.setStyle(ConfigData.loadPrefData("theme", Constants.STANDARD_THEME));
    }


    public static void showDashboardScreen(Event event) throws IOException {
        fxmlLoaderDS = new FXMLLoader(Util.class.getClassLoader().getResource(Constants.DASHBOARD_SCREEN));
        parent = fxmlLoaderDS.load();
        changeWindow(event, parent);
        parent.setStyle(ConfigData.loadPrefData("theme", Constants.STANDARD_THEME));
    }

    private static void changeWindow(Event event, Parent parent) {
        scene = new Scene(parent);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    public static void windowMinimize(Event event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public static void windowClose(Event event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public static void showSplashScreenScreen(Stage stage) throws IOException {
        fxmlLoaderSPS = new FXMLLoader(Util.class.getClassLoader().getResource(Constants.SPLASH_SCREEN));
        parent = fxmlLoaderSPS.load();
        Scene scene = new Scene(parent);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("J-Finance");
        stage.setScene(scene);
        stage.show();
    }

    public static void refreshLedgerData() {
        Object object = fxmlLoaderLOS.getController();
        if (object instanceof LedgerOverviewScreenController) {
            LedgerOverviewScreenController bos = (LedgerOverviewScreenController) object;
            bos.loadLedgerData();
        }
    }

    public static void refreshPaymentData() {
        Object object = fxmlLoaderPOS.getController();
        if (object instanceof PaymentOverviewScreenController) {
            PaymentOverviewScreenController pos = (PaymentOverviewScreenController) object;
            pos.loadPaymentData();
        }
    }

    public static void refreshLedgerMenu() {
        Object object = fxmlLoaderDS.getController();
        if (object instanceof DashboardController) {
            DashboardController dc = (DashboardController) object;
            dc.loadLedgerMenu();
        }

    }

    public static boolean isAccountPayments() {
        Object object = fxmlLoaderDS.getController();
        if (object instanceof DashboardController) {
            DashboardController dc = (DashboardController) object;
            if (dc.getMenuLedgers().getText().equals("ALL")) {
                return true;
            } else {
                return false;
            }
        }
        return false;

    }

    public static boolean confirmationAlert(String alertText) {
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ok, cancel);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setHeaderText(alertText);
        alert.getDialogPane().setStyle(Constants.STANDARD_THEME);
        setAlertSymbol(alert);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
            return true;
        } else {
            return false;
        }
    }

    public static void wrongWarningAlert(String alertText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setHeaderText(alertText);
        setAlertSymbol(alert);
        alert.showAndWait();
    }

    public static void setAlertSymbol(Alert alert) {
        alert.setGraphic(new ImageView(new Image(Constants.IMAGE_ALERT)));
    }

    public static void showFileChooser(ImageView imageView) {
        final FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imageView.setImage(new Image("file:" + file.getPath()));
        }
    }

    public static File showFileChooser() {
        DashboardController dc = fxmlLoaderDS.getController();
        String fileName = "J-Finance_Account_Statements_" + dc.getLabelUser().getText() + "_" + dc.getMenuLedgers().getText() + ".pdf";
        final FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(fileName);
        File file = fileChooser.showSaveDialog(null);
        return file;
    }

    public static java.sql.Date convert2Date(LocalDate date) {
        return java.sql.Date.valueOf(date);
    }

    public static LocalDate convertToLocalDateViaUtilDate(java.util.Date dateToConvert) {
        return new java.util.Date(dateToConvert.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static File getFile() {
        return file;
    }


    public static String colorToHex(Color color) {
        String hex1;
        String hex2;
        parent.setStyle("");

        hex1 = Integer.toHexString(color.hashCode()).toUpperCase();

        switch (hex1.length()) {
            case 2:
                hex2 = "000000";
                break;
            case 3:
                hex2 = String.format("00000%s", hex1.substring(0, 1));
                break;
            case 4:
                hex2 = String.format("0000%s", hex1.substring(0, 2));
                break;
            case 5:
                hex2 = String.format("000%s", hex1.substring(0, 3));
                break;
            case 6:
                hex2 = String.format("00%s", hex1.substring(0, 4));
                break;
            case 7:
                hex2 = String.format("0%s", hex1.substring(0, 5));
                break;
            default:
                hex2 = hex1.substring(0, 6);
        }
        return hex2;
    }

    public static void changeThemeColor(Color color) {
        String theme = "-fx-base: #" + colorToHex(color);
        System.out.println(colorToHex(color));
        ConfigData.setPrefData("theme", theme);
    }

    public static void loadLedger() throws IOException {
        fxmlLoaderLOS = new FXMLLoader(Util.class.getClassLoader().getResource(Constants.LEDGER_OVERVIEW_SCREEN));
        fxmlLoaderLOS.load();
    }

    public static String getSelectedLedgerName(){
        return ((DashboardController) fxmlLoaderDS.getController()).getMenuLedgers().getText();
    }

    public static String dateToString(Date date){
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(date);
    }

    public static File chooseFile() {
        final FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            return file;
        }else{
            return null;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String parseDateToFormat(String dateString) throws ParseException {
        DateFormat parseFormat = new SimpleDateFormat("dd.MM.yy");
        DateFormat formattingFormat = new SimpleDateFormat("dd.MM.yyyy");
        return formattingFormat.format(parseFormat.parse(dateString));
    }
}

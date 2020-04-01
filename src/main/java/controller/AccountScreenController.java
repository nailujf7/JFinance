package controller;

import com.jfoenix.controls.JFXColorPicker;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.ConfigData;
import util.Constants;
import util.Database;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for AccountScreen
 */
public class AccountScreenController implements Initializable {
    public ImageView imageViewProfile;
    public TextField firstnameTextField;
    public TextField lastameTextField;
    public DatePicker dateOfBirth;
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public JFXColorPicker colorPicker;
    private Database database = Database.getDatabase();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAccountData();
        imageViewProfile.setImage(new Image(ConfigData.loadPrefData("profile", Constants.IMAGE_PROFILE)));
    }

    /**
     * Loads the data of the current user account and maps them to the respective UI fields
     */
    public void loadAccountData() {
        firstnameTextField.setText(database.getAccount().getFirstname());
        lastameTextField.setText(database.getAccount().getLastname());
        dateOfBirth.setValue(Util.convertToLocalDateViaUtilDate(database.getAccount().getDateOfBirth()));
        usernameTextField.setText(database.getAccount().getUsername());
        passwordTextField.setText(database.getAccount().getPassword());
    }

    /**
     * Saves account changes such as theme color or account information
     * @param event
     * @throws IOException
     */
    public void saveAccount(ActionEvent event) throws IOException {
        if (Util.confirmationAlert("Do you really want to apply your changes?\nYou will be logged out!")) {
            if (Util.getFile() != null) {
                ConfigData.setPrefData("profile", "file:" + Util.getFile().getAbsolutePath());
            }
            if (colorPicker.getValue() != null && !colorPicker.getValue().toString().equals("0xffffffff")) {
                Util.changeThemeColor(colorPicker.getValue());
            }
            Util.loadLedger();
            database.updateAccount(firstnameTextField.getText(), lastameTextField.getText(), Util.convert2Date(dateOfBirth.getValue()), usernameTextField.getText(), passwordTextField.getText());
            Util.showLoginScreen(event);
        }
    }

    /**
     * Deletes the account and all related data
     * @param event
     * @throws IOException
     */
    public void deleteAccount(ActionEvent event) throws IOException {
        if (Util.confirmationAlert("Do you really want to delete this entry?")) {
            ConfigData.setDefaultData();
            database.deleteAccount();
            Util.showRegistrationScreen(event);
        }
    }

    /**
     * Opens file chooser to change account profile picture
     */
    public void showFileChooser() {
        Util.showFileChooser(imageViewProfile);
    }
}

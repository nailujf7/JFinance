package controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import util.ConfigData;
import util.Constants;
import util.Database;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for LoginScreen
 */
public class LoginScreenController implements Initializable {

    public TextField textFieldUsername;
    public PasswordField textFieldPassword;
    public ImageView imageViewProfile;
    public JFXCheckBox checkBoxRemeberMe;
    private Database database = Database.getDatabase();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageViewProfile.setImage(new Image(ConfigData.loadPrefData("profile", Constants.IMAGE_PROFILE)));
        setDefaultData();
    }

    /**
     * Checks account credentials and signs in if credentials are correct
     * @param event
     * @throws IOException
     */
    public void signIn(Event event) throws IOException {
        if (database.authenticate(textFieldUsername.getText(), textFieldPassword.getText())) {
            saveData();
            Util.showDashboardScreen(event);
        } else {
            Util.warningAlert("Wrong credentials, try again!");
        }
    }

    /**
     * Shows registration screen
     * @param event
     * @throws IOException
     */
    public void showRegistrationScreen(ActionEvent event) throws IOException {
        Util.showRegistrationScreen(event);
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

    /**
     * Saves account credentials if user wants the credentials to be remembered
     */
    public void saveData() {
        if (checkBoxRemeberMe.isSelected()) {
            ConfigData.setPrefData("data", "saved");
            ConfigData.setPrefData("username", textFieldUsername.getText());
            ConfigData.setPrefData("password", textFieldPassword.getText());
        }
    }

    /**
     * Applies remembered account credentials if set
     */
    public void setDefaultData() {
        if (ConfigData.userDataExist()) {
            textFieldUsername.setText(ConfigData.loadPrefData("username", ""));
            textFieldPassword.setText(ConfigData.loadPrefData("password", ""));
            imageViewProfile.setImage(new Image(ConfigData.loadPrefData("profile", Constants.IMAGE_PROFILE)));
        }
    }

    /**
     * Logs in if enter button is pressed
     * @param keyEvent
     * @throws IOException
     */
    public void enterButton(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            signIn(keyEvent);
        }
    }
}

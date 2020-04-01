package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Constants;
import util.Database;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for RegistrationScreen
 */
public class RegistrationScreenController implements Initializable {

    public TextField firstnameTextField;
    public TextField lastameTextField;
    public DatePicker dateOfBirth;
    public PasswordField passwordTextField;
    public TextField usernameTextField;
    public ImageView imageViewProfile;
    private Database database = Database.getDatabase();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageViewProfile.setImage(new Image(Constants.IMAGE_PROFILE));
    }

    /**
     * Creates a new account
     * @param event
     * @throws IOException
     */
    public void createAccount(ActionEvent event) throws IOException {
        if (!(firstnameTextField.getText().equals("") || lastameTextField.getText().equals("") || dateOfBirth.getValue() == null || usernameTextField.getText().equals("") ||
                passwordTextField.getText().equals(""))) {
            if (!database.usernameExists(usernameTextField.getText())) {
                database.createAccount(firstnameTextField.getText(), lastameTextField.getText(),
                        java.sql.Date.valueOf(dateOfBirth.getValue()),
                        usernameTextField.getText(), passwordTextField.getText());
                Util.showLoginScreen(event);
            } else {
                Util.warningAlert("Username already exists, please choose another one!");
            }
        } else {
            Util.warningAlert("Please fill out all fields!");
        }
    }

    /**
     * Shows login screen
     * @param event
     * @throws IOException
     */
    public void showLoginScreen(ActionEvent event) throws IOException {
        Util.showLoginScreen(event);
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

}

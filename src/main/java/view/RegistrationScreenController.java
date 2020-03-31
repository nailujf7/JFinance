package view;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import util.Constants;
import util.Database;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationScreenController implements Initializable {

    public TextField firstnameTextField;
    public TextField lastameTextField;
    public DatePicker dateOfBirth;
    public PasswordField passwordTextField;
    public TextField usernameTextField;
    public ImageView imageViewProfile;
    public AnchorPane anchorPaneRegistrationScreen;
    public JFXButton buttonSignUp;
    private Database database = Database.getDatabase();

    public RegistrationScreenController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageViewProfile.setImage(new Image(Constants.IMAGE_PROFILE));
    }

    public void createAccount(ActionEvent event) throws IOException {
        if (!(firstnameTextField.getText().equals("") || lastameTextField.getText().equals("") || dateOfBirth.getValue() == null || usernameTextField.getText().equals("") ||
                passwordTextField.getText().equals(""))) {
            if (!database.usernameExists(usernameTextField.getText())) {
                database.createAccount(firstnameTextField.getText(), lastameTextField.getText(),
                        java.sql.Date.valueOf(dateOfBirth.getValue()),
                        usernameTextField.getText(), passwordTextField.getText());
                Util.showLoginScreen(event);
            }else{
                Util.wrongWarningAlert("Username already exists, please choose another one!");
            }
        }else{
            Util.wrongWarningAlert("Please fill out all fields!");
        }
    }

    public void showLoginScreen(ActionEvent event) throws IOException {
        Util.showLoginScreen(event);
    }

    public void windowMinimize(Event event) {
        Util.windowMinimize(event);
    }

    public void windowClose(Event event) {
        Util.windowClose(event);
    }

}

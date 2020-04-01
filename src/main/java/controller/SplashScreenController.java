package controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import main.Main;
import util.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for SplashScreen
 */
public class SplashScreenController implements Initializable {
    public AnchorPane root;
    public ImageView imageViewSplash;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageViewSplash.setImage(new Image(Constants.IMAGE_JFINANCE));
        try {
            if (!main.Main.isSplashLoaded)
                loadSplashScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Loads splash screen with fade to login screen
     * @throws IOException
     */
    private void loadSplashScreen() throws IOException {
        Main.isSplashLoaded = true;
        //Load splash screen view FXML
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource((Constants.SPLASH_SCREEN))));
        //Add it to root container (Can be StackPane, AnchorPane etc)
        root.getChildren().setAll(pane);

        //Load splash screen with fade in effect
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.75), pane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        //Finish splash with fade out effect
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.75), pane);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);

        fadeIn.play();

        //After fade in, start fade out
        fadeIn.setOnFinished((e) -> fadeOut.play());

        //After fade out, load actual content
        fadeOut.setOnFinished((e) -> {
            try {
                AnchorPane parentContent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource((Constants.LOGIN_SCREEN))));
                root.getChildren().setAll(parentContent);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

    }


}

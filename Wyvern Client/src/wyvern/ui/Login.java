package wyvern.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wyvern.Redux;

import java.io.IOException;

public class Login extends WyvernController {

    @FXML
    public Button login;
    @FXML
    public TextField email;
    @FXML
    public PasswordField pass;

    @FXML public Stage primaryStage;

    @FXML
    protected void initialize() {
        setupButtonAnimation(login, 1.01);
        login.setOnAction(event -> {
            try {
                throw new IOException("Not implemented yet.");
            } catch (Exception ex) {
                Redux.getInstance().alert(Alert.AlertType.ERROR, "Oops!", "Alert: " + ex.getMessage());
            }
        });

//        primaryStage.setOnCloseRequest(event -> {
//            Redux.getInstance().loadWindow("/fxml/Connect.fxml");
//        });

    }


}

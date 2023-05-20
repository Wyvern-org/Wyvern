package wyvern.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import wyvern.Redux;
import wyvern.main.Main;

import java.io.IOException;

public class ConnectController extends WyvernController {
    @FXML
    protected Button wyvernButton, LogOut, Account, custom;
    @FXML
    protected AnchorPane window;

    @FXML
    public void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.SLASH) {
            try {
                Redux.getInstance().getNetworkManager().connect("dev.chat.wyvernapp.com", 5200);
                Redux.getInstance().loadWindow("/fxml/Main.fxml");
                Redux.getInstance().getNetworkManager().start();
            }catch (IOException ex) {
                Redux.getInstance().alert(Alert.AlertType.ERROR, "Oops!", "Connection failed: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                Redux.getInstance().alert(Alert.AlertType.ERROR, "Oops!", "You need to enter a valid port number (eg., 5600)");
            }
        }
    }

    @FXML
    protected void initialize() {
        setupButtonAnimation(wyvernButton, 1.01);
        setupButtonAnimation(LogOut, 1.01);
        setupButtonAnimation(Account, 1.01);
        setupButtonAnimation(custom, 1.01);


        wyvernButton.setOnAction(event -> {
            try {
                Redux.getInstance().getNetworkManager().connect("prod.chat.wyvernapp.com", 5600);
                Redux.getInstance().loadWindow("/fxml/Main.fxml");
                Redux.getInstance().getNetworkManager().start();
            } catch (IOException ex) {
                Redux.getInstance().alert(Alert.AlertType.ERROR, "Oops!", "Connection failed: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                Redux.getInstance().alert(Alert.AlertType.ERROR, "Oops!", "You need to enter a valid port number (eg., 5600)");
            }
        });
    }
}

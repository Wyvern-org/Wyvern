package wyvern.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import wyvern.Redux;
import wyvern.main.Main;

import java.io.IOException;

public class ConnectController extends WyvernController
{
    @FXML protected TextField txtIP, txtPort;
    @FXML protected Button connectButton, wyvernButton, LogOut, Account;

    @FXML protected void initialize()
    {
        setupButtonAnimation(connectButton, 1.1);
        setupButtonAnimation(wyvernButton, 1.1);
        setupButtonAnimation(LogOut, 1.1);
        setupButtonAnimation(Account, 1.1);

        connectButton.setOnAction((event) ->
        {
            try
            {
                Redux.getInstance().getNetworkManager().connect(txtIP.getText(), Integer.parseInt(txtPort.getText()));
                Redux.getInstance().loadWindow("/fxml/Main.fxml");
                Redux.getInstance().getNetworkManager().start();
            } catch (IOException ex) {
                Redux.getInstance().alert(Alert.AlertType.ERROR, "Oops!", "Connection failed: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                Redux.getInstance().alert(Alert.AlertType.ERROR, "Oops!", "You need to enter a valid port number (eg., 5600)");
            }
        });
        wyvernButton.setOnAction(event -> {
            try
            {
                    Redux.getInstance().getNetworkManager().connect("dev.chat.wyvernapp.com", 42069);
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

package wyvern.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import wyvern.Redux;
import wyvern.main.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class ConnectController extends WyvernController {
    @FXML
    protected Button wyvernButton, custom, login, register;
    @FXML
    protected AnchorPane window;
    @FXML
    protected Label news,newstitle;

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
        if (event.getCode() == KeyCode.CONTROL) {
            Redux.getInstance().loadWindow("/fxml/CustomConnect.fxml");
        }

        if (event.getCode() == KeyCode.D){
            Redux.getInstance().loadWindow("/fxml/nfr_use/loader.fxml");
        }
    }

    @FXML
    protected void initialize() {
        try {
            URL url = new URL("http://api.wyvernapp.com/news.txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();
            news.setText(response.toString());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            URL url = new URL("http://api.wyvernapp.com/verinf.txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);//.append("\n");
            }
            reader.close();
            if (!Objects.equals(Main.VERSION, response.toString())) {
                newstitle.setText("There is a new update available! Update " + response.toString() + "!");
                news.setText("For security reasons, You are unable to join the Wyvern network until you update. \nYou may still connect to custom servers however.\nAccount services have also been disabled.");
                wyvernButton.setDisable(true);
                login.setDisable(true);
                register.setDisable(true);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }



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
        login.setOnAction(event -> {
                Redux.getInstance().loadWindow("/fxml/Login.fxml");
        });
        register.setOnAction(event -> {
            Redux.getInstance().loadWindow("/fxml/Register.fxml");
        });
    }
}

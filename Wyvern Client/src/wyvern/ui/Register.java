package wyvern.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends WyvernController{

    @FXML public Button Register;
    @FXML public TextField age, username, email, password;
    @FXML public CheckBox agegree, tosagree;

    @FXML public TextArea TOSPanel;
    @FXML public void initialize() {
        try {
            URL url = new URL("http://prod.uas.wyvernapp.com/tos.txt");
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
            TOSPanel.setText(response.toString());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Register.setOnAction(event -> {
            try {
                URL url = new URL("http://prod.uas.wyvernapp.com/api/v1/user");
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }
        });
    }
}

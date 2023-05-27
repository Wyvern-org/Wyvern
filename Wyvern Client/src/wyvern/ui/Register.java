package wyvern.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import wyvern.util.jobs.RegisterJob;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends WyvernController{
    private static final Gson gson = new Gson();

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
                RegisterJob registerJob = new RegisterJob(email.getText(), username.getText(), password.getText(), (result) ->
                {
                    //TODO: i need to rework the entire http request system since we can't really see any of the metadata for the actual request in here
                });
                registerJob.start();
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }
        });
    }
}

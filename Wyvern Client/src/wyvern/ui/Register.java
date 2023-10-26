package wyvern.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import wyvern.Redux;
import wyvern.util.http.HttpRequest;
import wyvern.util.jobs.RegisterJob;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Register extends WyvernController{
    private static final Gson gson = new Gson();

    @FXML public Button Register;
    @FXML public TextField age, username, email, password;
    @FXML public CheckBox agegree, tosagree;

    @FXML public TextArea TOSPanel, ServiceInfo;
    @FXML public void initialize() {
        try {
            URL url = new URL("http://api.wyvernapp.com/TOS.txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            //System.out.println("Response Code: " + responseCode);
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
            e.printStackTrace(System.err);
        }
        try {
            URL url = new URL("http://api.wyvernapp.com/RSERV.txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            //System.out.println("Response Code: " + responseCode);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();
            ServiceInfo.setText(response.toString());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        Register.setOnAction(event -> {
            try {
                RegisterJob registerJob = new RegisterJob(email.getText(), username.getText(), password.getText(), (httpRequest) ->
                {
                    HttpRequest request = (HttpRequest) httpRequest;
                    if (request == null || Objects.requireNonNull(request).getResponse().toString().isEmpty()) Redux.getInstance().alert(Alert.AlertType.ERROR, "Failed to connect to the User Authentication Server. Please try again later.", "Oh noes!");
                    JsonObject response = gson.fromJson(Objects.requireNonNull(request).getResponse().toString(), JsonObject.class);
                    if (response.has("success"))
                    {
                        if (response.get("success").getAsBoolean())
                        {
                            Redux.getInstance().alert(Alert.AlertType.INFORMATION, "Your account has been registered! Please continue to the login screen, and provide your newly created credentials.", "Yay!");
                            Platform.runLater(() -> Redux.getInstance().loadWindow("/fxml/Login.fxml"));
                        } else {
                            Redux.getInstance().alert(response.get("message").getAsString(), "Authentication failed");
                        }
                    } else {
                        Redux.getInstance().alert(Alert.AlertType.ERROR, "An unknown error occurred while trying to authenticate you.", "Oh noes!");
                    }
                });
                registerJob.start();
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }
        });
    }
}

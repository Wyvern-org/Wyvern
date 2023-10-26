package wyvern.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import wyvern.Redux;
import wyvern.util.DataStore;
import wyvern.util.http.HttpRequest;
import wyvern.util.jobs.AuthJob;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class LoginController extends WyvernController {
    private static Gson gson = new Gson();

    @FXML
    public Button login;
    @FXML
    public TextField email;
    @FXML
    public PasswordField pass;

    @FXML public TextArea statuscheck;

    @FXML
    protected void initialize() {
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
            statuscheck.setText(response.toString());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        login.setOnAction(event -> {
            try {
                AuthJob authJob = new AuthJob(email.getText(), pass.getText(), (httpRequest) ->
                {
                    HttpRequest request = (HttpRequest) httpRequest;
                    if (request == null || Objects.requireNonNull(request).getResponse().toString().isEmpty()) Redux.getInstance().alert(Alert.AlertType.ERROR, "Failed to connect to the User Authentication Server. Please try again later.", "Oh noes!");
                    JsonObject res = gson.fromJson(Objects.requireNonNull(request).getResponse().toString(), JsonObject.class);
                    if (res.has("jwt"))
                    {
                        DataStore dataStore = Redux.getInstance().getDataStore();
                        String username = res.get("username").getAsString();
                        String uuid = res.get("uuid").getAsString();
                        String jwt = res.get("jwt").getAsString();

                        System.out.println("Logged in as " + username);

                        dataStore.setString("username", username);
                        dataStore.setString("uuid", uuid);
                        dataStore.setString("jwt", jwt);
                        //TODO: JWT should be saved to a file so we can validate it on the API rather than asking for login every time
                        Platform.runLater(() -> Redux.getInstance().loadWindow("/fxml/Connect.fxml"));
                    } else {
                        //TODO: auth definitely did not work, the jwt isn't there :/
                        // another side note, i forgor to add proper error handling to the API for when credentials are wrong
                        String msg = res.has("message") ? res.get("message").getAsString() : "Failed to authenticate!";
                        Platform.runLater(() -> Redux.getInstance().alert(Alert.AlertType.ERROR, msg, "Oh noes!"));
                    }
                });
                authJob.start(); // almost forgor to start the job lol
            } catch (Exception ex) {
                Redux.getInstance().alert(Alert.AlertType.ERROR, "Oops!", "Alert: " + ex.getMessage());
            }
        });

//        primaryStage.setOnCloseRequest(event -> {
//            Redux.getInstance().loadWindow("/fxml/Connect.fxml");
//        });

    }


}

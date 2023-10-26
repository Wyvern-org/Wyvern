package wyvern.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wyvern.Redux;
import wyvern.util.DataStore;
import wyvern.util.http.HttpRequest;
import wyvern.util.jobs.AuthJob;

import java.io.IOException;
import java.util.Objects;

public class LoginController extends WyvernController {
    private static Gson gson = new Gson();

    @FXML
    public Button login;
    @FXML
    public TextField email;
    @FXML
    public PasswordField pass;

    @FXML public Stage primaryStage;

    @FXML
    protected void initialize() {
        login.setOnAction(event -> {
            try {
                AuthJob authJob = new AuthJob(email.getText(), pass.getText(), (httpRequest) ->
                {
                    HttpRequest request = (HttpRequest) httpRequest;
                    if (request == null || Objects.requireNonNull(request).getResponse().toString().isEmpty()) Redux.getInstance().alert(Alert.AlertType.ERROR, "Failed to connect to the User Authentication Server. Please try again later.", "Oh noes!");
                    JsonObject res = gson.fromJson(Objects.requireNonNull(request).getResponse().toString(), JsonObject.class);
                    System.out.println("Raw auth response: " + request.getResponse().toString());
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
                        Platform.runLater(() -> Redux.getInstance().alert(Alert.AlertType.ERROR, "Failed to authenticate!", "Oh noes!"));
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

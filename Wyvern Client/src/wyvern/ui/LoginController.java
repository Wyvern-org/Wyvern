package wyvern.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wyvern.Redux;
import wyvern.util.jobs.AuthJob;

import java.io.IOException;

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
        setupButtonAnimation(login, 1.01);
        login.setOnAction(event -> {
            try {
                AuthJob authJob = new AuthJob(email.getText(), pass.getText(), (result) ->
                {
                    assert result != null;
                    if (result instanceof String)
                    {
                        String str = (String) result;
                        if (!str.isEmpty())
                        {
                            JsonObject res = gson.fromJson(str, JsonObject.class);
                            if (res.has("jwt"))
                            {
                                //TODO: auth worked, store jwt & user info somewhere so the connection window knows we're logged in for real
                                // load connection window
                                //Redux.getInstance().loadWindow("/fxml/Connect.fxml");
                            } else {
                                //TODO: auth definitely did not work, the jwt isn't there :/
                                // another side note, i forgor to add proper error handling to the API for when credentials are wrong
                            }
                        }
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

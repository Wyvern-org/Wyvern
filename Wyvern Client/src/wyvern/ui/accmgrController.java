package wyvern.ui;
import com.sun.prism.paint.Paint;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import wyvern.Redux;
import wyvern.main.Main;
import wyvern.util.DataStore;
import wyvern.util.pipes.SocketPipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
public class accmgrController extends WyvernController{

    @FXML public Label emailro, usernamero, uuidro, createdro;
    @FXML public TextArea statusro;

    @FXML protected void initialize(){
        DataStore dataStore = Redux.getInstance().getDataStore();
        if (dataStore.hasString("username"))
        {
            usernamero.setText("Username: "+dataStore.getString("username", "N/A"));
            emailro.setText("Email: "+dataStore.getString("email", "N/A"));
            uuidro.setText("UUID: "+dataStore.getString("uuid", "N/A"));
            createdro.setText("Created: "+dataStore.getInt("created_at", 0));
        }
        try {
            URL url = new URL("http://api.wyvernapp.com/RSERV.txt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            reader.close();
            statusro.setText(response.toString());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

}

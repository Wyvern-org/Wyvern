package wyvern;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import wyvern.main.Main;
import wyvern.net.NetworkManager;
import wyvern.util.Util;

import java.util.Objects;

public class Redux
{
    private Stage primaryStage;
    private NetworkManager networkManager;

    public void init(Stage primaryStage)
    {
        instance = this;
        this.primaryStage = primaryStage;
        networkManager = new NetworkManager();
        loadWindow("/fxml/Connect.fxml");
    }

    public void loadWindow(String fxmlPath)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fxml/css/darcula.css")).toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Wyvern v" + Main.VERSION);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception ex) {
            System.out.println("Failed to launch FX application");
            System.exit(1);
        }
    }

    public void alert(String msg, String title)
    {
        alert(Alert.AlertType.INFORMATION, msg, title);
    }

    public void alert(Alert.AlertType alertType, String msg, String title)
    {
        alert(alertType, msg, title, false);
    }

    public void alert(Alert.AlertType alertType, String msg, String title, boolean wait)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle("System Alert");
        alert.setContentText(msg);
        alert.setResizable(false);
        alert.setHeaderText(title);
        if (wait) alert.showAndWait();
        else alert.show();
    }

    public NetworkManager getNetworkManager()
    {
        return networkManager;
    }

    private static Redux instance;

    public static Redux getInstance()
    {
        return instance;
    }
}

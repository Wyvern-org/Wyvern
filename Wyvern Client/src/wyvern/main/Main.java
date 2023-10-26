package wyvern.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import wyvern.Redux;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    public static String VERSION = "0.0.1";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Redux().init(primaryStage);
    }
}

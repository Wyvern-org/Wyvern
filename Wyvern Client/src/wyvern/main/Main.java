package wyvern.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import wyvern.Redux;

import java.net.URL;
import java.util.Objects;

public class Main extends Application
{
    public static boolean devMode = false;
    public static float VERSION = 0.01f;

    public static void main(String[] args)
    {
        if(args.length > 0 && args[0].equals("-dev"))
            devMode = true;

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        new Redux().init(primaryStage);
    }
}

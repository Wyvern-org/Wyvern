package wyvern;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import wyvern.main.Main;
import wyvern.net.NetworkManager;
import wyvern.net.PacketRegistry;
import wyvern.net.handlers.ChatHandler;
import wyvern.net.handlers.DisconnectHandler;
import wyvern.net.handlers.HandshakeHandler;
import wyvern.net.packets.Packet0Handshake;
import wyvern.net.packets.Packet1Chat;
import wyvern.net.packets.Packet2Disconnect;
import wyvern.ui.WyvernController;
import wyvern.util.DataStore;
import wyvern.util.Util;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Redux
{
    private Stage primaryStage;
    private NetworkManager networkManager;
    private WyvernController activeController;
    private DataStore dataStore;

    public void init(Stage primaryStage)
    {
        instance = this;
        this.dataStore = new DataStore();
        this.primaryStage = primaryStage;
        networkManager = new NetworkManager();
        PacketRegistry.registerPacket(0, Packet0Handshake.class);
        PacketRegistry.registerPacketHandler(0, HandshakeHandler.class);
        PacketRegistry.registerPacket(1, Packet1Chat.class);
        PacketRegistry.registerPacketHandler(1, ChatHandler.class);
        PacketRegistry.registerPacket(2, Packet2Disconnect.class);
        PacketRegistry.registerPacketHandler(2, DisconnectHandler.class);
        loadWindow("/fxml/Connect.fxml");

        // keep the network connection alive
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5);
        executorService.scheduleAtFixedRate(() ->
        {
            try
            {
                networkManager.sendPacket(new Packet0Handshake(1));
            } catch (IOException ignored) {}
        }, 0L, 5, TimeUnit.SECONDS);
    }

    public void loadWindow(String fxmlPath)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            this.activeController = loader.getController();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fxml/css/win7.css")).toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Wyvern v" + Main.VERSION);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception ex) {
            System.out.println("Failed to launch FX application");
            ex.printStackTrace();
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
        try
        {
            Platform.runLater(() ->
            {
                Alert alert = new Alert(alertType);
                alert.setTitle("System Alert");
                alert.setContentText(msg);
                alert.setResizable(false);
                alert.setHeaderText(title);
                if (wait) alert.showAndWait();
                else alert.show();
            });
        } catch (Exception ex) {
            try
            {
                JOptionPane.showMessageDialog(null, msg, title, JOptionPane.PLAIN_MESSAGE);
            } catch (Exception ex2) {
                ex.printStackTrace(System.err);
                ex2.printStackTrace(System.err);
            }
        }
    }

    public NetworkManager getNetworkManager()
    {
        return networkManager;
    }

    public WyvernController getActiveController()
    {
        return activeController;
    }

    public DataStore getDataStore()
    {
        return dataStore;
    }

    private static Redux instance;

    public static Redux getInstance()
    {
        return instance;
    }
}

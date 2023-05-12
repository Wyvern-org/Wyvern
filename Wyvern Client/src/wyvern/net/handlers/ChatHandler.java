package wyvern.net.handlers;

import javafx.application.Platform;
import wyvern.Client;
import wyvern.Redux;
import wyvern.net.AbstractPacket;
import wyvern.net.PacketHandler;
import wyvern.net.packets.Packet1Chat;
import wyvern.ui.MainController;
import wyvern.ui.WyvernController;

public class ChatHandler implements PacketHandler
{
    @Override
    public void handlePacket(AbstractPacket packet)
    {
        Packet1Chat chat = (Packet1Chat) packet;

        try
        {
            //System.out.println("Received message through ChatHandler: " + chat.getMessage());
            Client.instance.receiveMessage(chat.getMessage());
        } catch (Exception ignored) {}

        try
        {
            System.out.println("Received message through ChatHandler: " + chat.getMessage());
            WyvernController activeController = Redux.getInstance().getActiveController();
            if (activeController == null) System.out.println("activeController is null");
            if (activeController instanceof MainController)
            {
                MainController controller = (MainController) activeController;
                Platform.runLater(() -> controller.appendToChatLog(chat.getMessage()));
            } else System.out.println("a message was received but activeController is not an instanceof MainController");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

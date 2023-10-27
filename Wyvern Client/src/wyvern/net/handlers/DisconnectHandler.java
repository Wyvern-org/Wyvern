package wyvern.net.handlers;

import javafx.scene.control.Alert;
import wyvern.Redux;
import wyvern.net.AbstractPacket;
import wyvern.net.NetworkManager;
import wyvern.net.PacketHandler;
import wyvern.net.packets.Packet1Chat;
import wyvern.net.packets.Packet2Disconnect;

import java.io.IOException;

public class DisconnectHandler implements PacketHandler
{
    @Override
    public void handlePacket(NetworkManager networkManager, AbstractPacket packet)
    {
        try
        {
            System.out.println("Disconnect packet received from server");
            new ChatHandler().handlePacket(networkManager, new Packet1Chat("Disconnected by server: " + ((Packet2Disconnect)packet).getReason()));
            networkManager.getSocket().close();
            networkManager.getSocketPipe().flush(networkManager.getSocket());
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}

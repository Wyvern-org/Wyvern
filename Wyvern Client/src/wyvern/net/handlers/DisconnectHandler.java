package wyvern.net.handlers;

import wyvern.Redux;
import wyvern.net.AbstractPacket;
import wyvern.net.NetworkManager;
import wyvern.net.PacketHandler;

import java.io.IOException;

public class DisconnectHandler implements PacketHandler
{
    @Override
    public void handlePacket(NetworkManager networkManager, AbstractPacket packet)
    {
        try
        {
            networkManager.getSocket().close();
            networkManager.getSocketPipe().flush(networkManager.getSocket());
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}

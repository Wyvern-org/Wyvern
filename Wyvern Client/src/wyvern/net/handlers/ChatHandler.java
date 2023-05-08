package wyvern.net.handlers;

import wyvern.Client;
import wyvern.net.AbstractPacket;
import wyvern.net.PacketHandler;
import wyvern.net.packets.Packet1Chat;

public class ChatHandler implements PacketHandler
{
    @Override
    public void handlePacket(Client client, AbstractPacket packet)
    {
        Packet1Chat chat = (Packet1Chat) packet;

        try
        {
            //System.out.println("Received message through ChatHandler: " + chat.getMessage());
            client.receiveMessage(chat.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

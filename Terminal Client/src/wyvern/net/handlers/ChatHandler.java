package wyvern.net.handlers;

import wyvern.net.AbstractPacket;
import wyvern.net.PacketHandler;
import wyvern.net.packets.Packet1Chat;

public class ChatHandler implements PacketHandler
{
    @Override
    public void handlePacket(AbstractPacket packet)
    {
        Packet1Chat chat = (Packet1Chat) packet;

        try
        {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

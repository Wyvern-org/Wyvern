package wyvern.net;

import wyvern.net.Packet;

public abstract class AbstractPacket implements Packet
{
    protected int packetID;

    public AbstractPacket(int packetID)
    {
        this.packetID = packetID;
    }

    public int getPacketID()
    {
        return packetID;
    }
}

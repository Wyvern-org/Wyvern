package com.owen2k6.chat.network.redux;

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

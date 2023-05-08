package wyvern.net.packets;

import wyvern.net.AbstractPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet0Handshake extends AbstractPacket
{
    private int protocolVersion;

    public Packet0Handshake()
    {
        super(0);
    }

    public Packet0Handshake(int protocolVersion)
    {
        super(0);
        this.protocolVersion = protocolVersion;
    }

    @Override
    public void readData(DataInputStream dis) throws IOException
    {
        protocolVersion = dis.readInt();
    }

    @Override
    public void writeData(DataOutputStream dis) {}

    public int getProtocolVersion()
    {
        return protocolVersion;
    }
}

package com.owen2k6.chat.network.redux.packets;

import com.owen2k6.chat.network.redux.AbstractPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet2Disconnect extends AbstractPacket
{
    private String reason;

    public Packet2Disconnect()
    {
        super(2);
    }

    public Packet2Disconnect(String reason)
    {
        super(2);
        this.reason = reason;
    }

    @Override
    public void readData(DataInputStream dis) throws IOException
    {
        this.reason = dis.readUTF();
    }

    @Override
    public void writeData(DataOutputStream dos) throws IOException
    {
        dos.writeUTF(this.reason);
    }
}

package wyvern.net.packets;

import wyvern.net.AbstractPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet1Chat extends AbstractPacket
{
    private String message;

    public Packet1Chat()
    {
        super(1);
    }

    public Packet1Chat(String message)
    {
        super(1);
        this.message = message;
    }

    @Override
    public void readData(DataInputStream dis) throws IOException
    {
        message = dis.readUTF();
    }

    @Override
    public void writeData(DataOutputStream dos) throws IOException
    {
        dos.writeUTF(message);
    }

    public String getMessage()
    {
        return message;
    }
}

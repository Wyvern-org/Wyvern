package wyvern.net;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkManager extends Thread
{
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public void connect(String ip, int port) throws IOException
    {
        socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port));
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void run()
    {
        while (!socket.isClosed())
        {
            try
            {
                int packetID = dis.readInt();
                Class<? extends AbstractPacket> packetClass = PacketRegistry.getPacketClass(packetID);
                if (packetClass != null)
                {
                    AbstractPacket packet = packetClass.newInstance();
                    packet.readData(dis);

                    PacketHandler packetHandler = PacketRegistry.getPacketHandler(packetID).newInstance();
                    packetHandler.handlePacket(packet);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendPacket(AbstractPacket packet) throws IOException
    {
        if (!socket.isConnected() || socket.isClosed()) return;
        dos.writeInt(packet.getPacketID());
        packet.writeData(dos);
    }

    public Socket getSocket()
    {
        return socket;
    }
}

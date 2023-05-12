package wyvern.net;

import javafx.scene.control.Alert;
import wyvern.Redux;

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
                try
                {
                    if (ex instanceof EOFException)
                        socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                ex.printStackTrace();
            }
        }
    }

    public void sendPacket(AbstractPacket packet) throws IOException
    {
        if (!socket.isConnected() || socket.isClosed())
        {
            Redux.getInstance().alert(Alert.AlertType.ERROR, "Oops!", "Can't send message. Client is not connected to any server.");
            return;
        }
        System.out.println("Sending packet: " + packet.getPacketID());
        dos.writeInt(packet.getPacketID());
        packet.writeData(dos);
        if (socket.isConnected() && !socket.isClosed()) dos.flush();
    }

    public Socket getSocket()
    {
        return socket;
    }
}

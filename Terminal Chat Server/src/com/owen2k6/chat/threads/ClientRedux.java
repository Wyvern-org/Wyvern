package com.owen2k6.chat.threads;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.account.user;
import com.owen2k6.chat.configuration.motd;
import com.owen2k6.chat.network.redux.AbstractPacket;
import com.owen2k6.chat.network.redux.PacketHandler;
import com.owen2k6.chat.network.redux.PacketRegistry;
import com.owen2k6.chat.network.redux.packets.Packet1Chat;

import java.io.*;
import java.net.Socket;

public class ClientRedux extends Thread
{
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean loggedIn;
    private Server server;
    private user userInfo;

    public ClientRedux(Server server, Socket socket)
    {
        this.server = server;
        this.socket = socket;
        try
        {
            this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        try
        {
            if (socket.isConnected()) sendMessage(motd.getMOTD());
            while (!socket.isClosed())
            {
                int packetID = dis.readInt();
                Class<? extends AbstractPacket> packetClass = PacketRegistry.getPacketClass(packetID);
                if (packetClass != null)
                {
                    AbstractPacket packet = packetClass.newInstance();
                    packet.readData(dis);

                    PacketHandler packetHandler = PacketRegistry.getPacketHandler(packetID).newInstance();
                    packetHandler.handlePacket(this, packet);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException
    {
        Packet1Chat chat = new Packet1Chat(message);
        sendPacket(chat);
    }

    public void sendPacket(AbstractPacket packet) throws IOException
    {
        dos.writeInt(packet.getPacketID());
        packet.writeData(dos);
    }

    public void disconnect() throws IOException
    {
        disconnect("Quit");
    }

    public void disconnect(String message) throws IOException
    {
        sendMessage(message);
        socket.close();
    }
}

package com.owen2k6.chat.threads;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.account.user;
import com.owen2k6.chat.configuration.motd;
import com.owen2k6.chat.network.redux.AbstractPacket;
import com.owen2k6.chat.network.redux.PacketHandler;
import com.owen2k6.chat.network.redux.PacketRegistry;
import com.owen2k6.chat.network.redux.packets.Packet1Chat;
import com.owen2k6.chat.network.redux.packets.Packet2Disconnect;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

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
            this.socket.setKeepAlive(true);
            this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace(System.err);
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

                    // this should be the only hardcoded packet handler, for AOT system command processing
                    if (packet instanceof Packet1Chat)
                    {
                        Packet1Chat chat = (Packet1Chat) packet;
                        if (chat.getMessage().startsWith("/"))
                        {
                            Server.getInstance().commandProcessor.processCommand(this, chat.getMessage());
                            continue;
                        }

                        if (!loggedIn) {
                            sendMessage("&cYou are not logged in!");
                            sendMessage("&cYou may log in locally using the login or register command.");
                            continue;
                        }// discard chats while not logged in
                    }

                    PacketHandler packetHandler = PacketRegistry.getPacketHandler(packetID).newInstance();
                    packetHandler.handlePacket(this, packet);
                }
            }
        } catch (Exception ex) {
            if (ex instanceof EOFException || ex instanceof SocketException) return;
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
        try
        {
            dos.flush();
        } catch (SocketException ex) {
            socket.close();
        }
    }

    public void disconnect() throws IOException
    {
        disconnect("Quit");
    }

    public void disconnect(String message)
    {
        try
        {
            sendPacket(new Packet2Disconnect(message));
            Server.getInstance().broadcastMessage(getUserInfo().username + " has disconnected", null);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public void setLoggedIn(boolean flag)
    {
        loggedIn = flag;
    }

    public boolean getLoggedIn()
    {
        return loggedIn;
    }

    public String toString()
    {
        return socket.toString();
    }

    public void loadData(String username) throws IOException {
        try (FileReader reader = new FileReader("data/users/" + username + ".json")) {
            userInfo = server.gson.fromJson(reader, user.class);
        }
    }

    public user getUserInfo() {
        return userInfo;
    }
}

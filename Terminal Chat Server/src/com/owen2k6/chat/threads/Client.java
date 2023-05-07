package com.owen2k6.chat.threads;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.account.Permissions;
import com.owen2k6.chat.account.user;
import com.owen2k6.chat.network.redux.AbstractPacket;
import com.owen2k6.chat.network.redux.packets.Packet1Chat;
import com.owen2k6.chat.server.channels;
import com.owen2k6.chat.server.servers;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import com.owen2k6.chat.configuration.motd;

public class Client extends Thread {
    private Server server;
    private Socket socket;
    private boolean loggedIn = false;
    public user userInfo;

    public servers currentserver;
    public channels currentchannel;
    public String bupUsername;
    public Client bupClient;
    private DataInputStream dis;
    private DataOutputStream dos;

    public Client(Server server, Socket socket) {
        this.socket = socket;
        this.server = server;
    }

    public String toString() {
        return socket.toString();
    }

    public void run() {
        while (socket.isConnected()) {
            try {
                dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                sendMessage(motd.getMOTD());
                boolean done = false;
                while (!done) {

                    String message = "";

                    try {
                        message = dis.readUTF();
                    } catch (EOFException e) {
                        //System.out.println("Client disconnected.");
                        break;
                    }

                    if (message.startsWith("/")) {
                        String[] parts = message.substring(1).split(" ");
                        switch (parts[0].toLowerCase()) {
                            default:
                                sendMessage("Invalid command.");
                                continue;
                            case "login":
                                if (loggedIn) {
                                    sendMessage("You are already logged in.");
                                    continue;
                                }
                                if (parts.length < 3) {
                                    sendMessage("Invalid command, usage: /login <username> <password>");
                                    continue;
                                }

                                if (server.login(parts[1], parts[2])) {
                                    loggedIn = true;
                                    loadData(parts[1]);
                                    sendMessage("You are now logged in.");
                                    Server.onlineUsers.add(parts[1]);
                                    bupUsername = this.userInfo.username;
                                    bupClient = this;
                                    try {
                                        server.broadcastMessage(parts[1] + " is now online.", null);
                                        server.broadcastMembersList(null);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    sendMessage("Invalid username or password.");
                                }


                                continue;
                            case "register":
                                if (loggedIn) {
                                    sendMessage("You are already logged in.");
                                    continue;
                                }
                                if (parts.length < 4) {
                                    sendMessage("Invalid command, usage: /register <username> <password> <confirm password>");
                                    continue;
                                }
                                if (!parts[2].equals(parts[3])) {
                                    sendMessage("Passwords do not match.");
                                    continue;
                                }

                                server.register(parts[1], parts[2]);
                                sendMessage("You are now registered, please login with /login <username> <password>.");
                                continue;
                            case "logout":
                                if (!loggedIn) {
                                    sendMessage("You must be logged in to do this, use /login or /register");
                                    continue;
                                }
                                if (loggedIn) {
                                    String username = this.userInfo.username;
                                    server.broadcastMessage(username + " Has gone offline", null);
                                    Server.onlineUsers.removeIf(s -> s.equals(username));
                                    server.broadcastMembersList(null);
                                    loggedIn = false;
                                }
                             continue;


                            case "whoami":
                                if (!loggedIn) {
                                    sendMessage("You must be logged in to do this, use /login or /register");
                                    continue;
                                }
                                sendMessage("Username: " + userInfo.username);
                                sendMessage("UUID: " + userInfo.uuid);
                                sendMessage("Permissions: " + userInfo.permissions);
                                continue;
                            case "bcast":
                                if (!loggedIn) {
                                    sendMessage("You must be logged in to do this, use /login or /register");
                                    continue;
                                }
                                if (!Permissions.hasPermission(userInfo.permissions, Permissions.GLOBAL_ANNOUNCE)) {
                                    sendMessage("No permission!");
                                    continue;
                                }
                                if (parts.length < 2) {
                                    sendMessage("Invalid command, usage: /bcast <message>");
                                    continue;
                                }
                                server.broadcastMessage("=== " + message.substring(7) + "===", null);
                                continue;
                            case "stop":
                                if (!loggedIn) {
                                    sendMessage("You must be logged in to do this, use /login or /register");
                                    continue;
                                }
                                if (!Permissions.hasPermission(userInfo.permissions, Permissions.STOP_SERVER)) {
                                    sendMessage("No permission!");
                                    continue;
                                }
                                if (parts.length > 1) {
                                    sendMessage("Invalid command, usage: /stop");
                                    continue;
                                }
                                done = true;
                                server.broadcastMessage("-> Server Stopping!", null);
                                System.exit(0);
                                continue;
                            case "new":
                                if (parts.length < 2) {
                                    sendMessage("Invalid command, usage: /new {sub-command}");
                                    continue;
                                }
                                if (parts.length >= 2) {
                                    if (parts[1].equals("server")) {
                                        if (!loggedIn) {
                                            sendMessage("You must be logged in to do this, use /login or /register");
                                            continue;
                                        }
                                        if (parts.length < 3) {
                                            sendMessage("Invalid command, usage: /new server {server name}");
                                            continue;
                                        }
                                        server.makeServer(parts[2], this.userInfo.uuid);
                                        continue;
                                    }
                                    if (parts[1].equals("group")) {
                                        if (!loggedIn) {
                                            sendMessage("You must be logged in to do this, use /login or /register");
                                            continue;
                                        }
                                        if (parts.length < 3) {
                                            sendMessage("Invalid command, usage: /new group {group name}");
                                            continue;
                                        }
                                    }
                                }

                                continue;
                            case "list":
                                sendMessage("Currently online members: ");
                                for (String username:Server.onlineUsers) {
                                    sendMessage(username);
                                }
                                continue;

                        }
                    } else {
                        if (!loggedIn) {
                            sendMessage("You must be logged in to send messages, use /login or /register");
                            continue;
                        }
                        System.out.println("Received message from client (" + this.userInfo.username + ") " + socket + ": " + message);

                        // Send the message to all connected clients
                        server.broadcastMessage("<" + this.userInfo.username + "> " + message, null);
                        //server.broadcastMessage("<" + userInfo.username + "> " + message, this);
                    }
                }
                dis.close();
                socket.close();
            } catch (SocketException f) {
                //System.out.println("Client disconnected");
                try {
                    String username = this.userInfo.username;
                    server.removeClient(this);
                    server.broadcastMessage(username + " Has gone offline", null);
                    Server.onlineUsers.removeIf(s -> s.equals(username));
                    server.broadcastMembersList(null);
                } catch (Exception e) {
                    try {
                        //server.removeClient(bupClient);
                        // TODO: fix this line  server.broadcastMessage(bupUsername + " Has gone offline", null);
                        Server.onlineUsers.removeIf(s -> s.equals(bupUsername));
                        // TODO: fix this line  server.broadcastMembersList(null);

                        System.out.println("Client unexpectedly disconnected.");
                    } catch (Exception ea) {
                        System.out.println("Client unexpectedly disconnected and the server has failed to remove user from list. " + ea);
                    }

                }
                //server.removeClient(this);
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
                //return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.removeClient(this);


    }

    public user getUserInfo() {
        return userInfo;
    }

    public void sendMessage(String message) throws IOException
    {
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        dos.writeUTF(message);
        dos.flush();
    }

    public void sendPacket(AbstractPacket packet) throws IOException
    {
        dos.writeInt(packet.getPacketID());
        packet.writeData(dos);
    }

    public void disconnect(String message) throws IOException
    {
        Packet1Chat chat = new Packet1Chat(message);
        sendPacket(chat);
    }

    public void loadData(String username) throws IOException {
        try (FileReader reader = new FileReader("data/users/" + username + ".json")) {
            userInfo = server.gson.fromJson(reader, user.class);
        }
    }
}
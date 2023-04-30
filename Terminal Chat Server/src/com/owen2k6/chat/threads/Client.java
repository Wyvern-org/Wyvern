package com.owen2k6.chat.threads;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.account.Permissions;
import com.owen2k6.chat.account.user;
import com.owen2k6.chat.server.channels;
import com.owen2k6.chat.server.servers;

import javax.print.DocFlavor;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

public class Client extends Thread {
    private Server server;
    private Socket socket;
    private boolean loggedIn = false;
    private user userInfo;

    public servers currentserver;
    public channels currentchannel;

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
                DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                sendMessage("Welcome to wyvern!");
                sendMessage("Please login or register with /login <username> <password> or /register <username> <password> <confirm password>");
                sendMessage("You can use /whoami to see your username, uuid and permissions when logged in.");
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
                                    try {
                                        server.broadcastMessage(parts[1] + " is now online.", null);
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
                            case "whoami":
                                if (!loggedIn) {
                                    sendMessage("You must be logged in to do this., use /login or /register");
                                    continue;
                                }
                                sendMessage("Username: " + userInfo.username);
                                sendMessage("UUID: " + userInfo.uuid);
                                sendMessage("Permissions: " + userInfo.permissions);
                                continue;
                            case "bcast":
                                if (!loggedIn) {
                                    sendMessage("You must be logged in to do this., use /login or /register");
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
                        }
                    } else {
                        if (!loggedIn) {
                            sendMessage("You must be logged in to send messages, use /login or /register");
                            continue;
                        }
                        System.out.println("Received message from client " + socket + ": " + message);

                        // Send the message to all connected clients
                        server.broadcastMessage("<" + this.userInfo.username + "> " + message, null);
                        //server.broadcastMessage("<" + userInfo.username + "> " + message, this);
                    }
                }
                dis.close();
                socket.close();
            } catch (SocketException f) {
                System.out.println("Client disconnected");
                try {
                    server.broadcastMessage(this.userInfo.username + " Has gone offline", null);
                } catch (IOException e) {
                    System.out.println("Client quit not logged in" );
                }
                server.removeClient(this);
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

    public void sendMessage(String message) throws IOException {
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        dos.writeUTF(message);
        dos.flush();
    }

    public void loadData(String username) throws IOException {
        try (FileReader reader = new FileReader("data/users/" + username + ".json")) {
            userInfo = server.gson.fromJson(reader, user.class);
        }
    }
}
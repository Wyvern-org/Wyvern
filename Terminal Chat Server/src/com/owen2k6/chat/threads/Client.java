package com.owen2k6.chat.threads;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.account.user;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client extends Thread {
    private Server server;
    private Socket socket;
    private boolean loggedIn = false;
    private user userInfo;

    public Client(Server server, Socket socket) {
        this.socket = socket;
        this.server = server;
    }

    public String toString()
    {
        return socket.toString();
    }

    public void run() {
        while (socket.isConnected()) {
            try {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                boolean done = false;
                while (!done) {
                    String message = dis.readUTF();

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
                                sendMessage("You are: " + userInfo.username);
                                continue;
                        }
                    } else {
                        if (!loggedIn) {
                            sendMessage("You must be logged in to send messages., use /login or /register");
                            continue;
                        }
                        System.out.println("Received message from client " + socket + ": " + message);

                        // Send the message to all connected clients
                        server.broadcastMessage("<" + userInfo.username + "> " + message, this);
                    }

                    done = message.equals("bye");
                }
                dis.close();
                socket.close();
            } catch (SocketException f) {
                System.out.println("Client disconnected");
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
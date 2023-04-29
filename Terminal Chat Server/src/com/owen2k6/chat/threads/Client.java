package com.owen2k6.chat.threads;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.account.user;

import java.io.*;
import java.net.Socket;

public class Client extends Thread
{
    private Server server;
    private Socket socket;
    private boolean loggedIn = false;
    private user userInfo;

    public Client(Server server, Socket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        while (socket.isConnected())
        {
            try {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                boolean done = false;
                while (!done) {
                    String message = dis.readUTF();

                    if (message.startsWith("/"))
                    {
                        String[] parts = message.substring(1).split(" ");
                        switch (parts[0].toLowerCase())
                        {
                            default:
                                dos.writeUTF("Invalid command.");
                                continue;
                            case "login":
                                // login
                                continue;
                            case "register":
                                // register
                                continue;
                            case "whoami":
                                if (!loggedIn) continue;
                                dos.writeUTF("You are: " + userInfo.username);
                                continue;
                        }
                    } else {
                        if (!loggedIn) continue;
                        System.out.println("Received message from client " + socket + ": " + message);

                        // Send the message to all connected clients
                        server.broadcastMessage(message, this);
                    }

                    done = message.equals("bye");
                }
                dis.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                server.removeClient(this);
            }
        }


    }

    public user getUserInfo()
    {
        return userInfo;
    }

    public void sendMessage(String message) throws IOException {
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        dos.writeUTF(message);
        dos.flush();
    }
}
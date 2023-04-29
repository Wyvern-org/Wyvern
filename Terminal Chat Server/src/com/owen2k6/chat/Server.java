package com.owen2k6.chat;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket server;
    private int port = 5600;
    private List<Socket> clients;

    public Server() {
        clients = new ArrayList<>();
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        System.out.println("Server started on port " + port);
        while (true) {
            try {
                // Accept a new client connection
                Socket client = server.accept();
                System.out.println("Client connected: " + client);
                clients.add(client);

                // Start a new thread to handle incoming messages from the client
                Thread thread = new Thread(() -> {
                    try {
                        DataInputStream dis = new DataInputStream(
                                new BufferedInputStream(client.getInputStream()));
                        boolean done = false;
                        while (!done) {
                            String message = dis.readUTF();
                            System.out.println("Received message from client " + client + ": " + message);

                            // Send the message to all connected clients
                            for (Socket c : clients) {
                                if (c != client) {
                                    DataOutputStream dos = new DataOutputStream(
                                            new BufferedOutputStream(c.getOutputStream()));
                                    dos.writeUTF("Client " + client + ": " + message);
                                    dos.flush();
                                }
                            }

                            done = message.equals("bye");
                        }
                        dis.close();
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        clients.remove(client);
                        System.out.println("Client disconnected: " + client);
                    }
                });
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

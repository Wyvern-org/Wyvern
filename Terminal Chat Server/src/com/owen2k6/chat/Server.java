package com.owen2k6.chat;
import com.google.gson.Gson;
import com.owen2k6.chat.account.user;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class Server {

    private ServerSocket server;
    private int port = 5600;
    private List<Socket> clients;

    Gson gson = new Gson();

    public byte[] generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return salt;
    }
    public boolean login(String username, String password) throws FileNotFoundException {
        user u = gson.fromJson(new FileReader("data/users/" + username + ".json"), user.class);
        String hash = hash(password, u.salt);
        return Objects.requireNonNull(hash).equals(u.password);
    }

    public boolean register(String username, String password) throws IOException {
        user u = new user();
        String saltString = Base64.getEncoder().encodeToString(generateSalt(16));

        u.username = username;
        u.password = hash(password, saltString);
        u.salt = saltString;
        gson.toJson(u, user.class, new FileWriter("data/users/" + username + ".json"));
        String hash = hash(password, u.salt);
        return Objects.requireNonNull(hash).equals(u.password);
    }

    public static String hash(String input, String salt)
    {
        try
        {
            Base64.Decoder dec = Base64.getDecoder();
            KeySpec spec = new PBEKeySpec(input.toCharArray(), dec.decode(salt), 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = f.generateSecret(spec).getEncoded();
            Base64.Encoder enc = Base64.getEncoder();
            return enc.encodeToString(hash);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

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
                                    dos.writeUTF("<username>: " + message);
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

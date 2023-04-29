package com.owen2k6.chat;
import com.google.gson.Gson;
import com.owen2k6.chat.account.user;
import com.owen2k6.chat.threads.Client;

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
    private List<Client> clients;


    public Gson gson = new Gson();

    public byte[] generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return salt;
    }
    public boolean login(String username, String password) throws IOException {
        try (FileReader reader = new FileReader("data/users/" + username + ".json")) {
            user u = gson.fromJson(reader, user.class);
            String hash = hash(password, u.salt);
            return Objects.requireNonNull(hash).equals(u.password);
        }
    }

    public boolean register(String username, String password) throws IOException {
        try (FileWriter writer = new FileWriter("data/users/" + username + ".json")) {
            user u = new user();
            String saltString = Base64.getEncoder().encodeToString(generateSalt(16));

            u.username = username;
            u.password = hash(password, saltString);
            u.salt = saltString;
            u.permission = 0;
            gson.toJson(u, user.class, writer);
            String hash = hash(password, u.salt);
            return Objects.requireNonNull(hash).equals(u.password);
        }
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
        String directoryName = "data/users";
        File directory = new File(directoryName);
        if (!directory.exists()) {
            System.out.println("Directory " + directoryName + " does not exist.");

            // Try to create the directory
            try {
                directory.mkdirs();
                System.out.println("Created directory " + directoryName);
            } catch (SecurityException e) {
                System.err.println("Unable to create directory " + directoryName);
                e.printStackTrace();
            }
        } else {
            System.out.println("Directory " + directoryName + " already exists.");
        }
        while (true) {
            try {
                Client client = new Client(this, server.accept());
                System.out.println("Client join: " + client);
                clients.add(client);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String message, Client src) throws IOException {
        for (Client c : clients)
        {
            if (src != null && c != src)
                c.sendMessage(message);
        }
    }

    public void removeClient(Client client) {
        System.out.println("Client quit: " + client);
        clients.remove(client);
    }
}

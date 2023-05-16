package com.owen2k6.chat;

import com.google.gson.Gson;
import com.owen2k6.chat.account.user;
import com.owen2k6.chat.commands.*;
import com.owen2k6.chat.event.ChatEvent;
import com.owen2k6.chat.event.EventSystem;
import com.owen2k6.chat.network.redux.PacketRegistry;
import com.owen2k6.chat.network.redux.handlers.ChatHandler;
import com.owen2k6.chat.network.redux.handlers.HandshakeHandler;
import com.owen2k6.chat.network.redux.packets.Packet0Handshake;
import com.owen2k6.chat.network.redux.packets.Packet1Chat;
import com.owen2k6.chat.server.channels;
import com.owen2k6.chat.server.servers;
import com.owen2k6.chat.server.groups;
import com.owen2k6.chat.threads.Client;
import com.owen2k6.chat.network.network;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.*;

import com.owen2k6.chat.configuration.motd;
import com.owen2k6.chat.threads.ClientRedux;

public class Server {

    public static EventSystem EVENT_SYSTEM = new EventSystem();
    private static Server singleton;
    public static Server getInstance()
    {
        return singleton;
    }

    private ServerSocket server;
    private int port = 5200;
    private List<Client> clients;
    private List<ClientRedux> reduxClients;

    public ArrayList<servers> servers;
    public ArrayList<channels> channels;
    public ArrayList<groups> groups;
    public static ArrayList<String> onlineUsers = new ArrayList<String>();
    public Gson gson = new Gson();
    public CommandProcessor commandProcessor = new CommandProcessor();

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
            UUID uuid = UUID.randomUUID();

            u.username = username;
            u.password = hash(password, saltString);
            u.salt = saltString;
            u.permissions = 0;
            u.uuid = uuid;
            gson.toJson(u, user.class, writer);
            String hash = hash(password, u.salt);
            return Objects.requireNonNull(hash).equals(u.password);
        }
    }

    public boolean makeServer(String name, UUID owner) throws IOException {
        String id = generateId();
        try (FileWriter writer = new FileWriter("data/servers/" + id + name + ".json")) {
            servers s = new servers();
            String saltString = Base64.getEncoder().encodeToString(generateSalt(16));

            s.name = name;
            s.id = id;
            s.owner = owner;
            s.serverinvitecode = generateRandomString(8);
            //s.members.add(owner);
            gson.toJson(s, servers.class, writer);
            save();
            return true;
        }
    }

    public boolean makeChannel(String name, servers serv) throws IOException {
        String id = generateId();
        try (FileWriter writer = new FileWriter("data/servers/channels" + id + name + ".json")) {
            channels c = new channels();
            String saltString = Base64.getEncoder().encodeToString(generateSalt(16));

            c.name = name;
            c.id = id;
            c.can_see.add(serv.owner);
            c.p = false;
            c.chat = true;
            c.belong = serv;
            gson.toJson(c, channels.class, writer);
            save();
            return true;
        }
    }

    public boolean makeGroup(String name, servers serv, UUID userToAdd) throws IOException {
        String id = generateId();
        try (FileWriter writer = new FileWriter("data/servers/groups" + id + name + ".json")) {
            groups g = new groups();
            String saltString = Base64.getEncoder().encodeToString(generateSalt(16));

            g.name = name;
            g.id = id;
            g.members.add(userToAdd);
            g.belong = serv;
            gson.toJson(g, groups.class, writer);
            save();
            return true;
        }
    }

    public boolean save() throws IOException {
        try (FileWriter writer = new FileWriter("data/settings/network.json")) {
            network n = new network();
            try {
                n.servers = servers;
            } catch (Exception e) {
                System.out.println("No servers to save");
            }
            try {
                n.channels = channels;
            } catch (Exception e) {
                System.out.println("No channels to save");
            }
            try {
                n.groups = groups;
            } catch (Exception e) {
                System.out.println("No groups to save");
            }
            gson.toJson(n, network.class, writer);
            return true;
        }
    }

    public boolean load() throws IOException {
        try {
            try (FileReader reader = new FileReader("data/settings/network.json")) {
                network n = gson.fromJson(reader, network.class);
                for (servers s : n.servers) {
                    servers a = new servers();
                    a.name = s.name;
                    a.id = s.id;
                    a.owner = s.owner;
                    a.serverinvitecode = s.serverinvitecode;
                    a.members.add(s.owner);
                    servers.add(s);
                    System.out.println("Loaded " + s.name);
                }
                for (channels c : n.channels) {
                    channels b = new channels();
                    b.name = c.name;
                    b.id = c.id;
                    b.can_see.addAll(c.can_see);
                    b.p = c.p;
                    b.chat = c.chat;
                    b.belong = c.belong;
                    channels.add(c);
                    System.out.println("Loaded " + c.name);
                }
                for (groups g : n.groups) {
                    groups xr = new groups();
                    xr.name = g.name;
                    xr.id = g.id;
                    g.members.addAll(g.members);
                    xr.belong = g.belong;
                    groups.add(g);
                    System.out.println("Loaded " + g.name);
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("network.json is empty.");
            return false;
        }
    }

    public static String generateId() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateTime = sdf.format(calendar.getTime());

        Random rand = new Random();
        int randomNum = rand.nextInt((10000 - 100) + 1) + 100;

        String id = dateTime + String.valueOf(randomNum);
        return id;
    }

    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static String hash(String input, String salt) {
        try {
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
        singleton = this;
        clients = new ArrayList<>();
        reduxClients = new ArrayList<>();
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }

    public void start() throws IOException {
        String filename = "data/settings/";
        String filername = "data/settings/network.json";
        File file = new File(filename);
        File filer = new File(filername);
        motd.load();
        if (!file.exists() || !filer.exists()) {
            // Try to create the directory
            try {
                file.mkdirs();
                filer.createNewFile();
                System.out.println("Created Settings folder.");
            } catch (SecurityException e) {
                System.err.println("Unable to create required files.");
                e.printStackTrace();
            }
        }
        if (filer.exists()) {
            try {
                load();
            } catch (Exception e) {
                System.out.println("network.json is empty.");
            }
            System.out.println("Server started on port " + port);
            String directoryName = "data/users";
            File directory = new File(directoryName);
            String directoryName1 = "data/servers";
            File directory1 = new File(directoryName1);
            String directoryName2 = "data/servers/channels";
            File directory2 = new File(directoryName2);
            String directoryName3 = "data/servers/groups";
            File directory3 = new File(directoryName3);
            if (!directory.exists() || !directory1.exists() || !directory2.exists() || !directory3.exists()) {
                // Try to create the directory
                try {
                    directory.mkdirs();
                    directory1.mkdirs();
                    directory2.mkdirs();
                    directory3.mkdirs();
                    System.out.println("Created File Structure");
                } catch (SecurityException e) {
                    System.err.println("Unable to create required files.");
                    e.printStackTrace();
                }
            }

            PacketRegistry.registerPacket(0, Packet0Handshake.class);
            PacketRegistry.registerPacketHandler(0, HandshakeHandler.class);

            PacketRegistry.registerPacket(1, Packet1Chat.class);
            PacketRegistry.registerPacketHandler(1, ChatHandler.class);

            commandProcessor.registerHandler("login", Login.class);
            commandProcessor.registerHandler("whoami", Whoami.class);
            commandProcessor.registerHandler("logout", Logout.class);
            commandProcessor.registerHandler("register", Register.class);
            commandProcessor.registerHandler("bcast", Bcast.class);

            while (true) {
                try {
                    //Client client = new Client(this, server.accept());
                    ClientRedux reduxClient = new ClientRedux(this, server.accept());
                    reduxClients.add(reduxClient);
                    System.out.println("Client join: " + reduxClient);
                    if(!System.getenv().containsKey("WYVERN_DEV")) { // NOTE TO DEVS: add this environment variable to your intellij configuration
                        sumbitglobalmessage("Wyvern Servers have not launched yet! #nPlease wait until the official launch is done. #nYou may self host a wyvern server while you wait #nhttps://github.com/wyvern-org/wyvern#n#nCopyright (c) Wyvern 2023");
                        reduxClient.disconnect("disallowed entry. Client will close itself");
                    }
                    //clients.add(client);
                    reduxClient.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void broadcastMessage(String message, ClientRedux src) throws IOException {
        for (ClientRedux c : reduxClients) {
            if (src != null && c != src)
                c.sendMessage(message);
            else if (src == null)
                c.sendMessage(message);
        }
    }

    public void broadcastMembersList(Client src) throws IOException {
        String message = "{OnlineList ";
        for (String username:Server.onlineUsers) {
            message = message + username + "\n";
        }

        for (Client c : clients) {
            try {
                if (src != null && c != src && Objects.equals(src.currentchannel.id, null) && Objects.equals(src.currentserver.id, null))
                    c.sendMessage(message);
                else if (src == null)
                    c.sendMessage(message);
            } catch (Exception e) {

            }

        }

    }

    public void sumbitglobalmessage(String message){
        for (ClientRedux c : reduxClients) {
            try {
                    c.sendMessage("{GLOBALALERT:" + message);
            } catch (Exception e) {
                //ignore

            }

        }
    }

    public void broadcastMessageToChannel(String message, Client src, channels channel, servers serv) throws
            IOException {
        for (Client c : clients) {
            if (src != null && c != src && Objects.equals(src.currentchannel.id, channel.id) && Objects.equals(src.currentserver.id, serv.id))
                c.sendMessage(message);
        }
    }

    public void removeClient(Client client) {
        System.out.println("Client quit: " + client);
        clients.remove(client);
    }
}

package com.owen2k6.chat.commands;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.threads.ClientRedux;

import java.io.IOException;

public class Register implements CommandHandler {

    @Override
    public void handleCommand(ClientRedux sender, String[] args) {
        if (sender.getLoggedIn()) {
            try {
                sender.sendMessage("You are already logged in.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (args.length < 2) {
            try {
                sender.sendMessage("This creates a local account to the wyvern server.");
                sender.sendMessage("Invalid command, usage: /register <username> <password> <confirm password>");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if(args[1] != args[2]) {
            try {
                sender.sendMessage("Passwords do not match!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        try {
            if (Server.getInstance().register(args[0], args[1])) {
                sender.sendMessage("You are now registered, please login with /login <username> <password>.");
            }else throw new IOException();

        } catch (IOException e) {
            try {
                sender.sendMessage("A serious error occoured");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}

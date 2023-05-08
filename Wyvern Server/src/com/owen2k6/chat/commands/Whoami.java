package com.owen2k6.chat.commands;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.threads.ClientRedux;

import java.io.IOException;

public class Whoami implements CommandHandler {
    @Override
    public void handleCommand(ClientRedux sender, String[] args) {
        if (!sender.getLoggedIn()) {
            try {
                sender.sendMessage("You are not logged in!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        try {
            sender.sendMessage("Username: " + sender.getUserInfo().username);
            sender.sendMessage("UUID: " + sender.getUserInfo().uuid);
            sender.sendMessage("Permissions: " + sender.getUserInfo().permissions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.owen2k6.chat.commands;

import com.owen2k6.chat.threads.ClientRedux;

public class Logout implements CommandHandler {
    @Override
    public void handleCommand(ClientRedux sender, String[] args) {
        if (!sender.getLoggedIn()) {
            try {
                sender.sendMessage("You are not logged in!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }
        try {
            sender.sendMessage("You are now logged out.");
            sender.setLoggedIn(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

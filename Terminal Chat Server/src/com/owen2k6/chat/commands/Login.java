package com.owen2k6.chat.commands;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.threads.ClientRedux;

import java.io.IOException;

public class Login implements CommandHandler
{
    @Override
    public void handleCommand(ClientRedux sender, String[] args)
    {
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
                sender.sendMessage("Invalid command, usage: /login <username> <password>");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        try {
            if (Server.getInstance().login(args[0], args[1])) {
                sender.setLoggedIn(true);
                sender.loadData(args[0]);
                sender.sendMessage("You are now logged in.");
                Server.getInstance().onlineUsers.add(args[0]);
                try {
                    Server.getInstance().broadcastMessage(args[0] + " is now online.", null);
                    Server.getInstance().broadcastMembersList(null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    sender.sendMessage("Invalid username or password.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

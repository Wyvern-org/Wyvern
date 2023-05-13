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
        if (parts.length < 3) {
            sendMessage("Invalid command, usage: /login <username> <password>");
            return;
        }

        if (server.login(parts[1], parts[2])) {
            loggedIn = true;
            loadData(parts[1]);
            sender.sendMessage("You are now logged in.");
            Server.onlineUsers.add(parts[1]);
            bupUsername = this.userInfo.username;
            bupClient = this;
            try {
                server.broadcastMessage(parts[1] + " is now online.", null);
                server.broadcastMembersList(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            sender.sendMessage("Invalid username or password.");
        }
    }
}

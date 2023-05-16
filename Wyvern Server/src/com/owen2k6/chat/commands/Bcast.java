package com.owen2k6.chat.commands;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.account.Permissions;
import com.owen2k6.chat.threads.ClientRedux;

import java.io.IOException;

public class Bcast implements CommandHandler
{
    @Override
    public void handleCommand(ClientRedux sender, String[] args)
    {
        if (!sender.getLoggedIn()) {
            try {
                sender.sendMessage("You must be logged in to do this, use /login or /register");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (!Permissions.hasPermission(sender.getUserInfo().permissions, Permissions.GLOBAL_ANNOUNCE)) {
            try {
                sender.sendMessage("No permission!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (args.length < 1) {
            try {
                sender.sendMessage("Invalid command, usage: /bcast <message>");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        try {
            Server.getInstance().broadcastMessage("=== " + args.toString() + "===", null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


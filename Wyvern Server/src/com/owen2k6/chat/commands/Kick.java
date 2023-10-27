package com.owen2k6.chat.commands;

import com.owen2k6.chat.account.Permissions;
import com.owen2k6.chat.threads.ClientRedux;

import java.io.IOException;

public class Kick implements CommandHandler
{
    @Override
    public void handleCommand(ClientRedux sender, String[] args)
    {
        try
        {
            if (!sender.getLoggedIn())
            {
                sender.sendMessage("You must be logged in to use this command!");
                return;
            }

            if (!Permissions.hasPermission(sender.getUserInfo().permissions, Permissions.KICK))
            {
                sender.sendMessage("No permission!");
                return;
            }

            if (args.length < 1)
            {
                sender.sendMessage("Invalid arguments! Usage: /kick <username> [reason]");
                return;
            }

            String username = args[0];
            String reason = args.length == 2 ? args[1] : "You have been kicked from the server!";


        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

    }
}

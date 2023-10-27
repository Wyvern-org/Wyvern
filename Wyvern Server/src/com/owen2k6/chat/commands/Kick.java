package com.owen2k6.chat.commands;

import com.owen2k6.chat.Server;
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
            String reason = "You have been kicked from the server!";
            if (args.length >= 2)
            {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++)
                    sb.append(args[i]).append(" ");
                reason = sb.toString().trim();
            }

            for (ClientRedux client : Server.getInstance().reduxClients)
            {
                if (client.getUserInfo().username.equalsIgnoreCase(username))
                {
                    client.disconnect(reason);
                    sender.sendMessage(username + " has been kicked!");
                    return;
                }
            }

            sender.sendMessage("Could not kick user: " + username);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}

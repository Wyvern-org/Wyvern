package com.owen2k6.chat.commands;

import com.owen2k6.chat.threads.ClientRedux;

import java.io.IOException;

public class Login implements CommandHandler
{
    @Override
    public void handleCommand(ClientRedux sender, String[] args)
    {
        try
        {
            sender.sendMessage("You ran the login command");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

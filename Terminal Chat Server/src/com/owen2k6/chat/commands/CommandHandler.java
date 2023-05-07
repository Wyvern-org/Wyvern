package com.owen2k6.chat.commands;

import com.owen2k6.chat.threads.ClientRedux;

public interface CommandHandler
{
    void handleCommand(ClientRedux sender, String[] args);
}

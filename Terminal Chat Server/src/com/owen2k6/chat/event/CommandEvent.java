package com.owen2k6.chat.event;

import com.owen2k6.chat.threads.Client;
import com.owen2k6.chat.threads.ClientRedux;

import java.util.Arrays;

public class CommandEvent
{
    public final String label;
    public final String[] args;
    public final ClientRedux sender;

    /**
     * Splits a raw CMD string input into variables for convenient processing
     * @param sender
     * @param raw eg., /cmd arg1 arg2 arg3
     */
    public CommandEvent(ClientRedux sender, String raw)
    {
        this.sender = sender;
        String pre = raw.substring(1);
        String[] parts = pre.split(" ");
        this.label = parts[0];
        this.args = Arrays.copyOfRange(parts, 1, parts.length);
    }
}

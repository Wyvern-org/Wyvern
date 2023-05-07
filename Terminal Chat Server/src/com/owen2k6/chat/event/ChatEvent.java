package com.owen2k6.chat.event;

import com.owen2k6.chat.threads.ClientRedux;

public class ChatEvent extends AbstractEvent
{
    private String message;
    private ClientRedux sender;

    public ChatEvent(ClientRedux sender, String message)
    {
        this.sender = sender;
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public ClientRedux getSender()
    {
        return sender;
    }
}

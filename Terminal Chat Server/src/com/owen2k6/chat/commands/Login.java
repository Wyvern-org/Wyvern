package com.owen2k6.chat.commands;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.event.CommandEvent;
import com.owen2k6.chat.event.EventListener;

import java.io.IOException;

public class Login implements EventListener
{
    @Override
    public void onEvent(String eventType, Object eventData)
    {
        try
        {
            CommandEvent cmd = (CommandEvent) eventData;


        } catch (Exception ex) {
            //TODO: better handling
            ex.printStackTrace();
        }
    }
}

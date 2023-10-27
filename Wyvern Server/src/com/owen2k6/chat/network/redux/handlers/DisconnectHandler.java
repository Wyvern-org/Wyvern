package com.owen2k6.chat.network.redux.handlers;

import com.owen2k6.chat.network.redux.AbstractPacket;
import com.owen2k6.chat.network.redux.PacketHandler;
import com.owen2k6.chat.threads.ClientRedux;

import java.io.IOException;

public class DisconnectHandler implements PacketHandler
{
    @Override
    public void handlePacket(ClientRedux client, AbstractPacket packet)
    {
        try
        {
            client.disconnect();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}

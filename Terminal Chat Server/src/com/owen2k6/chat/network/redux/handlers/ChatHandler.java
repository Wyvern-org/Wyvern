package com.owen2k6.chat.network.redux.handlers;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.event.CommandEvent;
import com.owen2k6.chat.network.redux.AbstractPacket;
import com.owen2k6.chat.network.redux.PacketHandler;
import com.owen2k6.chat.network.redux.packets.Packet1Chat;
import com.owen2k6.chat.threads.ClientRedux;

public class ChatHandler implements PacketHandler
{
    @Override
    public void handlePacket(ClientRedux client, AbstractPacket packet)
    {
        Packet1Chat chat = (Packet1Chat) packet;

        if (chat.getMessage().startsWith("/"))
        {
            CommandEvent cmd = new CommandEvent(client, chat.getMessage());
            Server.EVENT_SYSTEM.fireEvent("command", cmd);
            return;
        }

        try
        {
            Server.getInstance().broadcastMessage(chat.getMessage(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

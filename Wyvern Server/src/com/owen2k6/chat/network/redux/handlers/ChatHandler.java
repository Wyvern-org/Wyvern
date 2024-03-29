package com.owen2k6.chat.network.redux.handlers;

import com.owen2k6.chat.Server;
import com.owen2k6.chat.event.ChatEvent;
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

        try
        {
            System.out.println(client.getUserInfo().username + " >:> " + chat.getMessage());
            ChatEvent chatEvent = new ChatEvent(client, chat.getMessage());
            Server.EVENT_SYSTEM.fireEvent("chat", chatEvent);
            if (chatEvent.isCancelled())
                return;
            Server.getInstance().broadcastMessage(String.format("<%s> %s", client.getUserInfo().username, chat.getMessage()), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

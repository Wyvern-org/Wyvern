package com.owen2k6.chat.network.redux.handlers;

import com.owen2k6.chat.network.redux.AbstractPacket;
import com.owen2k6.chat.network.redux.packets.Packet0Handshake;
import com.owen2k6.chat.network.redux.PacketHandler;
import com.owen2k6.chat.threads.Client;

import java.io.IOException;

public class HandshakeHandler implements PacketHandler
{
    public void handlePacket(Client client, AbstractPacket packet)
    {
        try
        {
            Packet0Handshake handshakePacket = (Packet0Handshake) packet;

            if (handshakePacket.getProtocolVersion() < 1) // initial protocol version is 1
            {
                client.disconnect("Outdated client");
            } else {
                // nothing? idk lol
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

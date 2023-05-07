package com.owen2k6.chat.network.redux;

import com.owen2k6.chat.threads.Client;

public interface PacketHandler
{
    void handlePacket(Client client, AbstractPacket packet);
}

package com.owen2k6.chat.network.redux;

import com.owen2k6.chat.threads.Client;
import com.owen2k6.chat.threads.ClientRedux;

public interface PacketHandler
{
    void handlePacket(ClientRedux client, AbstractPacket packet);
}

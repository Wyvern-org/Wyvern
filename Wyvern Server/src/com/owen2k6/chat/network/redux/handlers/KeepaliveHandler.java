package com.owen2k6.chat.network.redux.handlers;

import com.owen2k6.chat.network.redux.AbstractPacket;
import com.owen2k6.chat.network.redux.PacketHandler;
import com.owen2k6.chat.threads.ClientRedux;
import com.owen2k6.chat.network.redux.packets.Packet2Keepalive;

public class KeepaliveHandler implements PacketHandler {

    @Override
    public void handlePacket(ClientRedux client, AbstractPacket packet) {
        Packet2Keepalive keepalivePacket = (Packet2Keepalive) packet;
        System.out.println("DEV: Received keepalive packet from server");
    }
}


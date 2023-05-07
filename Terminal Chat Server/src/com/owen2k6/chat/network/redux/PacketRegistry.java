package com.owen2k6.chat.network.redux;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry
{
    private static final Map<Integer, Class<? extends AbstractPacket>> packetMap = new HashMap<>();
    private static final Map<Integer, Class<? extends PacketHandler>> handlerMap = new HashMap<>();

    public static void registerPacket(int packetID, Class<? extends AbstractPacket> packetClass)
    {
        packetMap.put(packetID, packetClass);
    }

    public static Class<? extends AbstractPacket> getPacketClass(int packetID)
    {
        return packetMap.get(packetID);
    }

    public static void registerPacketHandler(int packetID, Class<? extends PacketHandler> packetHandler)
    {
        handlerMap.put(packetID, packetHandler);
    }

    public static Class<? extends PacketHandler> getPacketHandler(int packetID)
    {
        return handlerMap.get(packetID);
    }
}

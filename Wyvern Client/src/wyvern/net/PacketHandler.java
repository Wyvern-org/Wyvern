package wyvern.net;

import wyvern.Client;

public interface PacketHandler
{
    void handlePacket(NetworkManager networkManager, AbstractPacket packet);
}

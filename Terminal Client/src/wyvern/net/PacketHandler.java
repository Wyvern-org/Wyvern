package wyvern.net;

import wyvern.Client;

public interface PacketHandler
{
    void handlePacket(Client client, AbstractPacket packet);
}

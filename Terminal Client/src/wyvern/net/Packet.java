package wyvern.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Packet
{
    void readData(DataInputStream dis) throws IOException;
    void writeData(DataOutputStream dos) throws IOException;
}

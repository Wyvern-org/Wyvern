package com.owen2k6.chat.network.redux.packets;

import com.owen2k6.chat.network.redux.AbstractPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet2Keepalive extends AbstractPacket {
    private int keepaliveInterval = 5000; // 5 seconds

    public Packet2Keepalive() {
        super(2);
    }

    @Override
    public void readData(DataInputStream dis) throws IOException {
        // No additional data to read for a keepalive packet
    }

    @Override
    public void writeData(DataOutputStream dos) throws IOException {
        // No additional data to write for a keepalive packet
    }



    public void performAction() {
        // Logic for sending keepalive packets at regular intervals
        try {
            while (true) {
                // Send a keepalive packet to the server
                sendPacket();

                // Sleep for the keepalive interval
                Thread.sleep(keepaliveInterval);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendPacket() throws IOException {
        // Implement the code to send the keepalive packet to the server
    }
}

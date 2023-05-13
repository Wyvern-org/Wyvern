package wyvern;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class temporary_bypass_network {
    public static void main(String[] args) throws IOException {
        int port = 5100;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());

            // Start a new thread to handle the client connection
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            new Thread(clientHandler).start();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Welcome to wyvern!#nWyvern UAS Developers note:#nUAS IS NOT AVAILABLE.#nTHIS IS JUST A BYPASS TO CONFIRM ACCESS IS AVAILABLE.");

                clientSocket.close();
                System.out.println("Client booted since access was granted");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private String host = "localhost";
    private int port = 5600;

    public Client() {
        try {
            socket = new Socket(host, port);
            System.out.println("Connected to server: " + socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {
        try {
            // Set up input and output streams for sending and receiving messages to/from the server
            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));
            DataInputStream dis = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            // Start a new thread to handle incoming messages from the server
            Thread thread = new Thread(() -> {
                try {
                    boolean done = false;
                    while (!done) {
                        String message = dis.readUTF();
                        System.out.println(message);
                        done = message.equals("bye");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        dis.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            // Read user input from console and send it to the server
            Scanner scanner = new Scanner(System.in);
            boolean done = false;
            while (!done) {
                String message = scanner.nextLine();
                dos.writeUTF(message);
                dos.flush();
                done = message.equals("bye");
            }

            // Close the output stream and wait for the thread to finish
            dos.close();
            thread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

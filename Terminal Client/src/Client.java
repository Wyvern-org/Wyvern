import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Client {

    private Socket socket;
    private String host = "localhost";
    private int port = 5600;

    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private JScrollPane scrollPane;
    private JButton sendButton;


    public void createAndShowGUI() {
        // Set dark theme colors
        Color darkGray = new Color(40, 44, 52);
        Color lightGray = new Color(66, 72, 84);
        Color textColor = new Color(248, 248, 242);

        // Create the frame
        frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600)); // Set window size to 800x600
        frame.getContentPane().setBackground(darkGray);
        frame.setLayout(new BorderLayout());

        // Create the text area and add it to the frame
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(lightGray); // Set background color of text area
        textArea.setForeground(textColor); // Set text color of text area
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Set font of text field
        scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create the panel for the text field and send button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(darkGray);

        textField = new JTextField();
        textField.setBackground(lightGray); // Set background color of text field
        textField.setForeground(textColor); // Set text color of text field
        textField.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Set font of text field
        textField.setBorder(BorderFactory.createCompoundBorder(
                textField.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // Add padding to text field
        ));

        // Add key listener to text field to listen for Enter key press
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(textField.getText());
                textField.setText("");
            }
        });

        sendButton = new JButton("Send");
        sendButton.setBackground(lightGray); // Set background color of send button
        sendButton.setForeground(textColor); // Set text color of send button
        sendButton.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Set font of text field
        sendButton.setFocusPainted(false); // Remove focus border from send button

        // Add action listener to send button
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage(textField.getText());
                textField.setText("");
            }
        });

        // Add text field and send button to input panel
        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add input panel to frame
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Pack and show the frame
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }




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

        if (args.length > 0 && args[0].equals("-console")) {
            // Disable GUI and start in console-only mode
            client.startConsole();
        } else {
            // Enable GUI
            client.start();
        }
    }

    public void startConsole() {
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket(host, port);
            System.out.println("Connected to server: " + socket);

            Thread thread = new Thread(() -> {
                try {
                    boolean done = false;
                    DataInputStream dis = new DataInputStream(
                            new BufferedInputStream(socket.getInputStream()));
                    while (!done) {
                        String message = dis.readUTF();
                        System.out.println(message);
                        done = message.equals("-> Server Stopping!");
                    }
                    dis.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

            while (true) {
                String message = scanner.nextLine();
                sendMessage(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void sendMessage(String message) {
        try {
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            StringBuilder sb = new StringBuilder(textArea.getText());
            sb.append(message).append("\n");
            textArea.setText(sb.toString());
        });
    }

    public void start() {
        SwingUtilities.invokeLater(this::createAndShowGUI);
        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                System.out.println("Connected to server: " + socket);

                Thread thread = new Thread(() -> {
                    try {
                        boolean done = false;
                        DataInputStream dis = new DataInputStream(
                                new BufferedInputStream(socket.getInputStream()));
                        while (!done) {
                            String message = dis.readUTF();
                            receiveMessage(message);
                            done = message.equals("-> Server Stopping!");
                        }
                        dis.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


}
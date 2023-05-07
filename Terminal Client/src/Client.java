import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Client {

    //TODO: Protocols
    public int protocol = 1;
    public float version = 0.01f;

    //TODO: mod man this for you ;)


    private Socket socket;
    private String host = "dev.wyvern.owen2k6.com"; //initialised as a nullsafe.
    private String username = "";
    private String password = "";
    private String wyvernH = "dev.wyvern.owen2k6.com";
    private int port = 5600; // initialised as a nullsafe.
    private int wyvernP = 5600;

    private JFrame frame;
    private JFrame eframe; // hector
    private JFrame aframe;
    private JTextArea textArea;
    private JTextArea onlineList;
    private JTextField textField;
    private JScrollPane scrollPane;
    private JButton sendButton;
    public boolean allowrun = false;
    public boolean console = false;
    public String reason = "";

    private JTextField ipAddressField;
    private JTextField portField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;


    public void createAndShowGUI() {
        // Set dark theme colors
        Color darkGray = new Color(40, 44, 52);
        Color lightGray = new Color(66, 72, 84);
        Color textColor = new Color(248, 248, 242);

        // Create the frame
        frame = new JFrame("Wyvern Alpha - Welcome to Wyvern Chat!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600)); // Set window size to 800x600
        frame.getContentPane().setBackground(darkGray);
        frame.setLayout(new BorderLayout());
        //region About Button
        JButton aboutButton = new JButton("About");
        aboutButton.setFont(new Font("Monospaced", Font.PLAIN, 12));
        aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Wyvern Chat\n" +
                        "Version: " + version + "\n" +
                        "Protocol: " + protocol + "\n" +
                        "Host: " + host + "\n" +
                        "Port: " + port + "\n" +
                        "Thank you for using Wyvern Chat! \n\n" +
                        "Protected under the Owen2k6 Open Sourced Licence\n" +
                        "https://github.com/wyvern-org/wyvern-alpha", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        //endregion

        JComboBox<String> usera = new JComboBox<>();
        usera.setRenderer(new ComboBoxButtonRenderer());
        usera.setMaximumSize(new Dimension(100, 30));
        usera.setFont(new Font("Monospaced", Font.PLAIN, 12));
        usera.addItem("---User---");
        usera.addItem("Log In");
        usera.addItem("Log Out");
        usera.addItem("Register");

        usera.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(usera.getSelectedItem(), "User")) {
                    //do nothing
                }
                else if (Objects.equals(usera.getSelectedItem(), "Log In")) {
                    LoginPrompt();
                    usera.setSelectedIndex(0);
                } else if (Objects.equals(usera.getSelectedItem(), "Log Out")) {
                    sendMessage("/logout");
                    usera.setSelectedIndex(0);
                } else if (Objects.equals(usera.getSelectedItem(), "Register")) {
                    //RegisterPrompt();
                    usera.setSelectedIndex(0);
                }
                usera.setSelectedIndex(0);
            }
        });

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(darkGray);
        toolBar.setBorder(BorderFactory.createEmptyBorder());
        toolBar.setPreferredSize(new Dimension(toolBar.getPreferredSize().width, 17));
        toolBar.add(aboutButton);
        toolBar.add(usera);

        // Add the toolbar to the top of the panel
        frame.add(toolBar, BorderLayout.PAGE_START);

        // Create the text area and add it to the frame
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(lightGray); // Set background color of text area
        textArea.setForeground(textColor); // Set text color of text area
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Set font of text field
        textArea.setLineWrap(true); // Enable line wrapping
        textArea.setWrapStyleWord(true); // Enable word wrapping
        scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        onlineList = new JTextArea();
        onlineList.setEditable(false);
        onlineList.setBackground(lightGray); // Set background color of text area
        onlineList.setForeground(textColor); // Set text color of text area
        onlineList.setFont(new Font("Monospaced", Font.PLAIN, 16)); // Set font of text field
        scrollPane = new JScrollPane(onlineList);
        scrollPane.setPreferredSize(new Dimension(200, 600)); // Set the preferred size of the scroll pane
        frame.add(scrollPane, BorderLayout.LINE_END);

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

    public void ConnectionDialog() {
        eframe = new JFrame("Wyvern Chat - Alpha Test Client");
        // Set up the GUI components
        eframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        eframe.setSize(400, 300);
        eframe.setLocationRelativeTo(null); // Center on screen
        JLabel david = new JLabel("Leave blank to connect to Wyvern");
        JLabel ipAddressLabel = new JLabel("Custom IP Address:");
        JLabel portLabel = new JLabel("Custom Port Number:");
        ipAddressField = new JTextField();
        portField = new JTextField();

        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(e -> {
            host = ipAddressField.getText().trim();
            try {
                port = Integer.parseInt(portField.getText().trim());
            } catch (IllegalArgumentException f) {
                //ignore
                port = 5600;
            }
            allowrun = true;
            try {
                if (portField.getText().isEmpty())
                    port = wyvernP;
                if (ipAddressField.getText().isEmpty())
                    host = wyvernH;

                socket = new Socket(host, port);
            } catch (IOException ex) {
                allowrun = false;
                if (port == wyvernP && Objects.equals(host, wyvernH)) {
                    JOptionPane.showMessageDialog(null, "The Wyvern servers are currently unavailable at this time.\n" + ex.getMessage() + "\nPlease try again later.", "Alert!", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to connect to server: \n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (allowrun) {
                eframe.dispose();
                start();
                JOptionPane.showMessageDialog(null, "Please keep in mind that this is an open testing phase.\n" +
                        "We are not yet ready for a full userbase to register yet\n" +
                        "Please report all bugs found!\n" +
                        "Enjoy the testing phase of Wyvern Chat!", "Welcome to Wyvern", JOptionPane.INFORMATION_MESSAGE);
            } else {
                eframe.dispose();
                ConnectionDialog();
            }
            eframe.dispose();
        });

        // Add the components to the layout
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(david)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(ipAddressLabel)
                                .addComponent(ipAddressField))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(portLabel)
                                .addComponent(portField))
                        .addComponent(connectButton))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(david)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(ipAddressLabel)
                        .addComponent(ipAddressField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(portLabel)
                        .addComponent(portField))
                .addComponent(connectButton)
        );

        eframe.getContentPane().add(panel);
        eframe.pack();
        eframe.setVisible(true);
    }

    public void LoginPrompt() {
        aframe = new JFrame("Wyvern Chat Pop-Out");
        // Set up the GUI components
        aframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        aframe.setSize(400, 300);
        aframe.setPreferredSize(new Dimension(400, 150));
        aframe.setLocationRelativeTo(null); // Center on screen
        JLabel bbbbbb = new JLabel("Login to Wyvern Chat!");
        JLabel aaaaaa = new JLabel("Username:");
        JLabel cccccc = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton ohyeah = new JButton("Login");
        ohyeah.addActionListener(e -> {
            sendMessage("/login " + usernameField.getText().trim() + " " + passwordField.getText().trim());
            aframe.dispose();
        });

        // Add the components to the layout
        JPanel aspanel = new JPanel();
        GroupLayout aslayout = new GroupLayout(aspanel);
        aspanel.setLayout(aslayout);

        aslayout.setAutoCreateGaps(true);
        aslayout.setAutoCreateContainerGaps(true);

        aslayout.setHorizontalGroup(aslayout.createSequentialGroup()
                .addGroup(aslayout.createParallelGroup()
                        .addComponent(bbbbbb)
                        .addGroup(aslayout.createSequentialGroup()
                                .addComponent(aaaaaa)
                                .addComponent(usernameField))
                        .addGroup(aslayout.createSequentialGroup()
                                .addComponent(cccccc)
                                .addComponent(passwordField))
                        .addComponent(ohyeah))
        );

        aslayout.setVerticalGroup(aslayout.createSequentialGroup()
                .addComponent(bbbbbb)
                .addGroup(aslayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(aaaaaa)
                        .addComponent(usernameField))
                .addGroup(aslayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(cccccc)
                        .addComponent(passwordField))
                .addComponent(ohyeah)
        );

        aframe.getContentPane().add(aspanel);
        aframe.pack();
        aframe.setVisible(true);
    }


    public Client() {
        if (console) {
            System.out.println("Connecting to server...");
        } else {
            connect();
        }
        //socket = new Socket(host, port); // Better call saul
        //System.out.println("Connected to server: " + socket);
    }

    public static void main(String[] args) {
        Client client = new Client();
        if (client.allowrun) {
            if (args.length > 0 && args[0].equals("-console")) {
                // Disable GUI and start in console-only mode
                client.console = true;
                client.startConsole();
            } else {
                // Enable GUI
                client.console = false;

            }
        } else {

            if (args.length > 0 && args[0].equals("-console")) {
                System.out.println("Wyvern Console is deprecated! You are unable to launch this in a release enviorment.");
            } else {

            }
        }
    }

    @Deprecated
    public void startConsole() {
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket(host, port);
            System.out.println("Connected to server: " + socket);
            System.out.println("Wyvern Console is being phased out! This wont be a thing on release!");

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
        //System.out.println(message);
        if (!message.startsWith("{")) {
            String formattedMessage = format(message); // Apply color formatting
            SwingUtilities.invokeLater(() -> {
                StringBuilder sb = new StringBuilder(textArea.getText());
                sb.append(formattedMessage).append("\n");
                textArea.setText(sb.toString());
            });
        } else {
            if (message.startsWith("{OnlineList")) {
                String msgsplit = message.split(" ")[1];

                String formattedMessage = format(msgsplit); // Apply color formatting
                SwingUtilities.invokeLater(() -> {
                    onlineList.setText("");
                    StringBuilder sb = new StringBuilder(onlineList.getText());
                    sb.append(formattedMessage).append("\n");
                    onlineList.setText(sb.toString());
                });
            }
        }
    }

    public void connect() {
        SwingUtilities.invokeLater(this::ConnectionDialog);
    }


    public static String format(String message) {
        message = message.replaceAll("&0", "\u001b[30m"); // black
        message = message.replaceAll("&1", "\u001b[34m"); // dark blue
        message = message.replaceAll("&2", "\u001b[32m"); // dark green
        message = message.replaceAll("&3", "\u001b[36m"); // dark aqua
        message = message.replaceAll("&4", "\u001b[31m"); // dark red
        message = message.replaceAll("&5", "\u001b[35m"); // dark purple
        message = message.replaceAll("&6", "\u001b[33m"); // gold
        message = message.replaceAll("&7", "\u001b[37m"); // gray
        message = message.replaceAll("&8", "\u001b[30;1m"); // dark gray
        message = message.replaceAll("&9", "\u001b[34;1m"); // blue
        message = message.replaceAll("&a", "\u001b[32;1m"); // green
        message = message.replaceAll("&b", "\u001b[36;1m"); // aqua
        message = message.replaceAll("&c", "\u001b[31;1m"); // red
        message = message.replaceAll("&d", "\u001b[35;1m"); // light purple
        message = message.replaceAll("&e", "\u001b[33;1m"); // yellow
        message = message.replaceAll("&f", "\u001b[37;1m"); // white
        message = message.replaceAll("&r", "\u001b[0m"); // reset
        return message;
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

class ComboBoxButtonRenderer extends JButton implements ListCellRenderer<String> {

    public ComboBoxButtonRenderer() {
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setBorderPainted(false);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value);
        setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
        setForeground(isSelected ? Color.WHITE : Color.BLACK);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        return this;
    }
}
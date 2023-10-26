package wyvern.ui;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import wyvern.Redux;
import wyvern.net.packets.Packet1Chat;
import wyvern.util.ColorMap;

import java.io.IOException;

public class MainController extends WyvernController
{
    @FXML public TextField chatBox; //Pushing to get above upstream
    @FXML public Button sendButton;
    @FXML public ListView<String> onlineUsersList;
    @FXML public TextFlow chatLog;

    @FXML public Label statuscon; // server status (online or offline or reconnecting)
    @FXML public Label uol; // users online text.

    @FXML protected void initialize()
    {
        sendButton.setOnAction((event) ->
                sendAndClear());

        chatBox.setOnKeyPressed(event ->
        {
            if (event.getCode() == KeyCode.ENTER)
                sendAndClear();
        });
    }

    private void sendAndClear()
    {
        try
        {
            Packet1Chat chat = new Packet1Chat(chatBox.getText());
            Redux.getInstance().getNetworkManager().sendPacket(chat);
            chatBox.clear();
        } catch (IOException e) {
            Redux.getInstance().alert(Alert.AlertType.ERROR, "Oops!", "Failed to send chat: " + e.getMessage());
        }
    }

    public void appendToChatLog(String message)
    {
        TextFlow textFlow = new TextFlow();
        StringBuilder plainText = new StringBuilder();
        boolean formatting = false;

        for (int i = 0; i < message.length(); i++)
        {
            char c = message.charAt(i);
            if (c == '&' && i + 1 < message.length())
            {
                char code = message.charAt(i + 1);
                if (ColorMap.MINECRAFT_COLORS.containsKey(code))
                {
                    if (plainText.length() > 0)
                    {
                        Text plain = new Text(plainText.toString());
                        textFlow.getChildren().add(plain);
                        plainText = new StringBuilder();
                    }
                    formatting = true;
                    i++;
                    continue;
                }
            }
            if (formatting)
            {
                Text text = new Text(Character.toString(c));
                text.setFill(ColorMap.getColorFromMinecraftCode(message.charAt(i - 1)));
                textFlow.getChildren().add(text);
            } else plainText.append(c);
        }

        if (plainText.length() > 0)
        {
            Text plain = new Text(plainText.toString());
            textFlow.getChildren().add(plain);
        }

        chatLog.getChildren().add(new Text(message + "\n"));
    }
}

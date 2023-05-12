package wyvern.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import wyvern.Redux;

public class MainController extends WyvernController
{
    @FXML protected TextField chatBox;
    @FXML protected Button sendButton;
    @FXML protected ListView<String> onlineUsersList;

    @FXML protected void initialize()
    {
        setupButtonAnimation(sendButton, 1.1);

        sendButton.setOnAction((event) ->
        {
            //
        });
    }
}

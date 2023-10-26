package wyvern.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class WyvernController
{



    protected void scaleTransition(Node node, double toScale)
    {
        scaleTransition(node, toScale, 200);
    }

    protected void scaleTransition(Node node, double toScale, double durationMillis)
    {
        Duration duration = Duration.millis(durationMillis);
        ScaleTransition scaleTransition = new ScaleTransition(duration, node);
        scaleTransition.setToX(toScale);
        scaleTransition.setToY(toScale);
        scaleTransition.play();
    }

    protected void fadeTransition(Node node, double toOpacity)
    {
        fadeTransition(node, toOpacity, 200);
    }

    protected void fadeTransition(Node node, double toOpacity, double durationMillis)
    {
        Duration duration = Duration.millis(durationMillis);
        FadeTransition transition = new FadeTransition(duration, node);
        transition.setToValue(toOpacity);
        transition.play();
    }
}

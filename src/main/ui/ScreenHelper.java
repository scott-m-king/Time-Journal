package ui;

import javafx.stage.Stage;

import java.awt.*;

public class ScreenHelper {
    // MODIFIES: this
    // EFFECTS: sets mainStage dimensions to set width and height
    public void setMainStageDimensions(UserInterface userInterface) {
        userInterface.setScreenSize(Toolkit.getDefaultToolkit().getScreenSize());
        userInterface.getMainStage().setWidth(UserInterface.WINDOW_WIDTH);
        userInterface.getMainStage().setHeight(UserInterface.WINDOW_HEIGHT);
        userInterface.getMainStage().setMinWidth(UserInterface.WINDOW_WIDTH);
        userInterface.getMainStage().setMinHeight(UserInterface.WINDOW_HEIGHT);
        setMiddle(userInterface.getMainStage(), userInterface);
    }

    // MODIFIES: object that calls this method
    // EFFECTS: sets stage to middle of screen depending on device's screen resolution
    public void setMiddle(Stage s, UserInterface userInterface) {
        double middleCoordinateX = userInterface.getScreenSize().getWidth() / 2;
        double middleCoordinateY = userInterface.getScreenSize().getHeight() / 2;
        double subtractWindowSizeX = s.getWidth() / 2;
        double subtractWindowSizeY = s.getHeight() / 2;
        s.setX(middleCoordinateX - subtractWindowSizeX);
        s.setY(middleCoordinateY - subtractWindowSizeY);
    }
}

package ui;

import javafx.stage.Stage;

import java.awt.*;

// Represents helper class to set the dimensions of the main stage and to help other stages center to the user's screen
public class StageHelper {
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final int TITLE_FONT_SIZE = 35;
    private Dimension screenSize;

    public StageHelper() {
    }

    // MODIFIES: UserInterface
    // EFFECTS: sets mainStage dimensions to set width and height
    public void setMainStageDimensions(UserInterface userInterface) {
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        userInterface.getMainStage().setWidth(WINDOW_WIDTH);
        userInterface.getMainStage().setHeight(WINDOW_HEIGHT);
        userInterface.getMainStage().setMinWidth(WINDOW_WIDTH);
        userInterface.getMainStage().setMinHeight(WINDOW_HEIGHT);
        setMiddle(userInterface.getMainStage());
    }

    // MODIFIES: object that calls this method
    // EFFECTS: sets stage to middle of screen depending on device's screen resolution
    public void setMiddle(Stage s) {
        double middleCoordinateX = screenSize.getWidth() / 2;
        double middleCoordinateY = screenSize.getHeight() / 2;
        double subtractWindowSizeX = s.getWidth() / 2;
        double subtractWindowSizeY = s.getHeight() / 2;
        s.setX(middleCoordinateX - subtractWindowSizeX);
        s.setY(middleCoordinateY - subtractWindowSizeY);
    }
}

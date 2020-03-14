package ui.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.UserInterface;

public class NewUserWelcomeScreen extends Screen {
    private final UserInterface userInterface;
    private Label title;
    private Button newUserButton;
    private Pane pane;

    public NewUserWelcomeScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderNewUserWelcomeScreen() {
        setLabel();
        setButton();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
        userInterface.getMainStage().show();
    }

    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(title, newUserButton);
        pane = vbox;
    }

    public void setButton() {
        newUserButton = new Button(">");
        newUserButton.setStyle("-fx-min-width: 75;");
        newUserButton.setOnAction(e -> userInterface.newUserNameScreen());
    }

    public void setLabel() {
        title = new Label("Welcome to Time Journal");
        title.setStyle("-fx-font-size: 60px; -fx-text-fill: #383838;");
        title.setPadding(new Insets(0, 0, 75, 0));
    }
}
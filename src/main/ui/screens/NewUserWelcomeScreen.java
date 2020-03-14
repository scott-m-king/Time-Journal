package ui.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ui.UserInterface;

public class NewUserWelcomeScreen {
    private final UserInterface userInterface;

    public NewUserWelcomeScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void newUserWelcomeScreen() {
        Label title = newUserWelcomeScreenLabel();
        Button newUserButton = newUserWelcomeScreenButton();
        initializeNewUserWelcomeScreen(title, newUserButton);
    }

    public void initializeNewUserWelcomeScreen(Label title, Button newUserButton) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(title, newUserButton);
        userInterface.initializeScene(vbox, userInterface.getMainStage());
    }

    public Button newUserWelcomeScreenButton() {
        Button newUserButton = new Button(">");
        newUserButton.setStyle("-fx-min-width: 75;");
        newUserButton.setOnAction(e -> userInterface.newUserNameScreen());
        return newUserButton;
    }

    public Label newUserWelcomeScreenLabel() {
        Label title = new Label("Welcome to Time Journal");
        title.setStyle("-fx-font-size: 60px; -fx-text-fill: #383838;");
        title.setPadding(new Insets(0, 0, 75, 0));
        return title;
    }
}
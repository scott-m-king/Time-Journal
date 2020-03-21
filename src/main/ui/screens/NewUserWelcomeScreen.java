package ui.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
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


    // REQUIRES: valid UserSession
    // MODIFIES: this
    // EFFECTS: runs methods needed to render new user welcome screen
    public void renderNewUserWelcomeScreen() {
        setLabel();
        setButton();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
        userInterface.getMainStage().show();
        setEnterListener();
    }

    // MODIFIES: this
    // EFFECTS: populates final vbox pane to load to scene
    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(title, newUserButton);
        pane = vbox;
    }

    // MODIFIES: this
    // EFFECTS: sets continue button
    public void setButton() {
        newUserButton = new Button(">");
        newUserButton.setStyle("-fx-min-width: 75;");
        newUserButton.setOnAction(e -> userInterface.getNewUserNameScreen().renderNewUserNameScreen());
    }

    // MODIFIES: this
    // EFFECTS: sets and aligns main screen label
    public void setLabel() {
        title = new Label("Welcome to Time Journal");
        title.setStyle("-fx-font-size: 60px; -fx-text-fill: #383838;");
        title.setPadding(new Insets(0, 0, 75, 0));
    }

    // MODIFIES: this
    // EFFECTS: sets event listener for enter key
    private void setEnterListener() {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                userInterface.getNewUserNameScreen().renderNewUserNameScreen();
            }
        });
    }
}
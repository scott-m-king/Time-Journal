package ui.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.UserInterface;

// Represents the welcome screen that appears if the program detects there is least one save file is present
public class WelcomeScreen extends Screen {
    private final UserInterface userInterface;
    private Pane pane;

    public WelcomeScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // EFFECTS: runs methods needed to initialize this screen
    public void renderWelcomeScreen() {
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
        userInterface.getMainStage().show();
    }

    // MODIFIES: this
    // EFFECTS: creates final gridpane pane to display all elements in scene, sets buttons and labels
    @Override
    protected void initializeFinalPane() {
        Label title = setPageLabel();
        Button newUserButton = newUserButton();
        Button returningUserButton = returningUserButton();
        GridPane gridPane = setGridPane(newUserButton, returningUserButton);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(title, gridPane);
        pane = vbox;
    }

    // EFFECTS: returns main page label
    public Label setPageLabel() {
        Label title = new Label("Time Journal");
        title.setStyle("-fx-font-size: 70px; -fx-text-fill: #383838;");
        title.setPadding(new Insets(0, 0, 85, 0));
        return title;
    }

    // EFFECTS: returns gridpane with button listeners
    public GridPane setGridPane(Button newUserButton, Button returningUserButton) {
        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);
        setButtonListeners(newUserButton, returningUserButton);
        grid.setAlignment(Pos.CENTER);
        grid.getChildren().addAll(newUserButton, returningUserButton);
        return grid;
    }

    // MODIFIES: this
    // EFFECTS: sets button listeners and triggers page load
    public void setButtonListeners(Button newUserButton, Button returningUserButton) {
        newUserButton.setOnAction(e -> userInterface.getNewUserWelcomeScreen().renderNewUserWelcomeScreen());
        returningUserButton.setOnAction(e -> userInterface.getUserSelectScreen().renderUserSelectScreen());
    }

    // EFFECTS: returns returning user button
    public Button returningUserButton() {
        Button returningUserButton = new Button("Returning User");
        returningUserButton.setMinWidth(100);
        GridPane.setConstraints(returningUserButton, 0, 1);
        return returningUserButton;
    }

    // EFFECTS: returns new user button
    public Button newUserButton() {
        Button newUserButton = new Button("New User");
        newUserButton.setMinWidth(100);
        GridPane.setConstraints(newUserButton, 0, 0);
        return newUserButton;
    }
}
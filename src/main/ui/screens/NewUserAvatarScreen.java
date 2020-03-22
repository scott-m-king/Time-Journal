package ui.screens;

import exceptions.NullEntryException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.UserInterface;

// Represents a screen to let the user choose a personal avatar. Uses the AvatarPickerComponent to display media
public class NewUserAvatarScreen extends Screen {
    private final UserInterface userInterface;
    private Label nameLabel;
    private Button newUserButton;
    private GridPane imagePane;
    private Pane pane;

    public NewUserAvatarScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // MODIFIES: this
    // EFFECTS: runs methods needed to initialize this screen
    public void renderNewUserAvatarScreen() {
        setScreenLabel();
        imagePane = userInterface.getAvatarPickerComponent().renderAvatarPicker();
        setSubmitButton();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
        setEnterListener();
    }

    // MODIFIES: this
    // EFFECTS: creates vbox as final pane to display visual components for screen
    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(nameLabel, imagePane, newUserButton);
        pane = vbox;
    }

    // MODIFIES: this
    // EFFECTS: creates and aligns submit button to middle of screen
    public void setSubmitButton() {
        newUserButton = new Button(">");
        newUserButton.setStyle("-fx-min-width: 75;");
        newUserButton.setAlignment(Pos.CENTER);
        setSubmitButtonListener();
    }

    // MODIFIES: this
    // EFFECTS: creates and aligns main label to middle of screen
    public void setScreenLabel() {
        nameLabel = new Label("Choose an avatar");
        nameLabel.setStyle("-fx-font-size: 45px; -fx-text-fill: #383838;");
        nameLabel.setAlignment(Pos.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: tries to set UserSession avatar to chosen avatar, displays alert if nothing selected
    private void submitAvatar() {
        try {
            userInterface.getCurrentSession().setCurrentUser(userInterface.getNewUserNameScreen().getUserName());
            userInterface.getCurrentSession().newSession();
            userInterface.getCurrentSession().setUserAvatar(
                    userInterface
                    .getAvatarPickerComponent()
                    .getSelectedAvatarImageURL());
            userInterface.getFirstNewCategoryScreen().renderFirstNewCategoryScreen();
        } catch (NullEntryException exception) {
            nullEntryAlert();
        }
    }

    // EFFECTS: alerts user if avatar was not selected
    private void nullEntryAlert() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Please choose an avatar!");
        a.show();
    }

    // MODIFIES: this
    // EFFECTS: sets button listener on main page
    public void setSubmitButtonListener() {
        newUserButton.setOnAction(e -> {
            submitAvatar();
        });
    }

    // MODIFIES: this
    // EFFECTS: sets event listener for enter key
    private void setEnterListener() {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitAvatar();
            }
        });
    }
}
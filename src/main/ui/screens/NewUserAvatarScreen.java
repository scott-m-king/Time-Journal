package ui.screens;

import exceptions.NullEntryException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.UserInterface;

public class NewUserAvatarScreen extends Screen {
    private final UserInterface userInterface;
    private Label nameLabel;
    private Button newUserButton;
    private GridPane imagePane;
    private Pane pane;

    public NewUserAvatarScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderNewUserAvatarScreen() {
        setScreenLabel();
        setSubmitButton();
        imagePane = userInterface.getAvatarPickerComponent().renderAvatarPicker();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
    }

    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(nameLabel, imagePane, newUserButton);
        pane = vbox;
    }

    public void setSubmitButton() {
        newUserButton = new Button(">");
        newUserButton.setStyle("-fx-min-width: 75;");
        newUserButton.setAlignment(Pos.CENTER);
        setSubmitButtonListener();
    }

    public void setScreenLabel() {
        nameLabel = new Label("Choose an avatar below:");
        nameLabel.setStyle("-fx-font-size: 45px; -fx-text-fill: #383838;");
        nameLabel.setAlignment(Pos.CENTER);
    }

    public void setSubmitButtonListener() {
        newUserButton.setOnAction(e -> {
            try {
                userInterface.getSession().setCurrentUser("Test");
                userInterface.getSession().newSession();
                userInterface.getFirstNewCategoryScreen().renderFirstNewCategoryScreen();
            } catch (NullEntryException exception) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("You must enter at least one character for your name.");
                a.show();
            }
        });
    }
}
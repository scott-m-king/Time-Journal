package ui.screens;

import exceptions.NullEntryException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.UserInterface;

public class NewUserNameScreen extends Screen {
    private final UserInterface userInterface;
    private Label nameLabel;
    private TextField name;
    private Button newUserButton;
    private Pane pane;

    public NewUserNameScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderNewUserNameScreen() {
        setScreenLabel();
        setTextField();
        setSubmitButton();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
    }

    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(nameLabel, name, newUserButton);
        pane = vbox;
    }

    public void setSubmitButton() {
        newUserButton = new Button(">");
        newUserButton.setStyle("-fx-min-width: 75;");
        newUserButton.setAlignment(Pos.CENTER);
        setSubmitButtonListener(newUserButton, name);
    }

    public void setTextField() {
        name = new TextField();
        name.setMaxWidth(300);
        name.setStyle("-fx-font-size: 20px;");
        name.setAlignment(Pos.CENTER);
    }

    public void setScreenLabel() {
        nameLabel = new Label("What's your name? Enter below: ");
        nameLabel.setStyle("-fx-font-size: 45px; -fx-text-fill: #383838;");
        nameLabel.setAlignment(Pos.CENTER);
    }

    public void setSubmitButtonListener(Button newUserButton, TextField name) {
        newUserButton.setOnAction(e -> {
            try {
                userInterface.getSession().setCurrentUser(name.getText());
                userInterface.getSession().newSession();
                userInterface.firstNewCategory();
            } catch (NullEntryException exception) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("You must enter at least one character for your name.");
                a.show();
            }
        });
    }
}
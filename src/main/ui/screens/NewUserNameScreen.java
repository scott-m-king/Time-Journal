package ui.screens;

import exceptions.NullEntryException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ui.UserInterface;

public class NewUserNameScreen extends Screen {
    private final UserInterface userInterface;
    private Label nameLabel;
    private TextField userName;
    private Button newUserButton;
    private Pane pane;

    public NewUserNameScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void renderNewUserNameScreen() {
        setScreenLabel();
        setTextField();
        setSubmitButton();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
        setEnterListener();
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(nameLabel, userName, newUserButton);
        pane = vbox;
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void setSubmitButton() {
        newUserButton = new Button(">");
        newUserButton.setStyle("-fx-min-width: 75;");
        newUserButton.setAlignment(Pos.CENTER);
        setSubmitButtonListener(newUserButton);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void setTextField() {
        userName = new TextField();
        userName.setMaxWidth(300);
        userName.setStyle("-fx-font-size: 20px;");
        userName.setAlignment(Pos.CENTER);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void setScreenLabel() {
        nameLabel = new Label("What's your name? Enter below: ");
        nameLabel.setStyle("-fx-font-size: 45px; -fx-text-fill: #383838;");
        nameLabel.setAlignment(Pos.CENTER);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void submitForm() {
        try {
            userInterface.getCurrentSession().setCurrentUser(userName.getText());
            userInterface.getNewUserAvatarScreen().renderNewUserAvatarScreen();
        } catch (NullEntryException exception) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("You must enter at least one character for your name.");
            a.show();
        }
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void setSubmitButtonListener(Button newUserButton) {
        newUserButton.setOnAction(e -> submitForm());
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void setEnterListener() {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitForm();
            }
        });
    }

    // Getter
    public String getUserName() {
        return userName.getText();
    }
}
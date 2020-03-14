package ui.screens;

import exceptions.NullEntryException;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import ui.UserInterface;

import java.util.ArrayList;

public class UserSelectScreen extends Screen {
    private final UserInterface userInterface;
    private Label title;
    private ComboBox<String> comboBox;
    private Button submit;
    private Pane pane;

    public UserSelectScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderUserSelectScreen() {
        setLabel();
        setDropDownList();
        setSubmitButton();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
    }

    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.setSpacing(50);
        vbox.getChildren().addAll(title, comboBox, submit);
        vbox.setAlignment(Pos.CENTER);
        pane = vbox;
    }

    public void setLabel() {
        title = new Label("Which user are you?");
        title.setStyle("-fx-font-size: 40px; -fx-text-fill: #383838;");
        title.setTextAlignment(TextAlignment.CENTER);
    }

    public void setDropDownList() {
        comboBox = new ComboBox<>();
        comboBox.setPromptText("Select one...");
        comboBox.setMinWidth(200);
        comboBox.setStyle("-fx-font-size: 15px;");

        ArrayList<String> list = userInterface.getSession().getUserList();

        for (String s : list) {
            comboBox.getItems().add(s);
        }
    }

    public void setSubmitButton() {
        submit = new Button("Submit");
        submit.setMinWidth(100);
        submitButtonListener();
    }

    public void submitButtonListener() {
        submit.setOnAction(e -> {
            try {
                userInterface.getSession().selectUser(comboBox.getSelectionModel().getSelectedItem());
                userInterface.makeSideBar();
            } catch (NullEntryException exception) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("Please select a user.");
                a.show();
            }
        });
    }
}
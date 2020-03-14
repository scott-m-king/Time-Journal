package ui.screens;

import exceptions.NullEntryException;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import ui.UserInterface;

import java.util.ArrayList;

public class UserSelectScreen {
    private final UserInterface userInterface;
    private ComboBox<String> comboBox;

    public UserSelectScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void userSelect() {
        GridPane root = createGridPane();
        Label title = setScreenLabel();
        setDropDownList();
        Button submit = setSubmitButton();
        initializeScreen(root, title, submit);
    }

    public void initializeScreen(GridPane root, Label title, Button submit) {
        root.getChildren().addAll(title, comboBox, submit);
        root.setAlignment(Pos.CENTER);
        submitButtonListener(submit);
        userInterface.initializeScene(root, userInterface.getMainStage());
    }

    public GridPane createGridPane() {
        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(50);
        root.setHgap(10);
        return root;
    }

    public Label setScreenLabel() {
        Label title = new Label("Which user are you?");
        title.setStyle("-fx-font-size: 40px; -fx-text-fill: #383838;");
        GridPane.setConstraints(title, 0, 0);
        return title;
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

        GridPane.setConstraints(comboBox, 0, 1);
        GridPane.setHalignment(comboBox, HPos.CENTER);
    }

    public Button setSubmitButton() {
        Button submit = new Button("Submit");
        submit.setMinWidth(100);
        GridPane.setConstraints(submit, 0, 2);
        GridPane.setHalignment(submit, HPos.CENTER);
        return submit;
    }

    public void submitButtonListener(Button submit) {
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
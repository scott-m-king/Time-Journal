package ui.screens;

import exceptions.NullEntryException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
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

    // EFFECTS: runs methods needed to initialize this screen
    public void renderUserSelectScreen() {
        setLabel();
        setDropDownList();
        setSubmitButton();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
        setEnterListener();
    }

    // MODIFIES: this
    // EFFECTS: creates final vbox pane to display all elements in scene
    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.setSpacing(50);
        vbox.getChildren().addAll(title, comboBox, submit);
        vbox.setAlignment(Pos.CENTER);
        pane = vbox;
    }

    // MODIFIES: this
    // EFFECTS: instantiates and positions screen's main label
    public void setLabel() {
        title = new Label("Which user are you?");
        title.setStyle("-fx-font-size: 40px; -fx-text-fill: #383838;");
        title.setTextAlignment(TextAlignment.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: instantiates and positions screen's category combobox
    public void setDropDownList() {
        comboBox = new ComboBox<>();
        comboBox.setPromptText("Select one...");
        comboBox.setMinWidth(200);
        comboBox.setStyle("-fx-font-size: 15px;");

        ArrayList<String> list = userInterface.getCurrentSession().getUserList();

        for (String s : list) {
            comboBox.getItems().add(s);
        }
    }

    // MODIFIES: this
    // EFFECTS: instantiates and sets this screen's submit button
    public void setSubmitButton() {
        submit = new Button("Submit");
        submit.setMinWidth(100);
        submitButtonListener();
    }

    // MODIFIES: UserSession
    // EFFECTS: validates if user is selected
    //          if validated, creates first new category and adds it to UserSession's CategoryList
    public void selectUser() {
        try {
            userInterface.getCurrentSession().selectUser(comboBox.getSelectionModel().getSelectedItem());
            userInterface.getSideBarComponent().renderSideBar();
        } catch (NullEntryException exception) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Please select a user.");
            a.show();
        }
    }

    // MODIFIES: this
    // EFFECTS: sets event listener for button click, tries to submit form if clicked
    public void submitButtonListener() {
        submit.setOnAction(e -> {
            selectUser();
        });
    }

    // MODIFIES: this
    // EFFECTS: sets event listener for enter key, tries to submit form if pressed
    public void setEnterListener() {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                selectUser();
            }
        });
    }
}
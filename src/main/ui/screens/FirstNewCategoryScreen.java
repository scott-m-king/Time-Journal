package ui.screens;

import exceptions.CategoryExistsException;
import exceptions.NullEntryException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import ui.UserInterface;

// Represents a screen in which the user creates their first category when creating a new account
public class FirstNewCategoryScreen extends Screen {
    private final UserInterface userInterface;
    private Label nameLabel;
    private Button startJournal;
    private TextField categoryName;
    private Pane pane;

    public FirstNewCategoryScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // EFFECTS: runs methods needed to initialize this screen
    public void renderFirstNewCategoryScreen() {
        setScreenLabel();
        setTextField();
        setStartButton();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
        setEnterListener();
    }

    // MODIFIES: this
    // EFFECTS: creates final vbox pane to display all elements in scene
    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(nameLabel, categoryName, startJournal);
        pane = vbox;
    }

    // MODIFIES: this
    // EFFECTS: instantiates and sets screen's main label
    public void setScreenLabel() {
        nameLabel = new Label("Let's start with creating your first category.\n"
                + "Enter a name for your category below:");
        nameLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: #383838;");
        nameLabel.setTextAlignment(TextAlignment.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: instantiates and sets screen's text entry field
    public void setTextField() {
        categoryName = new TextField();
        categoryName.setMaxWidth(300);
        categoryName.setStyle("-fx-font-size: 20px;");
        categoryName.setAlignment(Pos.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: instantiates and sets this screen's submit button
    public void setStartButton() {
        startJournal = new Button("Get Started");
        startJournal.setAlignment(Pos.CENTER);
        setButtonHandler(startJournal);
    }

    // MODIFIES: UserSession
    // EFFECTS: validates if form is properly filled out
    //          if validated, creates first new category and adds it to UserSession's CategoryList
    private void submitForm() {
        try {
            userInterface.getCurrentSession().createNewCategory(categoryName.getText());
            userInterface.getSideBarComponent().renderSideBar();
        } catch (NullEntryException e1) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("You must enter a name for your category.");
            a.show();
        } catch (CategoryExistsException exception) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("You happened to enter the one category that already exists... try again.");
            a.show();
        }
    }

    // MODIFIES: this
    // EFFECTS: sets event listener for button click, tries to submit form if clicked
    public void setButtonHandler(Button startJournal) {
        startJournal.setOnAction(e -> submitForm());
    }

    // MODIFIES: this
    // EFFECTS: sets event listener for enter key, tries to submit form if pressed
    public void setEnterListener() {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitForm();
            }
        });
    }
}
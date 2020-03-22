package ui.screens;

import exceptions.CategoryExistsException;
import exceptions.NullEntryException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.UserInterface;


// Represents a Create Category Popup which is where the user can create new categories
public class CreateCategoryPopup extends Popup {
    private final UserInterface userInterface;
    private TextField categoryName;
    private Label mainLabel;
    private Text instructionText;
    private Stage stage;
    private Pane pane;


    public CreateCategoryPopup(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // MODIFIES: this
    // EFFECTS: runs methods needed to render category list screen
    public void renderCategoryPopup() {
        stage = createPopupStage(STANDARD_POPUP_WIDTH, STANDARD_POPUP_HEIGHT);
        initializeFinalPane();
        userInterface.setMiddle(stage);
        initializeScreen(pane, stage);
        setEnterListener();
    }

    // MODIFIES: this
    // EFFECTS: creates vbox to display all content on popup
    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.setSpacing(20.0);
        setMainLabel();
        setInstructionText();
        setTextField();
        HBox buttonPane = makeFormButtons(stage, CREATE_CATEGORY, userInterface);
        vbox.getChildren().addAll(mainLabel, instructionText, categoryName, buttonPane);
        vbox.setAlignment(Pos.CENTER);
        pane = vbox;
    }

    // MODIFIES: this
    // EFFECTS: creates main label for popup
    private void setMainLabel() {
        mainLabel = new Label("Create New Category");
        mainLabel.setStyle("-fx-text-fill:#383838;");
    }

    // MODIFIES: this
    // EFFECTS: creates instruction label for popup
    private void setInstructionText() {
        instructionText = new Text("Enter a name for your your category:");
        instructionText.setStyle("-fx-font-size:16px;");
    }

    // MODIFIES: this
    // EFFECTS: creates text field to collect user input
    private void setTextField() {
        categoryName = new TextField();
        categoryName.setMaxWidth(300);
        categoryName.setStyle("-fx-font-size:14px;");
    }

    // MODIFIES: this, UserSession
    // EFFECTS: validates user entry, if successful create new category in UserSession
    public void createNewCategory() {
        try {
            userInterface.getCurrentSession().createNewCategory(categoryName.getText());
            playSuccessSound();
            stage.close();
            userInterface.getCategoryListScreen().renderCategoryListScreen();
            successAlert();
        } catch (NullEntryException e) {
            nullEntryAlert();
        } catch (CategoryExistsException e) {
            categoryAlreadyExistsAlert();
        }
    }

    // EFFECTS: displays alert if user successfully created category
    private void successAlert() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("You've successfully created the " + categoryName.getText() + " category.");
        a.show();
    }

    // EFFECTS: displays alert if user entered duplicate category name
    private void categoryAlreadyExistsAlert() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Sorry, that category already exists. Please try again.");
        a.show();
    }

    // EFFECTS: displays alert if user did not enter a name to change category to
    private void nullEntryAlert() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("You must enter a name for your category.");
        a.show();
    }

    // MODIFIES: this
    // EFFECTS: sets enter key listener
    private void setEnterListener() {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createNewCategory();
            }
        });
    }
}
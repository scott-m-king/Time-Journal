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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import ui.UserInterface;

// Represents an edit category popup which the user can use to edit the name of a category
public class EditCategoryPopup extends Popup {
    private final UserInterface userInterface;
    private Stage stage;
    private Label label;
    private Text text;
    private TextField categoryName;
    private HBox buttons;
    private Pane pane;

    public EditCategoryPopup(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // REQUIRES: valid UserSession
    // MODIFIES: this
    // EFFECTS: runs methods needed to render edit category popup
    public void initializeScreen() {
        stage = createPopupStage(STANDARD_POPUP_WIDTH, STANDARD_POPUP_HEIGHT);
        setFields();
        buttons = makeFormButtons(stage, EDIT_CATEGORY, userInterface);
        initializeFinalPane();
        userInterface.setMiddle(stage);
        initializeScreen(pane, stage);
        setEnterListener();
    }

    // MODIFIES: this
    // EFFECTS: sets up final vbox pane to render to scene
    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, text, categoryName, buttons);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(15.0);
        pane = vbox;
    }

    // MODIFIES: this
    // EFFECTS: sets main label and instructions
    private void setFields() {
        setLabel();
        text = new Text("What would you like to change the name of the category to?");
        text.setStyle("-fx-font-size:15px;");
        categoryName = new TextField();
        categoryName.setMaxWidth(300);
    }

    // REQUIRES: category selected is not null
    // MODIFIES: this
    // EFFECTS:
    private void setLabel() {
        label = new Label(
                "Editing "
                + userInterface.getCategoryListScreen().getCategoryCurrentSelected()
                + " category"
        );
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-text-fill:#383838; -fx-font-size:25px;");
    }

    // MODIFIES: this, UserSession
    // EFFECTS: validates that a category is selected and is not null or uncategorized
    //          if validated, remove category from UserSession's CategoryList
    public void editCategory() {
        try {
            userInterface.getCurrentSession().editCategory(userInterface
                            .getCategoryListScreen()
                            .getCategoryCurrentSelected(), categoryName.getText());
            stage.close();
            playSuccessSound();
            userInterface.getCategoryListScreen().renderCategoryListScreen();
            successAlert();
        } catch (CategoryExistsException exception1) {
            categoryAlreadyExistsAlert();
        } catch (NullEntryException exception2) {
            nullEntryAlert();
        }
    }

    // EFFECTS: displays alert if user successfully edited the category
    private void successAlert() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("You've successfully edited the category.");
        a.show();
    }

    // EFFECTS: displays alert if user did not enter a name to change the category to
    private void nullEntryAlert() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Please enter a name for your category.");
        a.show();
    }

    // EFFECTS: displays alert if user entered name of a category that already exists
    private void categoryAlreadyExistsAlert() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("That category name already exists.");
        a.show();
    }

    // EFFECTS: sets enter key listener to submit form
    private void setEnterListener() {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                editCategory();
            }
        });
    }
}
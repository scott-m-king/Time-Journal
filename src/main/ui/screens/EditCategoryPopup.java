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

    public void initializeScreen() {
        stage = createPopupStage(STANDARD_POPUP_WIDTH, STANDARD_POPUP_HEIGHT);
        setFields();
        buttons = makeFormButtons(stage, EDIT_CATEGORY, userInterface);
        initializeFinalPane();
        userInterface.setMiddle(stage);
        initializeScreen(pane, stage);
        setEnterListener();
    }

    @Override
    protected void initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, text, categoryName, buttons);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(15.0);
        pane = vbox;
    }

    private void setFields() {
        setLabel();
        text = new Text("What would you like to change the name of the category to?");
        text.setStyle("-fx-font-size:15px;");
        categoryName = new TextField();
        categoryName.setMaxWidth(300);
    }

    private void setLabel() {
        label = new Label(
                "Editing "
                + userInterface.getCategoryListScreen().getCategoryCurrentSelected()
                + " category"
        );
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-text-fill:#383838; -fx-font-size:25px;");
    }

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

    private void successAlert() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("You've successfully edited the category.");
        a.show();
    }

    private void nullEntryAlert() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Please enter a name for your category.");
        a.show();
    }

    private void categoryAlreadyExistsAlert() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("That category name already exists.");
        a.show();
    }

    private void setEnterListener() {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                editCategory();
            }
        });
    }
}
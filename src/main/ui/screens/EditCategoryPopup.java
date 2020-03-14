package ui.screens;

import exceptions.CategoryExistsException;
import exceptions.NullEntryException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    Label label;
    Text text;
    TextField categoryName;
    HBox buttons;

    public EditCategoryPopup(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void initializeScreen() {
        stage = createStandardStage();
        setFields();
        buttons = userInterface
                .getCategoryListScreen()
                .makeCategoryButtons(categoryName, stage, "editCategoryScreen");
        initializeScreen(initializeFinalPane(), stage);
        userInterface.setMiddle(stage);
        stage.show();
    }

    @Override
    protected Pane initializeFinalPane() {
        VBox screen = new VBox();
        screen.getChildren().addAll(label, text, categoryName, buttons);
        screen.setAlignment(Pos.CENTER);
        screen.setSpacing(15.0);
        return screen;
    }

    private void setFields() {
        label = new Label("Editing " + userInterface.getCategoryCurrentlySelected() + " category");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-text-fill:#383838; -fx-font-size:25px;");
        text = new Text("What would you like to change the name of the category to?");
        text.setStyle("-fx-font-size:15px;");
        categoryName = new TextField();
        categoryName.setMaxWidth(300);
    }

    public void editCategory(Button submit, TextField categoryName) {
        submit.setOnAction(e -> {
            try {
                userInterface.getSession().editCategory(userInterface.getCategoryCurrentlySelected(), categoryName.getText());
                stage.close();
            } catch (CategoryExistsException exception1) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("That category name already exists.");
                a.show();
            } catch (NullEntryException exception2) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("Please enter a name for your category.");
                a.show();
            }
        });
    }
}
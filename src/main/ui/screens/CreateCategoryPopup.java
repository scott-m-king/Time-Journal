package ui.screens;

import exceptions.CategoryExistsException;
import exceptions.NullEntryException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import ui.UserInterface;

public class CreateCategoryPopup extends Popup {
    private final UserInterface userInterface;
    private Stage stage;

    public CreateCategoryPopup(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderScreen() {
        stage = createStandardStage(
        );

        initializeScreen(initializeFinalPane(), stage);
        userInterface.setMiddle(stage);
        stage.show();
    }

    @Override
    protected Pane initializeFinalPane() {
        VBox vbox = new VBox();
        vbox.setSpacing(20.0);

        Text text = new Text("Enter a name for your your category:");
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle("-fx-font-size:16px;");

        TextField categoryName = new TextField();
        categoryName.setAlignment(Pos.CENTER);
        categoryName.setMaxWidth(300);
        categoryName.setStyle("-fx-font-size:14px;");

        HBox hbox = userInterface.getCategoryListScreen().makeCategoryButtons(categoryName, stage, "createCategoryScreen");

        vbox.getChildren().addAll(text, categoryName, hbox);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    public void createNewCategory(TextField categoryName, Stage createCategoryStage) {
        try {
            userInterface.getSession().createNewCategory(categoryName.getText());
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("You've successfully added the category.");
            a.show();
            createCategoryStage.close();
        } catch (NullEntryException e1) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("You must enter a name for your category.");
            a.show();
        } catch (CategoryExistsException exception) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Sorry, that category already exists. Please try again.");
            a.show();
        }
    }
}
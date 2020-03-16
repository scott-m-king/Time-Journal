package ui.screens;

import exceptions.CategoryExistsException;
import exceptions.NullEntryException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.UserInterface;

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

    public void renderCategoryPopup() {
        stage = createPopupStage(STANDARD_POPUP_WIDTH, STANDARD_POPUP_HEIGHT);
        initializeFinalPane();
        userInterface.setMiddle(stage);
        initializeScreen(pane, stage);
    }

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

    private void setMainLabel() {
        mainLabel = new Label("Create New Category");
        mainLabel.setStyle("-fx-text-fill:#383838;");
    }

    private void setInstructionText() {
        instructionText = new Text("Enter a name for your your category:");
        instructionText.setStyle("-fx-font-size:16px;");
    }

    private void setTextField() {
        categoryName = new TextField();
        categoryName.setMaxWidth(300);
        categoryName.setStyle("-fx-font-size:14px;");
    }

    public void createNewCategory() {
        try {
            userInterface.getCurrentSession().createNewCategory(categoryName.getText());
            userInterface.getCategoryListScreen().renderCategoryListScreen();
            stage.close();
        } catch (NullEntryException e) {
            nullEntryAlert();
        } catch (CategoryExistsException e) {
            categoryAlreadyExistsAlert();
        }
    }

    private void categoryAlreadyExistsAlert() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Sorry, that category already exists. Please try again.");
        a.show();
    }

    private void nullEntryAlert() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("You must enter a name for your category.");
        a.show();
    }

}
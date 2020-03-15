package ui.screens;

import exceptions.NegativeNumberException;
import exceptions.NullEntryException;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Category;
import model.JournalEntry;
import ui.UserInterface;

public class JournalEntryEditPopup extends Popup {
    private final UserInterface userInterface;
    private JournalEntry selectedJournalEntry;
    private Label mainLabel;
    private Text descriptionLabel;
    private TextField descriptionField;
    private Text durationLabel;
    private TextField durationField;
    private Text categoryLabel;
    private ComboBox<String> categoryDropDown;
    private ObservableList<Category> categoryDropDownCategories;
    private GridPane gridPane;
    private HBox buttonLayout;
    private Pane pane;
    private Stage stage;

    public static String LABEL_TEXT_SIZE = "-fx-font-size:17px;";

    public JournalEntryEditPopup(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderJournalEntryEditPopup() {
        selectedJournalEntry = userInterface.getJournalLogScreen().getSelectedEntry();
        initializeFields();
        setFormLayout();
        setMainLabel();
        setStage();
        setButtonLayout();
        initializeFinalPane();
        initializeScreen(pane, stage);
    }

    @Override
    protected void initializeFinalPane() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().addAll(mainLabel, gridPane, buttonLayout);
        pane = anchorPane;
    }

    private void setStage() {
        stage = createPopupStage(UserInterface.WINDOW_WIDTH - 100, UserInterface.WINDOW_HEIGHT - 100);
        userInterface.setMiddle(stage);
    }

    private void setMainLabel() {
        mainLabel = new Label("Edit Journal Entry");
        AnchorPane.setTopAnchor(mainLabel, 30.0);
        AnchorPane.setLeftAnchor(mainLabel, 30.0);
        mainLabel.setStyle("-fx-text-fill:#383838; -fx-font-size:25px;");
    }

    private void setButtonLayout() {
        buttonLayout = makeFormButtons(stage, EDIT_JOURNAL_ENTRY, userInterface);
        AnchorPane.setBottomAnchor(buttonLayout, 30.0);
        AnchorPane.setRightAnchor(buttonLayout, 30.0);
    }

    private void setFormLayout() {
        gridPane = new GridPane();
        gridPane.getChildren().addAll(
                descriptionLabel,
                descriptionField,
                durationLabel,
                durationField,
                categoryLabel,
                categoryDropDown);
        AnchorPane.setTopAnchor(gridPane, 100.0);
        AnchorPane.setLeftAnchor(gridPane, 30.0);
        AnchorPane.setRightAnchor(gridPane, 30.0);
        AnchorPane.setBottomAnchor(gridPane, 30.0);
        gridPane.setVgap(15);
    }

    private void initializeFields() {
        setDescriptionLabel();
        setDescriptionField();
        setDurationLabel();
        setDurationField();
        setCategoryLabel();
        setCategoryDropDown();
    }

    private void setDescriptionLabel() {
        descriptionLabel = new Text("What did you get up to? Enter a description for your journal entry: ");
        descriptionLabel.setStyle(LABEL_TEXT_SIZE);
        GridPane.setConstraints(descriptionLabel, 0, 0);
    }

    private void setDescriptionField() {
        descriptionField = new TextField(selectedJournalEntry.getDescription());
        GridPane.setConstraints(descriptionField, 0, 1);
        descriptionField.setMinWidth(UserInterface.WINDOW_WIDTH - 160);
    }

    private void setDurationLabel() {
        durationLabel = new Text("How long did you spend on this? (in minutes):");
        durationLabel.setStyle(LABEL_TEXT_SIZE);
        GridPane.setConstraints(durationLabel, 0, 3);
    }

    private void setDurationField() {
        durationField = new TextField(selectedJournalEntry.getDuration().toString());
        GridPane.setConstraints(durationField, 0, 4);
        durationField.setMinWidth(UserInterface.WINDOW_WIDTH - 160);
    }

    private void setCategoryLabel() {
        categoryLabel = new Text("What category would you like to assign this entry to?");
        categoryLabel.setStyle(LABEL_TEXT_SIZE);
        GridPane.setConstraints(categoryLabel, 0, 6);
    }

    private void setCategoryDropDown() {
        categoryDropDown = new ComboBox<>();
        categoryDropDownCategories = userInterface.getCategoryListScreen().generateCategoryList();

        for (Category c : categoryDropDownCategories) {
            categoryDropDown.getItems().add(c.getDurationString());
        }

        categoryDropDown.setValue(selectedJournalEntry.getCategory().getDurationString());
        GridPane.setConstraints(categoryDropDown, 0, 7, 1, 1);
        categoryDropDown.setMinWidth(UserInterface.WINDOW_WIDTH - 160);
    }

    public void editJournalEntry() {
        validateForm();
        userInterface.getSession().editJournalEntryDescription(
                selectedJournalEntry.getJournalID(),
                descriptionField.getText());
        userInterface.getSession().editJournalEntryDuration(
                selectedJournalEntry.getJournalID(),
                durationField.getText());
        userInterface.getSession().editJournalEntryCategory(
                selectedJournalEntry.getJournalID(),
                categoryDropDownCategories.get(categoryDropDown.getSelectionModel().getSelectedIndex()).getName());
        stage.close();
        userInterface.getJournalLogScreen().renderJournalLogScreen();
        alertSuccessfulEntry();
    }

    private void validateForm() {
        try {
            userInterface.getSession().checkValidForm(descriptionField.getText(), durationField.getText());
        } catch (NumberFormatException e) {
            alertNumberFormatException();
        } catch (NegativeNumberException e) {
            alertNegativeDuration();
        } catch (NullEntryException e) {
            alertNullFieldEntry();
        }
    }

    private void alertNumberFormatException() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("You didn't enter a number for the duration. Please try again.");
        a.show();
    }

    private void alertNullFieldEntry() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Please make sure to fill in all fields.");
        a.show();
    }

    private void alertSuccessfulEntry() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Entry successfully edited!");
        a.show();
    }

    private void alertNegativeDuration() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Please enter a positive duration.");
        a.show();
    }
}

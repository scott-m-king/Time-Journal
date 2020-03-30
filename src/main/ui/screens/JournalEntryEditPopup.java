package ui.screens;

import exceptions.NegativeNumberException;
import exceptions.NullEntryException;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Category;
import model.JournalEntry;
import ui.UserInterface;

// Represents a popup within the Journal Log screen where users can edit a chosen journal entry
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

    public static final String LABEL_TEXT_SIZE = "-fx-font-size: 17px;";
    public static final String FIELD_TEXT_SIZE = "-fx-font-size: 15px;";

    public JournalEntryEditPopup(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // REQUIRES: valid UserSession
    // MODIFIES: this
    // EFFECTS: runs methods needed to render this scene
    public void renderJournalEntryEditPopup() {
        selectedJournalEntry = userInterface.getJournalLogScreen().getSelectedEntry();
        initializeFields();
        setFormLayout();
        setMainLabel();
        setStage();
        setButtonLayout();
        initializeFinalPane();
        initializeScreen(pane, stage);
        setEnterListener();
    }

    // MODIFIES: this
    // EFFECTS: sets the final layout of the scene
    @Override
    protected void initializeFinalPane() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().addAll(mainLabel, gridPane, buttonLayout);
        pane = anchorPane;
    }

    // MODIFIES: this
    // EFFECTS: creates the popup and sets it to the middle of the screen
    private void setStage() {
        stage = createPopupStage(UserInterface.WINDOW_WIDTH - 100, UserInterface.WINDOW_HEIGHT - 100);
        userInterface.getScreenHelper().setMiddle(stage, userInterface);
    }

    // MODIFIES: this
    // EFFECTS: creates and anchors the main label of the popup
    private void setMainLabel() {
        mainLabel = new Label("Edit Journal Entry");
        AnchorPane.setTopAnchor(mainLabel, 30.0);
        AnchorPane.setLeftAnchor(mainLabel, 30.0);
        mainLabel.setStyle("-fx-text-fill:#383838; -fx-font-size:25px;");
    }

    // MODIFIES: this
    // EFFECTS:creates and anchors the "submit" and "cancel" buttons of the popup - implemented in Popup
    private void setButtonLayout() {
        buttonLayout = makeFormButtons(stage, EDIT_JOURNAL_ENTRY, userInterface);
        AnchorPane.setBottomAnchor(buttonLayout, 30.0);
        AnchorPane.setRightAnchor(buttonLayout, 30.0);
    }

    // MODIFIES: this
    // EFFECTS: creates and anchors the gridpane that holds the fields of the form
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

    // EFFECTS: runs initialization methods for the form fields
    private void initializeFields() {
        setDescriptionLabel();
        setDescriptionField();
        setDurationLabel();
        setDurationField();
        setCategoryLabel();
        setCategoryDropDown();
    }

    // MODIFIES: this
    // EFFECTS: creates description label and places in gridpane
    private void setDescriptionLabel() {
        descriptionLabel = new Text("What did you get up to? Enter a description for your journal entry: ");
        descriptionLabel.setStyle(LABEL_TEXT_SIZE);
        GridPane.setConstraints(descriptionLabel, 0, 0);
    }

    // MODIFIES: this
    // EFFECTS: creates description field and places in gridpane
    private void setDescriptionField() {
        descriptionField = new TextField(selectedJournalEntry.getDescription());
        descriptionField.setStyle(FIELD_TEXT_SIZE);
        GridPane.setConstraints(descriptionField, 0, 1);
        descriptionField.setMinWidth(UserInterface.WINDOW_WIDTH - 160);
    }

    // MODIFIES: this
    // EFFECTS: creates duration label and places in gridpane
    private void setDurationLabel() {
        durationLabel = new Text("How long did you spend on this? (in minutes):");
        durationLabel.setStyle(LABEL_TEXT_SIZE);
        GridPane.setConstraints(durationLabel, 0, 3);
    }

    // MODIFIES: this
    // EFFECTS: creates duration field and places in gridpane
    private void setDurationField() {
        durationField = new TextField(selectedJournalEntry.getDuration().toString());
        durationField.setStyle(FIELD_TEXT_SIZE);
        GridPane.setConstraints(durationField, 0, 4);
        durationField.setMinWidth(UserInterface.WINDOW_WIDTH - 160);
    }

    // MODIFIES: this
    // EFFECTS: creates category label and places in gridpane
    private void setCategoryLabel() {
        categoryLabel = new Text("What category would you like to assign this entry to?");
        categoryLabel.setStyle(LABEL_TEXT_SIZE);
        GridPane.setConstraints(categoryLabel, 0, 6);
    }

    // MODIFIES: this
    // EFFECTS: creates and populates category combobox and places in gridpane
    private void setCategoryDropDown() {
        categoryDropDown = new ComboBox<>();
        categoryDropDown.setStyle(FIELD_TEXT_SIZE);
        categoryDropDownCategories = userInterface.getCategoryListComponent().getCategoryObservableList();

        for (Category c : categoryDropDownCategories) {
            categoryDropDown.getItems().add(c.getDurationString());
        }

        categoryDropDown.setValue(selectedJournalEntry.getCategory().getDurationString());
        GridPane.setConstraints(categoryDropDown, 0, 7, 1, 1);
        categoryDropDown.setMinWidth(UserInterface.WINDOW_WIDTH - 160);
    }

    // MODIFIES: this
    // EFFECTS: if form is validated, convert user input to strings, otherwise do nothing
    public void editJournalEntry() {
        if (isFormValidated()) {
            userInterface.getCurrentSession().editJournalEntryDescription(
                    selectedJournalEntry.getJournalID(),
                    descriptionField.getText());
            userInterface.getCurrentSession().editJournalEntryDuration(
                    selectedJournalEntry.getJournalID(),
                    durationField.getText());
            userInterface.getCurrentSession().editJournalEntryCategory(
                    selectedJournalEntry.getJournalID(),
                    categoryDropDownCategories.get(categoryDropDown.getSelectionModel().getSelectedIndex()).getName());
            stage.close();
            userInterface.getJournalLogScreen().renderJournalLogScreen();
            playSuccessSound();
            alertSuccessfulEntry();
        }
    }

    // EFFECTS: returns true if form is validated, false otherwise
    private boolean isFormValidated() {
        try {
            userInterface.getCurrentSession().checkValidForm(descriptionField.getText(), durationField.getText());
        } catch (NumberFormatException e) {
            alertNumberFormatException();
            return false;
        } catch (NegativeNumberException e) {
            alertNegativeDuration();
            return false;
        } catch (NullEntryException e) {
            alertNullFieldEntry();
            return false;
        }
        return true;
    }

    // EFFECTS: alerts user that entry was successful
    private void alertSuccessfulEntry() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("Entry successfully edited!");
        a.show();
    }

    // EFFECTS: alerts user that not all fields are filled out
    private void alertNullFieldEntry() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Please make sure to fill in all fields.");
        a.show();
    }

    // EFFECTS: alerts user that duration input is not a number
    private void alertNumberFormatException() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("You didn't enter a number for the duration. Please try again.");
        a.show();
    }

    // EFFECTS: alerts user that duration entry is a negative number
    private void alertNegativeDuration() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Please enter a positive duration.");
        a.show();
    }

    // REQUIRES: enter button to be instantiated before this method is called
    // MODIFIES: this
    // EFFECTS: sets button listener on enter key anywhere on screen - will trigger method if enter key pressed
    private void setEnterListener() {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                editJournalEntry();
            }
        });
    }
}

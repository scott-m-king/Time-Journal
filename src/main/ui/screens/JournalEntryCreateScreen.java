package ui.screens;

import exceptions.NegativeNumberException;
import exceptions.NullEntryException;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Category;
import ui.UserInterface;

// Represents the screen in which the user can enter new journal entries
public class JournalEntryCreateScreen extends Screen {
    private final UserInterface userInterface;
    private Pane sideBar;
    private Text title;
    private ComboBox<String> categoryListDurationString;
    private ObservableList<Category> categoryListCategory;
    private TextField descriptionField;
    private TextField durationField;
    private String descriptionEntry;
    private String durationEntry;
    private String categoryEntry;
    private Pane pane;

    public JournalEntryCreateScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // REQUIRES: valid UserSession
    // MODIFIES: this
    // EFFECTS: runs all methods necessary to render this screen
    public void renderJournalEntryCreateScreen() {
        title = setMainTitle();
        Button newJournalEntryButton = userInterface.getSideBarComponent().getNewJournalEntryButton();
        sideBar = userInterface.getSideBarComponent().getSideBarPane();
        newJournalEntryButton.setStyle("-fx-background-color:#787878");
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
    }

    // MODIFIES: this
    // EFFECTS: creates final pane to render to screen
    @Override
    protected void initializeFinalPane() {
        pane = new AnchorPane();

        Button submit = setSubmitButton();
        TextField descriptionField = setDescriptionField();
        TextField durationField = setDurationField();
        ComboBox<String> categoryList = setCategoryDropdown();

        pane.getChildren().addAll(
                sideBar,
                userInterface.getSideBarComponent().getQuitButton(),
                title,
                setDurationLabel(),
                setDescriptionLabel(),
                setCategoryLabel(),
                descriptionField,
                durationField,
                categoryList,
                submit);

        setListeners(submit);
    }

    // EFFECTS: returns anchored submit button
    private Button setSubmitButton() {
        Button submit = new Button("Submit");
        AnchorPane.setBottomAnchor(submit, 30.0);
        AnchorPane.setRightAnchor(submit, 30.0);
        return submit;
    }

    // EFFECTS: returns anchored main title
    public Text setMainTitle() {
        Text title = new Text();
        title.setFont(new Font(UserInterface.TITLE_FONT_SIZE));
        title.setText("Create New Journal Entry");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);
        return title;
    }

    // EFFECTS: returns anchored description label
    public Text setDescriptionLabel() {
        Text descriptionLabel = new Text("What did you get up to? Enter a description for your journal entry:");
        descriptionLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(descriptionLabel, 95.0);
        AnchorPane.setLeftAnchor(descriptionLabel, 230.0);
        AnchorPane.setRightAnchor(descriptionLabel, 30.0);
        return descriptionLabel;
    }

    // EFFECTS: returns anchored description field
    public TextField setDescriptionField() {
        descriptionField = new TextField();
        descriptionField.setStyle("-fx-font-size: 15px");
        AnchorPane.setTopAnchor(descriptionField, 125.0);
        AnchorPane.setLeftAnchor(descriptionField, 230.0);
        AnchorPane.setRightAnchor(descriptionField, 30.0);
        return descriptionField;
    }

    // EFFECTS: returns anchored duration label
    public Text setDurationLabel() {
        Text durationLabel = new Text("How long did you spend on this? (in minutes)");
        durationLabel.setStyle("-fx-font-size: 17px;");
        AnchorPane.setTopAnchor(durationLabel, 185.0);
        AnchorPane.setLeftAnchor(durationLabel, 230.0);
        AnchorPane.setRightAnchor(durationLabel, 30.0);
        return durationLabel;
    }

    // EFFECTS: returns anchored duration field
    public TextField setDurationField() {
        durationField = new TextField();
        durationField.setStyle("-fx-font-size: 15px");
        AnchorPane.setTopAnchor(durationField, 215.0);
        AnchorPane.setLeftAnchor(durationField, 230.0);
        AnchorPane.setRightAnchor(durationField, 30.0);
        return durationField;
    }

    // EFFECTS: returns anchored category label
    public Text setCategoryLabel() {
        Text categoryLabel = new Text("What category would you like to assign this entry to?");
        categoryLabel.setStyle("-fx-font-size: 17px;");
        AnchorPane.setTopAnchor(categoryLabel, 265.0);
        AnchorPane.setLeftAnchor(categoryLabel, 230.0);
        AnchorPane.setRightAnchor(categoryLabel, 30.0);
        return categoryLabel;
    }

    // EFFECTS: returns anchored category combobox
    public ComboBox<String> setCategoryDropdown() {
        populateCategoryDropdown();
        AnchorPane.setTopAnchor(categoryListDurationString, 295.0);
        AnchorPane.setLeftAnchor(categoryListDurationString, 230.0);
        AnchorPane.setRightAnchor(categoryListDurationString, 30.0);
        return categoryListDurationString;
    }

    // EFFECTS: populates category dropdown
    private void populateCategoryDropdown() {
        categoryListDurationString = new ComboBox<>();
        categoryListDurationString.setStyle("-fx-font-size: 15px");
        categoryListCategory = userInterface.getCategoryListScreen().getCategoryObservableList();

        for (Category c : categoryListCategory) {
            categoryListDurationString.getItems().add(c.getDurationString());
        }

        categoryListDurationString.setValue(categoryListCategory.get(0).getDurationString());
    }

    // MODIFIES: this
    // EFFECTS: sets submit button listener and sets enter key listener
    //          runs enter journal entry methods if either listener triggered
    public void setListeners(Button submit) {
        submit.setOnAction(e -> doJournalEntry());

        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                doJournalEntry();
            }
        });
    }

    // MODIFIES: UserSession
    // EFFECTS: if form is filled out properly, does journal entry and alerts user
    //          else, alerts user that there was something wrong with their entry
    public void doJournalEntry() {
        if (isFormValidated()) {
            userInputToString();
            userInterface.getCurrentSession().createNewJournalEntry(descriptionEntry, durationEntry, categoryEntry);
            playSuccessSound();
            clearFields();
            alertSuccessfulEntry();
        }
    }

    // EFFECTS: if filled out correctly, returns true. Else, catches exceptions and returns false
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

    // MODIFIES: this
    // EFFECTS: gets value of user input as strings
    private void userInputToString() {
        descriptionEntry = descriptionField.getText();
        durationEntry = durationField.getText();
        categoryEntry = categoryListCategory.get(
                categoryListDurationString
                        .getSelectionModel()
                        .getSelectedIndex())
                .getName();
    }

    // REQUIRES: final pane with child nodes already populated
    // MODIFIES: this
    // EFFECTS: clears all fields and resets pane
    private void clearFields() {
        descriptionField.clear();
        durationField.clear();
        categoryListDurationString.setValue(categoryListCategory.get(0).getDurationString());
        pane.getChildren().clear();
        renderJournalEntryCreateScreen();
    }

    // EFFECTS: alerts user if duration entered is not numeric
    private void alertNumberFormatException() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("You didn't enter a number for the duration. Please try again.");
        a.show();
    }

    // EFFECTS: alerts user either description or duration field is not filled out
    private void alertNullFieldEntry() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Please make sure to fill in all fields.");
        a.show();
    }

    /// EFFECTS: alerts user if entry is successful
    private void alertSuccessfulEntry() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("Entry successfully added!");
        a.show();
    }

    // EFFECTS: alerts user if duration entered is negative
    private void alertNegativeDuration() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Please enter a positive duration.");
        a.show();
    }
}
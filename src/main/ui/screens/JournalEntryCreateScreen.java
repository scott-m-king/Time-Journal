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

//TODO: REFACTOR!

public class JournalEntryCreateScreen extends Screen {
    private final UserInterface userInterface;
    private Pane pane;
    private Pane sideBar;
    private Text title;
    private ComboBox<String> categoryListDurationString;
    private ObservableList<Category> categoryListCategory;
    private TextField descriptionField;
    private TextField durationField;
    private String descriptionEntry;
    private String durationEntry;
    private String categoryEntry;

    public JournalEntryCreateScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void createJournalEntryScreen() {
        title = createJournalEntrySetTitle();
        Button newJournalEntryButton = userInterface.getJournalEntryButton();
        sideBar = userInterface.getSideBarComponent().getSideBarPane();
        newJournalEntryButton.setStyle("-fx-background-color:#787878");
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
    }

    @Override
    protected void initializeFinalPane() {
        pane = new AnchorPane();

        Button submit = setSubmitButton();
        TextField descriptionField = createJournalSetDescriptionField();
        TextField durationField = createJournalSetDurationField();
        ComboBox<String> categoryList = createJournalGenerateCategoryList();

        pane.getChildren().addAll(
                sideBar,
                userInterface.getQuitButton(),
                title,
                createJournalSetDurationLabel(),
                createJournalSetLabel(),
                createJournalSetCategoryLabel(),
                descriptionField,
                categoryList,
                durationField,
                submit);

        setJournalEntrySubmitListener(submit);
    }

    private Button setSubmitButton() {
        Button submit = new Button("Submit");
        AnchorPane.setBottomAnchor(submit, 30.0);
        AnchorPane.setRightAnchor(submit, 30.0);
        return submit;
    }

    public Text createJournalEntrySetTitle() {
        Text title = new Text();
        title.setFont(new Font(UserInterface.TITLE_FONT_SIZE));
        title.setText("Create New Journal Entry");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);
        return title;
    }

    public ComboBox<String> createJournalGenerateCategoryList() {
        categoryListDurationString = new ComboBox<>();
        categoryListCategory = userInterface.getCategoryListScreen().getCategoryObservableList();

        for (Category c : categoryListCategory) {
            categoryListDurationString.getItems().add(c.getDurationString());
        }

        categoryListDurationString.setValue(categoryListCategory.get(0).getDurationString());

        AnchorPane.setTopAnchor(categoryListDurationString, 295.0);
        AnchorPane.setLeftAnchor(categoryListDurationString, 230.0);
        AnchorPane.setRightAnchor(categoryListDurationString, 30.0);
        return categoryListDurationString;
    }

    public Text createJournalSetCategoryLabel() {
        Text categoryLabel = new Text("What category would you like to assign this entry to?");
        categoryLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(categoryLabel, 265.0);
        AnchorPane.setLeftAnchor(categoryLabel, 230.0);
        AnchorPane.setRightAnchor(categoryLabel, 30.0);
        return categoryLabel;
    }

    public TextField createJournalSetDurationField() {
        durationField = new TextField();
        AnchorPane.setTopAnchor(durationField, 215.0);
        AnchorPane.setLeftAnchor(durationField, 230.0);
        AnchorPane.setRightAnchor(durationField, 30.0);
        return durationField;
    }

    public Text createJournalSetDurationLabel() {
        Text durationLabel = new Text("How long did you spend on this? (in minutes)");
        durationLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(durationLabel, 185.0);
        AnchorPane.setLeftAnchor(durationLabel, 230.0);
        AnchorPane.setRightAnchor(durationLabel, 30.0);
        return durationLabel;
    }

    public TextField createJournalSetDescriptionField() {
        descriptionField = new TextField();
        AnchorPane.setTopAnchor(descriptionField, 125.0);
        AnchorPane.setLeftAnchor(descriptionField, 230.0);
        AnchorPane.setRightAnchor(descriptionField, 30.0);
        return descriptionField;
    }

    public Text createJournalSetLabel() {
        Text descriptionLabel = new Text("What did you get up to? Enter a description for your journal entry:");
        descriptionLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(descriptionLabel, 95.0);
        AnchorPane.setLeftAnchor(descriptionLabel, 230.0);
        AnchorPane.setRightAnchor(descriptionLabel, 30.0);
        return descriptionLabel;
    }

    public void setJournalEntrySubmitListener(Button submit) {
        submit.setOnAction(e -> {
            doCategoryEntry();
        });

        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                doCategoryEntry();
            }
        });
    }

    public void doCategoryEntry() {
        if (isFormValidated()) {
            userInputToString();
            userInterface.getSession().createNewJournalEntry(descriptionEntry, durationEntry, categoryEntry);
            alertSuccessfulEntry();
            clearFields();
        }
    }

    private boolean isFormValidated() {
        try {
            userInterface.getSession().checkValidForm(descriptionField.getText(), durationField.getText());
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

    private void userInputToString() {
        descriptionEntry = descriptionField.getText();
        durationEntry = durationField.getText();
        categoryEntry = categoryListCategory.get(
                categoryListDurationString
                        .getSelectionModel()
                        .getSelectedIndex())
                .getName();
    }

    private void clearFields() {
        descriptionField.clear();
        durationField.clear();
        categoryListDurationString.setValue(categoryListCategory.get(0).getDurationString());
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
        a.setContentText("Entry successfully added!");
        a.show();
    }

    private void alertNegativeDuration() {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText("Please enter a positive duration.");
        a.show();
    }
}
package ui.screens;

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

    public JournalEntryCreateScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void createJournalEntryScreen() {
        title = createJournalEntrySetTitle();
        Button newJournalEntryButton = userInterface.getJournalEntryButton();
        sideBar = userInterface.getSideBar().getSideBarPane();
        newJournalEntryButton.setStyle("-fx-background-color:#787878");
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
    }

    @Override
    protected void initializeFinalPane() {
        pane = new AnchorPane();

        Text descriptionLabel = createJournalSetLabel();
        TextField descriptionField = createJournalSetDescriptionField();
        Text durationLabel = createJournalSetDurationLabel();
        TextField durationField = createJournalSetDurationField();
        Text categoryLabel = createJournalSetCategoryLabel();
        ComboBox<String> categoryList = createJournalGenerateCategoryList();

        Button submit = new Button("Submit");
        AnchorPane.setBottomAnchor(submit, 30.0);
        AnchorPane.setRightAnchor(submit, 30.0);

        pane.getChildren().addAll(
                sideBar,
                userInterface.getQuitButton(),
                title,
                durationLabel,
                descriptionLabel,
                categoryLabel,
                descriptionField,
                categoryList,
                durationField,
                submit);

        setJournalEntrySubmitListener(submit, descriptionField, durationField, categoryList);
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
        ComboBox<String> categoryList = new ComboBox<>();
        ObservableList<Category> listToAdd = userInterface.getCategoryListScreen().generateCategoryList();

        for (Category c : listToAdd) {
            categoryList.getItems().add(c.getName());
        }

        categoryList.setValue("Uncategorized");

        AnchorPane.setTopAnchor(categoryList, 295.0);
        AnchorPane.setLeftAnchor(categoryList, 230.0);
        AnchorPane.setRightAnchor(categoryList, 30.0);
        return categoryList;
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
        TextField durationField = new TextField();
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
        TextField descriptionField = new TextField();
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

    public void setJournalEntrySubmitListener(
            Button submit, TextField descriptionField, TextField durationField, ComboBox<String> categoryList) {
        submit.setOnAction(e -> {
            doCategoryEntry(descriptionField, durationField, categoryList);
        });

        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                doCategoryEntry(descriptionField, durationField, categoryList);
            }
        });
    }

    public void doCategoryEntry(TextField descriptionField, TextField durationField, ComboBox<String> categoryList) {
        try {
            String description = descriptionField.getText();
            String duration = durationField.getText();
            String category = categoryList.getSelectionModel().getSelectedItem();
            userInterface.getSession().createNewJournalEntry(description, duration, category);
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("Entry successfully added!");
            a.show();
            descriptionField.clear();
            durationField.clear();
            categoryList.setValue("Uncategorized");
        } catch (NullEntryException e1) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Please make sure to fill in all fields.");
            a.show();
        } catch (NumberFormatException e2) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("You didn't enter a number for the duration. Please try again.");
            a.show();
        }
    }
}
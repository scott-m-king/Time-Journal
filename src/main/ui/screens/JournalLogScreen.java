package ui.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.JournalEntry;
import model.JournalLog;
import ui.UserInterface;

import java.util.ArrayList;
import java.util.Optional;

public class JournalLogScreen extends Screen {
    private final UserInterface userInterface;
    private Pane sideBar;
    private Button journalLogMenuButton;
    private HBox buttonPane;
    private JournalEntry selectedEntry;
    private TableView<JournalEntry> table;
    private Text title;
    private Pane pane;

    public JournalLogScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // MODIFIES: this
    // EFFECTS: runs initialization methods needed to render journal log screen
    public void renderJournalLogScreen() {
        this.sideBar = userInterface.getSideBarComponent().getSideBarPane();
        this.journalLogMenuButton = userInterface.getSideBarComponent().getViewJournalLogButton();
        renderJournalEntryTable();
        initializeFinalPane();

        initializeScreen(pane, userInterface.getMainStage());
    }

    // MODIFIES: this
    // EFFECTS: populates a journal table component to use on this screen
    private void renderJournalEntryTable() {
        userInterface.getJournalTableComponent().renderJournalTable(this);
        this.table = userInterface.getJournalTableComponent().getJournalEntryTableView();
        journalTableSelectionListener();
    }

    // REQUIRES: all required panes to be initialized
    // MODIFIES: this
    // EFFECTS: initializes final pane to load to screen
    @Override
    protected void initializeFinalPane() {
        pane = new AnchorPane();
        createPageTitle();
        createButtonPane();
        journalLogMenuButton.setStyle("-fx-background-color:#787878");
        pane.getChildren().addAll(
                sideBar,
                userInterface.getSideBarComponent().getQuitButton(),
                title,
                table,
                buttonPane);
    }

    // MODIFIES: this
    // EFFECTS: sets main title for screen and sets anchors
    private void createPageTitle() {
        title = new Text();
        title.setFont(new Font(UserInterface.TITLE_FONT_SIZE));
        title.setText("Journal Entry Log");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);
    }

    // MODIFIES: this
    // EFFECTS: creates hbox pane to hold buttons
    private void createButtonPane() {
        buttonPane = makeFormButtons(JOURNAL_LOG, userInterface);
        AnchorPane.setRightAnchor(buttonPane, 30.0);
        AnchorPane.setTopAnchor(buttonPane, 30.0);
    }

    // MODIFIES: UserInterface, JournalEntryCreateScreen
    // EFFECTS: clears button colors and navigates to JournalEntryCreateScreen
    protected void createButtonAction() {
        userInterface.clearButtonColours();
        userInterface.getJournalEntryCreateScreen().renderJournalEntryCreateScreen();
    }

    // MODIFIES: JournalEntryEditPopup
    // EFFECTS: if entry is selected, bring up JournalEntryEditPopup, otherwise do nothing
    protected void editButtonAction() {
        if (selectedEntry != null) {
            try {
                userInterface.getJournalEntryEditPopup().renderJournalEntryEditPopup();
            } catch (NullPointerException e) {
                // do nothing
            }
        }
    }

    // EFFECTS: if entry is selected, ask user if they want to delete entry
    protected void deleteButtonAction() {
        if (selectedEntry != null) {
            deleteConfirmation();
        }
    }

    // EFFECTS: alert shown to user if they want to delete entry. If yes, runs deleteEntry method, else closes alert
    public void deleteConfirmation() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Are you sure you want to delete this entry? This cannot be undone.");
        Optional<ButtonType> result = a.showAndWait();
        if (!result.isPresent() || result.get() == ButtonType.CANCEL) {
            a.close();
        } else if (result.get() == ButtonType.OK) {
            deleteEntry();
        }
    }

    // MODIFIES: UserSession
    // EFFECTS: deletes selected category from UserSession
    public void deleteEntry() {
        if (selectedEntry != null) {
            userInterface.getCurrentSession().deleteJournalEntry(selectedEntry.getJournalID());
            playDeleteSound();
            renderJournalLogScreen();
        }
    }

    // MODIFIES: this
    // EFFECTS: when at least one entry is selected, will change edit and delete button colours
    public void journalTableSelectionListener() {
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                selectedEntry = table.getSelectionModel().getSelectedItem();
                setButtonColors();
            } catch (NullPointerException e) {
                // no action
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: changes button colours depending on if journal entry is selected or not
    private void setButtonColors() {
        if (selectedEntry == null) {
            delete.setStyle("-fx-background-color: #c7c7c7; -fx-min-width: 100;");
            edit.setStyle("-fx-background-color: #c7c7c7; -fx-min-width: 100;");
        } else {
            delete.setStyle("-fx-background-color: #585858; -fx-min-width: 100;");
            edit.setStyle("-fx-background-color: #585858; -fx-min-width: 100;");
        }
    }

    // EFFECTS: returns selected entry from table
    public JournalEntry getSelectedEntry() {
        return table.getSelectionModel().getSelectedItem();
    }

    // EFFECTS: returns observable list of journal entries
    public ObservableList<JournalEntry> getEntries() {
        ObservableList<JournalEntry> entries = FXCollections.observableArrayList();
        JournalLog journalLog = userInterface.getCurrentSession().getJournalLog();
        ArrayList<JournalEntry> entryList = journalLog.getEntriesAsList();
        entries.addAll(entryList);
        return entries;
    }

    // Setter
    public void setJournalEntryCurrentlySelected(JournalEntry entry) {
        selectedEntry = entry;
    }
}
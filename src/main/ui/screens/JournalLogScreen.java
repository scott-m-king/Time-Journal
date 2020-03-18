package ui.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Text title;
    private Pane pane;

    public JournalLogScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void renderJournalLogScreen() {
        this.sideBar = userInterface.getSideBarComponent().getSideBarPane();
        this.journalLogMenuButton = userInterface.getSideBarComponent().getViewJournalLogButton();
        initializeFinalPane();
        journalTableSelectionListener();
        initializeScreen(pane, userInterface.getMainStage());
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
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
                userInterface.getJournalTableView(),
                buttonPane);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void createPageTitle() {
        title = new Text();
        title.setFont(new Font(UserInterface.TITLE_FONT_SIZE));
        title.setText("Journal Entry Log");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void createButtonPane() {
        renderTable();
        buttonPane = makeFormButtons(JOURNAL_LOG, userInterface);
        AnchorPane.setRightAnchor(buttonPane, 30.0);
        AnchorPane.setTopAnchor(buttonPane, 30.0);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    protected void createButtonAction() {
        userInterface.clearButtonColours();
        userInterface.getJournalEntryCreateScreen().renderJournalEntryCreateScreen();
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    protected void editButtonAction() {
        if (selectedEntry != null) {
            try {
                userInterface.getJournalEntryEditPopup().renderJournalEntryEditPopup();
            } catch (NullPointerException e) {
                // do nothing
            }
        }
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    protected void deleteButtonAction() {
        if (selectedEntry != null) {
            deleteConfirmation();
        }
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
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

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void deleteEntry() {
        if (selectedEntry != null) {
            userInterface.getCurrentSession().deleteJournalEntry(selectedEntry.getJournalID());
            playDeleteSound();
            renderJournalLogScreen();
        }
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void journalTableSelectionListener() {
        userInterface.getJournalTableView()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    try {
                        selectedEntry = userInterface.getJournalTableView().getSelectionModel().getSelectedItem();
                        setButtonColors();
                    } catch (NullPointerException e) {
                        // no action
                    }
                });
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void setButtonColors() {
        if (selectedEntry == null) {
            delete.setStyle("-fx-background-color: #c7c7c7; -fx-min-width: 100;");
            edit.setStyle("-fx-background-color: #c7c7c7; -fx-min-width: 100;");
        } else {
            delete.setStyle("-fx-background-color: #585858; -fx-min-width: 100;");
            edit.setStyle("-fx-background-color: #585858; -fx-min-width: 100;");
        }
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public JournalEntry getSelectedEntry() {
        return userInterface.getJournalTableView().getSelectionModel().getSelectedItem();
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public ObservableList<JournalEntry> getEntries() {
        ObservableList<JournalEntry> entries = FXCollections.observableArrayList();
        JournalLog journalLog = userInterface.getCurrentSession().getJournalLog();
        ArrayList<JournalEntry> entryList = journalLog.getEntriesAsList();
        entries.addAll(entryList);
        return entries;
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void renderTable() {
        userInterface.getJournalTableObject().renderJournalTable(this, userInterface);
    }

    // Setter
    public void setJournalEntryCurrentlySelected(JournalEntry entry) {
        selectedEntry = entry;
    }
}
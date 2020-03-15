package ui.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.JournalEntry;
import model.JournalLog;
import ui.UserInterface;

import java.util.ArrayList;

public class JournalLogScreen extends Screen {
    private final UserInterface userInterface;
    private Pane sideBar;
    private Button journalLogMenuButton;
    private Button createNew;
    private Button delete;
    private Button edit;
    private HBox buttonPane;
    private JournalEntry selectedEntry;
    private Text title;
    private Pane pane;

    public JournalLogScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderJournalLogScreen() {
        this.sideBar = userInterface.getSideBarComponent().getSideBarPane();
        this.journalLogMenuButton = userInterface.getSideBarComponent().getViewJournalLogButton();
        initializeFinalPane();
        journalTableSelectionListener();
        initializeScreen(pane, userInterface.getMainStage());
    }

    @Override
    protected void initializeFinalPane() {
        pane = new AnchorPane();
        createPageTitle();
        createButtonPane();
        journalLogMenuButton.setStyle("-fx-background-color:#787878");
        pane.getChildren().addAll(
                sideBar,
                userInterface.getQuitButton(),
                title,
                userInterface.getJournalTableView(),
                buttonPane);
    }

    private void createPageTitle() {
        title = new Text();
        title.setFont(new Font(UserInterface.TITLE_FONT_SIZE));
        title.setText("Journal Entry Log");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);
    }

    private void createButtonPane() {
        renderTable();
        setJournalLogButtons();
        buttonPane.setSpacing(15.0);
        AnchorPane.setRightAnchor(buttonPane, 30.0);
        AnchorPane.setTopAnchor(buttonPane, 30.0);
    }

    public void setJournalLogButtons() {
        buttonPane = new HBox();

        createNew = new Button("Create");
        createNew.setStyle("-fx-min-width: 100;");

        delete = new Button("Delete");
        delete.setStyle("-fx-min-width: 100;");

        edit = new Button("Edit");
        edit.setStyle("-fx-min-width: 100;");

        buttonPane.getChildren().addAll(edit, delete, createNew);
        setJournalLogButtonListeners();
    }

    public void setJournalLogButtonListeners() {
        createNew.setOnAction(e -> {
            userInterface.clearButtonColours();
            userInterface
                    .getJournalEntryCreateScreen()
                    .createJournalEntryScreen();
        });

        edit.setOnAction(e -> {
            userInterface.getJournalEntryEditPopup().renderJournalEntryEditPopup();
        });

        delete.setOnAction(e -> {
            System.out.println(getSelectedEntry().getDescription());
        });
    }

    public void journalTableSelectionListener() {
        userInterface.getJournalTableView()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    try {
                        selectedEntry = userInterface.getJournalTableView().getSelectionModel().getSelectedItem();
                    } catch (NullPointerException e) {
                        // no action
                    }
                });
    }

    public JournalEntry getSelectedEntry() {
        return userInterface.getJournalTableView().getSelectionModel().getSelectedItem();
    }

    public ObservableList<JournalEntry> getEntries() {
        ObservableList<JournalEntry> entries = FXCollections.observableArrayList();
        JournalLog journalLog = userInterface.getSession().getJournalLog();
        ArrayList<JournalEntry> entryList = journalLog.getEntriesAsList();
        entries.addAll(entryList);
        return entries;
    }

    public void renderTable() {
        userInterface.getJournalTableObject().renderJournalTable(this, userInterface);
    }
}
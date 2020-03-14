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
    private Pane pane;

    public JournalLogScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderJournalLogScreen() {
        this.sideBar = userInterface.getSideBar().getSideBarPane();
        this.journalLogMenuButton = userInterface.getSideBar().getViewJournalLogButton();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
    }

    @Override
    protected void initializeFinalPane() {
        pane = new AnchorPane();

        Text title = new Text();
        title.setFont(new Font(UserInterface.TITLE_FONT_SIZE));
        title.setText("Journal Entry Log");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        journalLogMenuButton.setStyle("-fx-background-color:#787878");

        setJournalTableColumns();

        HBox buttons = setJournalLogButtons(sideBar);
        buttons.setSpacing(15.0);
        AnchorPane.setRightAnchor(buttons, 30.0);
        AnchorPane.setTopAnchor(buttons, 30.0);

        pane.getChildren().addAll(
                sideBar,
                userInterface.getQuitButton(),
                title,
                userInterface.getJournalTableView(),
                buttons);
    }

    public HBox setJournalLogButtons(Pane sideBar) {
        HBox buttons = new HBox();

        Button createNew = new Button("Create");
        createNew.setStyle("-fx-min-width: 100;");
        Button delete = new Button("Delete");

        delete.setStyle("-fx-min-width: 100;");
        Button edit = new Button("Edit");

        edit.setStyle("-fx-min-width: 100;");
        buttons.getChildren().addAll(edit, delete, createNew);

        setJournalLogButtonListeners(createNew, edit);
        return buttons;
    }

    public void setJournalLogButtonListeners(Button createNew, Button edit) {
        createNew.setOnAction(e -> {
            userInterface.clearButtonColours();
            userInterface
                    .getJournalEntryCreateScreen()
                    .createJournalEntryScreen();
        });

        edit.setOnAction(e -> {
            ObservableList<JournalEntry> entrySelected = userInterface.getJournalTableView().getSelectionModel().getSelectedItems();
            System.out.println(entrySelected.get(0).getDescription());
        });
    }

    public ObservableList<JournalEntry> getEntries() {
        ObservableList<JournalEntry> entries = FXCollections.observableArrayList();
        JournalLog journalLog = userInterface.getSession().getJournalLog();
        ArrayList<JournalEntry> entryList = journalLog.getEntriesAsList();
        entries.addAll(entryList);
        return entries;
    }

    public void setJournalTableColumns() {
        userInterface.getJournalTable().renderJournalTable(this, userInterface);
    }
}
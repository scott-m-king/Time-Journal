package ui.components;

import javafx.beans.binding.Bindings;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.JournalEntry;
import ui.UserInterface;
import ui.screens.JournalLogScreen;

public class JournalTableComponent {
    private TableView<JournalEntry> journalEntryTableView;
    private TableColumn<JournalEntry, Integer> idColumn;
    private TableColumn<JournalEntry, String> descriptionColumn;
    private TableColumn<JournalEntry, Integer> durationColumn;
    private TableColumn<JournalEntry, String> categoryColumn;
    private TableColumn<JournalEntry, String> dateColumn;

    public JournalTableComponent() {
        initializeFields();
    }

    // EFFECTS: initializes all fields in JournalTable object
    public void initializeFields() {
        idColumn();
        descriptionColumn();
        durationColumn();
        categoryColumn();
        dateColumn();
    }

    // MODIFIES: this
    // EFFECTS: initializes 'Journal ID' field
    public void idColumn() {
        idColumn = new TableColumn<>("Journal ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("journalID"));
    }

    // MODIFIES: this
    // EFFECTS: initializes 'Description' field
    public void descriptionColumn() {
        descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(315);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    // MODIFIES: this
    // EFFECTS: initializes 'Duration' field
    public void durationColumn() {
        durationColumn = new TableColumn<>("Duration");
        durationColumn.setMinWidth(100);
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }

    // MODIFIES: this
    // EFFECTS: initializes 'Category' field
    public void categoryColumn() {
        categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(100);
        categoryColumn.setCellValueFactory(cellData -> Bindings.selectString(
                cellData.getValue().getCategory(), "name"));
    }

    // MODIFIES: this
    // EFFECTS: initializes 'Date' field
    public void dateColumn() {
        dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(100);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    // MODIFIES: this
    // REQUIRES: an active JournalLogScreen and UserInterface
    // EFFECTS: renders JournalLog table with fields
    public void renderJournalTable(JournalLogScreen journalLogScreen) {
        journalEntryTableView = new TableView<>();
        journalEntryTableView.setItems(journalLogScreen.getEntries());
        journalEntryTableView.getColumns().addAll(
                idColumn,
                dateColumn,
                categoryColumn,
                durationColumn,
                descriptionColumn
        );
        setPlaceholderAndAnchors();
    }

    // MODIFIES: this
    // REQUIRES: an instantiated table
    // EFFECTS: sets the placeholder text of the table and sets anchors to fill screen
    private void setPlaceholderAndAnchors() {
        journalEntryTableView.setPlaceholder(new Text("No journal entries yet!"));
        AnchorPane.setTopAnchor(journalEntryTableView, 95.0);
        AnchorPane.setBottomAnchor(journalEntryTableView, 30.0);
        AnchorPane.setLeftAnchor(journalEntryTableView, 230.0);
        AnchorPane.setRightAnchor(journalEntryTableView, 30.0);
    }

    // Getters
    public TableColumn<JournalEntry, Integer> getIdColumn() {
        return idColumn;
    }

    public TableColumn<JournalEntry, String> getDescriptionColumn() {
        return descriptionColumn;
    }

    public TableColumn<JournalEntry, Integer> getDurationColumn() {
        return durationColumn;
    }

    public TableColumn<JournalEntry, String> getCategoryColumn() {
        return categoryColumn;
    }

    public TableColumn<JournalEntry, String> getDateColumn() {
        return dateColumn;
    }

    public TableView<JournalEntry> getJournalEntryTableView() {
        return journalEntryTableView;
    }
}
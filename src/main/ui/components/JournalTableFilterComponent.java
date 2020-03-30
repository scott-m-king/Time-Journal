package ui.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.JournalEntry;
import ui.UserInterface;

import java.util.List;
import java.util.stream.Collectors;

// Represents a class that generates a filtered TableView of JournalEntries based on a filter condition
public class JournalTableFilterComponent {
    private UserInterface userInterface;
    private TableColumn<JournalEntry, String> dateTableColumn;
    private TableColumn<JournalEntry, String> categoryTableColumn;
    private TableColumn<JournalEntry, Integer> durationTableColumn;
    private TableColumn<JournalEntry, String> descriptionTableColumn;
    private ObservableList<JournalEntry> journalEntryObservableList;
    private TableView<JournalEntry> categoryJournalTable;
    private String categoryCurrentSelected;

    public JournalTableFilterComponent(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // REQUIRES: string representing category currently selected that exists in the user's list of CategoryList
    // MODIFIES: this
    // EFFECTS: returns TableView of JournalEntry that has been filtered according to selected category
    public TableView<JournalEntry> renderFilteredJournalTable(String categoryCurrentSelected) {
        setJournalEntryFieldColumns();
        this.categoryCurrentSelected = categoryCurrentSelected;
        filterEntriesBasedOnCategory();
        return categoryJournalTable;
    }

    // REQUIRES: empty JavaFX ObservableList
    // EFFECTS: returns an empty tableview
    public TableView<JournalEntry> renderFilteredJournalTable(ObservableList<JournalEntry> list) {
        setJournalEntryFieldColumns();
        return createFilteredJournalEntryTable(list);
    }

    // Reference: using .filter() and Java streams https://mkyong.com/java8/java-8-streams-filter-examples/
    // REQUIRES: a valid category to filter
    // MODIFIES: this
    // EFFECTS: generates a filtered List<JournalEntry> based on category filter condition
    public void filterEntriesBasedOnCategory() {
        final String[] filterCondition = new String[1];
        filterCondition[0] = categoryCurrentSelected;
        ObservableList<JournalEntry> entriesToFilter = userInterface.getJournalLogScreen().getEntries();
        List<JournalEntry> filterResult = entriesToFilter.stream()
                .filter(journalEntry -> filterCondition[0].equals(journalEntry.getCategory().getName()))
                .collect(Collectors.toList());
        filteredTable(filterCondition[0], filterResult);
    }

    // REQUIRES: a filtered list of journal entries
    // MODIFIES: this
    // EFFECTS: if no filter results, display placeholder text for no entries
    //          otherwise, display the filtered list of journal entries
    private void filteredTable(String filterCondition, List<JournalEntry> filterResult) {
        if (filterResult.size() == 0) {
            categoryJournalTable = createFilteredJournalEntryTable(filterResult);
            categoryJournalTable.setPlaceholder(new Text("No entries for " + filterCondition));
            categoryJournalTable.setSelectionModel(null);
        } else {
            categoryJournalTable = createFilteredJournalEntryTable(filterResult);
        }
    }

    // REQUIRES: journal table columns to be instantiated
    // MODIFIES: this
    // EFFECTS: sets column fields for use in filtered journal entry table
    public void setJournalEntryFieldColumns() {
        dateTableColumn = userInterface.getJournalTableComponent().getDateColumn();
        categoryTableColumn = userInterface.getJournalTableComponent().getCategoryColumn();
        durationTableColumn = userInterface.getJournalTableComponent().getDurationColumn();
        descriptionTableColumn = userInterface.getJournalTableComponent().getDescriptionColumn();
    }

    // REQUIRES: populated list of journal entries
    // MODIFIES: this
    // EFFECTS: returns the rendered filtered journal entry table
    public TableView<JournalEntry> createFilteredJournalEntryTable(List<JournalEntry> entries) {
        categoryJournalTable = new TableView<>();
        journalEntryObservableList = FXCollections.observableArrayList();
        journalEntryObservableList.addAll(entries);
        constructCategoryJournalEntryTable();
        return categoryJournalTable;
    }

    // REQUIRES: ObservableList of journal entries
    // MODIFIES: this
    // EFFECTS: adds columns and entries to table, sets anchors
    @SuppressWarnings("unchecked")
    public void constructCategoryJournalEntryTable() {
        categoryJournalTable.setItems(journalEntryObservableList);
        categoryJournalTable.getStyleClass().add("category-journal-table");
        categoryJournalTable.getColumns().addAll(
                dateTableColumn, categoryTableColumn, durationTableColumn, descriptionTableColumn);
        AnchorPane.setTopAnchor(categoryJournalTable, 385.0);
        AnchorPane.setBottomAnchor(categoryJournalTable, 30.0);
        AnchorPane.setRightAnchor(categoryJournalTable, 30.0);
        AnchorPane.setLeftAnchor(categoryJournalTable, 230.0);
    }
}

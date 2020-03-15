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
    private TableColumn<JournalEntry, Integer> idColumn;
    private TableColumn<JournalEntry, String> descriptionColumn;
    private TableColumn<JournalEntry, Integer> durationColumn;
    private TableColumn<JournalEntry, String> categoryColumn;
    private TableColumn<JournalEntry, String> dateColumn;

    public JournalTableComponent() {
        initializeFields();
    }

    public void initializeFields() {
        idColumn();
        descriptionColumn();
        durationColumn();
        categoryColumn();
        dateColumn();
    }

    public void idColumn() {
        idColumn = new TableColumn<>("Journal ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("journalID"));
    }

    public void descriptionColumn() {
        descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(315);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    public void durationColumn() {
        durationColumn = new TableColumn<>("Duration");
        durationColumn.setMinWidth(100);
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }

    public void categoryColumn() {
        categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(100);
        categoryColumn.setCellValueFactory(cellData -> Bindings.selectString(
                cellData.getValue().getCategory(), "name"));
    }

    public void dateColumn() {
        dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(100);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    public void renderJournalTable(JournalLogScreen journalLogScreen, UserInterface userInterface) {
        userInterface.setJournalTableView(new TableView<>());
        userInterface.getJournalTableView().setItems(journalLogScreen.getEntries());
        userInterface.getJournalTableView().getColumns().addAll(idColumn, dateColumn,
                categoryColumn, durationColumn, descriptionColumn);
        userInterface.getJournalTableView().setPlaceholder(new Text("No journal entries yet!"));
        AnchorPane.setTopAnchor(userInterface.getJournalTableView(), 95.0);
        AnchorPane.setBottomAnchor(userInterface.getJournalTableView(), 30.0);
        AnchorPane.setLeftAnchor(userInterface.getJournalTableView(), 230.0);
        AnchorPane.setRightAnchor(userInterface.getJournalTableView(), 30.0);
    }

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
}
package ui.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Category;
import model.CategoryList;
import model.JournalEntry;
import ui.UserInterface;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryListScreen extends Screen {
    private final UserInterface userInterface;
    private ListView<String> categoryListView;
    private TableView<JournalEntry> categoryJournalTable;
    private Pane sideBar;
    private String categoryCurrentSelected;
    private ObservableList<JournalEntry> journalEntryObservableList;
    private ObservableList<Category> categoryObservableList;
    private TableColumn<JournalEntry, String> dateTableColumn;
    private TableColumn<JournalEntry, String> categoryTableColumn;
    private TableColumn<JournalEntry, Integer> durationTableColumn;
    private TableColumn<JournalEntry, String> descriptionTableColumn;
    private Text title;
    private HBox buttons;
    private Pane pane;

    public CategoryListScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderCategoryListScreen() {
        categoryCurrentSelected = null;
        this.sideBar = userInterface.getSideBarComponent().getSideBarPane();
        Button categoriesMenuButton = userInterface.getSideBarComponent().getViewCategoryListButton();
        categoriesMenuButton.setStyle("-fx-background-color:#787878");
        setJournalEntryFieldColumns();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
    }

    @Override
    protected void initializeFinalPane() {
        pane = new AnchorPane();
        setMainLabel();
        generateCategoryDurationListView();
        setButtonLayout();
        categoryTableListener();
        pane.getChildren().addAll(
                sideBar,
                userInterface.getQuitButton(),
                title,
                categoryListView,
                categoryJournalTable, buttons);
    }

    private void setMainLabel() {
        title = new Text();
        title.setFont(new Font(UserInterface.TITLE_FONT_SIZE));
        title.setText("Category List");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);
    }

    private void setButtonLayout() {
        buttons = makeFormButtons(CATEGORY_LIST, userInterface);
        AnchorPane.setRightAnchor(buttons, 30.0);
        AnchorPane.setTopAnchor(buttons, 30.0);
    }

    private void setButtonColors() {
        if (categoryCurrentSelected == null) {
            delete.setStyle("-fx-background-color: #c7c7c7; -fx-min-width: 100;");
            edit.setStyle("-fx-background-color: #c7c7c7; -fx-min-width: 100;");
        } else {
            delete.setStyle("-fx-background-color: #585858; -fx-min-width: 100;");
            edit.setStyle("-fx-background-color: #585858; -fx-min-width: 100;");
        }
    }

    public void createButtonAction() {
        userInterface.getCreateCategoryPopup().renderCategoryPopup();
    }

    public void deleteButtonAction() {
        if (categoryCurrentSelected == null) {
            return;
        }
        if (!categoryCurrentSelected.equals("Uncategorized")) {
            userInterface.getCategoryListScreen().confirmCategoryDelete();
        } else {
            invalidCategoryDeleteAlert();
        }
    }

    public void deleteCategory() {
        if (categoryCurrentSelected != null) {
            userInterface.getSession().deleteCategory(categoryCurrentSelected);
            userInterface.viewAllCategories();
        }
    }

    public void confirmCategoryDelete() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Are you sure you want to delete this category? This cannot be undone.");
        Optional<ButtonType> result = a.showAndWait();
        if (!result.isPresent() || result.get() == ButtonType.CANCEL) {
            a.close();
        } else if (result.get() == ButtonType.OK) {
            deleteCategory();
        }
    }

    public void invalidCategoryDeleteAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Sorry, you cannot delete the Uncategorized category.");
        a.show();
    }

    public void editButtonAction() {
        if (categoryCurrentSelected == null) {
            return;
        }
        if (!categoryCurrentSelected.equals("Uncategorized")) {
            userInterface.getEditCategoryPopup().initializeScreen();
        } else {
            invalidCategoryEditAlert();
        }
    }

    public void invalidCategoryEditAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Cannot modify the Uncategorized category.");
        a.show();
    }

    public void generateCategoryDurationListView() {
        categoryListView = new ListView<>();
        generateCategoryList();

        for (Category category : categoryObservableList) {
            categoryListView.getItems().add(category.getDurationString());
        }
        renderCategoryDurationListView();
    }

    public void generateCategoryList() {
        categoryObservableList = FXCollections.observableArrayList();
        CategoryList categoryList = userInterface.getSession().getCategoryList();
        for (int i = 0; i < categoryList.getSize(); i++) {
            categoryObservableList.add(categoryList.get(i));
        }
    }

    private void renderCategoryDurationListView() {
        categoryListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        categoryListView.setMaxHeight(275);
        AnchorPane.setLeftAnchor(categoryListView, 230.0);
        AnchorPane.setTopAnchor(categoryListView, 95.0);
        AnchorPane.setRightAnchor(categoryListView, 30.0);
        categoryJournalTable = renderFilteredJournalEntryTable(userInterface.getJournalLogScreen().getEntries());
    }

    public void categoryTableListener() {
        categoryListView.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        try {
                            int index = categoryListView.getSelectionModel().getSelectedIndex();
                            categoryCurrentSelected = userInterface.getSession().getCategoryList().get(index).getName();
                            setButtonColors();
                            filterEntriesBasedOnCategory();
                            pane.getChildren().clear();
                            pane.getChildren().addAll(sideBar, userInterface.getQuitButton(), title, categoryListView,
                                    categoryJournalTable, buttons);
                        } catch (IndexOutOfBoundsException exception) {
                            // no action
                        }
                    }
            );
    }

    public void filterEntriesBasedOnCategory() {
        final String[] filterCondition = new String[1];
        filterCondition[0] = categoryCurrentSelected;
        ObservableList<JournalEntry> entriesToFilter = userInterface.getJournalLogScreen().getEntries();
        List<JournalEntry> filterResult = entriesToFilter.stream()
                .filter(journalEntry -> filterCondition[0].equals(journalEntry.getCategory().getName()))
                .collect(Collectors.toList());
        filteredTable(filterCondition[0], filterResult);
    }

    private void filteredTable(String filterCondition, List<JournalEntry> filterResult) {
        if (filterResult.size() == 0) {
            categoryJournalTable = renderFilteredJournalEntryTable(filterResult);
            categoryJournalTable.setPlaceholder(new Text("No entries for " + filterCondition));
            categoryJournalTable.setSelectionModel(null);
        } else {
            categoryJournalTable = renderFilteredJournalEntryTable(filterResult);
        }
    }

    public void setJournalEntryFieldColumns() {
        dateTableColumn = userInterface.getJournalTableObject().getDateColumn();
        categoryTableColumn = userInterface.getJournalTableObject().getCategoryColumn();
        durationTableColumn = userInterface.getJournalTableObject().getDurationColumn();
        descriptionTableColumn = userInterface.getJournalTableObject().getDescriptionColumn();
    }

    public TableView<JournalEntry> renderFilteredJournalEntryTable(List<JournalEntry> entries) {
        categoryJournalTable = new TableView<>();
        journalEntryObservableList = FXCollections.observableArrayList();
        journalEntryObservableList.addAll(entries);
        constructCategoryJournalEntryTable();
        return categoryJournalTable;
    }

    public void constructCategoryJournalEntryTable() {
        categoryJournalTable.setItems(journalEntryObservableList);
        categoryJournalTable.getColumns().addAll(
                dateTableColumn, categoryTableColumn, durationTableColumn, descriptionTableColumn);
        AnchorPane.setTopAnchor(categoryJournalTable, 385.0);
        AnchorPane.setBottomAnchor(categoryJournalTable, 30.0);
        AnchorPane.setRightAnchor(categoryJournalTable, 30.0);
        AnchorPane.setLeftAnchor(categoryJournalTable, 230.0);
    }

    public Pane getPane() {
        return pane;
    }

    public String getCategoryCurrentSelected() {
        return categoryCurrentSelected;
    }

    public void setCategoryCurrentSelected(String categoryName) {
        categoryCurrentSelected = categoryName;
    }

    public ObservableList<Category> getCategoryObservableList() {
        generateCategoryList();
        return categoryObservableList;
    }
}
package ui.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Category;
import model.CategoryList;
import model.JournalEntry;
import ui.UserInterface;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryListScreen extends Screen {
    final UserInterface userInterface;
    private ListView<String> categoryDurationList;
    private TableView<JournalEntry> categoryJournalTable;
    private Pane sideBar;
    private Button categoriesMenuButton;
    private Pane pane;

    public CategoryListScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderJournalLogScreen() {
        this.sideBar = userInterface.getSideBarComponent().getSideBarPane();
        this.categoriesMenuButton = userInterface.getSideBarComponent().getViewCategoryListButton();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
    }

    @Override
    protected void initializeFinalPane() {
        pane = new AnchorPane();
        userInterface.setCategoryCurrentlySelected(null);

        Text title = new Text();
        title.setFont(new Font(UserInterface.TITLE_FONT_SIZE));
        title.setText("Category List");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        ObservableList<Category> observableList = generateCategoryDurationListView();
        HBox buttons = setCategoryLogButtons();
        buttons.setSpacing(15.0);
        AnchorPane.setRightAnchor(buttons, 30.0);
        AnchorPane.setTopAnchor(buttons, 30.0);

        // string to filter with
        final String[] toFilter = new String[1];
        categoryTableListener(sideBar, title, toFilter, observableList, (AnchorPane) pane, buttons);

        pane.getChildren().addAll(
                sideBar,
                userInterface.getQuitButton(),
                title,
                categoryDurationList,
                categoryJournalTable, buttons);

        categoriesMenuButton.setStyle("-fx-background-color:#787878");
    }

    public ObservableList<Category> generateCategoryDurationListView() {
        categoryDurationList = new ListView<>();
        ObservableList<Category> observableList = generateCategoryList();

        for (Category category : observableList) {
            categoryDurationList.getItems().add(category.getDurationString());
        }

        categoryDurationList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        categoryDurationList.setMaxHeight(275);
        AnchorPane.setLeftAnchor(categoryDurationList, 230.0);
        AnchorPane.setTopAnchor(categoryDurationList, 95.0);
        AnchorPane.setRightAnchor(categoryDurationList, 30.0);
        categoryJournalTable = makeCategoryJournalEntryTable(userInterface.getJournalLogScreen().getEntries());

        return observableList;
    }


    public void categoryTableListener(Pane sideBar, Text title,
                                      String[] toFilter, ObservableList<Category> observableList,
                                      AnchorPane pane, HBox buttons) {
        categoryDurationList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        int index = categoryDurationList.getSelectionModel().getSelectedIndex();
                        userInterface.setCategoryCurrentlySelected(
                                userInterface.getSession().getCategoryList().get(index).getName());

                        toFilter[0] = observableList.get(index).getName();
                        filterList(toFilter[0]);

                        pane.getChildren().clear();
                        pane.getChildren().addAll(sideBar, userInterface.getQuitButton(), title, categoryDurationList,
                                categoryJournalTable, buttons);
                    } catch (IndexOutOfBoundsException exception) {
                        // no action
                    }
                }
        );
    }

    public void filterList(String filterCondition) {
        ObservableList<JournalEntry> entriesToFilter = userInterface.getJournalLogScreen().getEntries();
        List<JournalEntry> result = entriesToFilter.stream()
                .filter(journalEntry -> filterCondition.equals(journalEntry.getCategory().getName()))
                .collect(Collectors.toList());
        if (result.size() == 0) {
            categoryJournalTable = makeCategoryJournalEntryTable(result);
            categoryJournalTable.setPlaceholder(new Text("No entries for " + filterCondition));
        } else {
            categoryJournalTable = makeCategoryJournalEntryTable(result);
        }
    }

    public ObservableList<Category> generateCategoryList() {
        ObservableList<Category> observableCategoryList = FXCollections.observableArrayList();
        CategoryList categoryList = userInterface.getSession().getCategoryList();
        for (int i = 0; i < categoryList.getSize(); i++) {
            observableCategoryList.add(categoryList.get(i));
        }
        return observableCategoryList;
    }

    public TableView<JournalEntry> makeCategoryJournalEntryTable(List<JournalEntry> entries) {
        categoryJournalTable = new TableView<>();
        ObservableList<JournalEntry> observableList = FXCollections.observableArrayList();
        observableList.addAll(entries);
        return renderCategoryJournalEntryTable(
                observableList,
                userInterface.getJournalTableObject().getDateColumn(),
                userInterface.getJournalTableObject().getCategoryColumn(),
                userInterface.getJournalTableObject().getDurationColumn(),
                userInterface.getJournalTableObject().getDescriptionColumn());
    }

    public TableView<JournalEntry> renderCategoryJournalEntryTable(
            ObservableList<JournalEntry> observableList,
            TableColumn<JournalEntry, String> dateTableColumn,
            TableColumn<JournalEntry, String> categoryTableColumn,
            TableColumn<JournalEntry, Integer> durationTableColumn,
            TableColumn<JournalEntry, String> descriptionTableColumn) {
        categoryJournalTable.setItems(observableList);
        categoryJournalTable.getColumns().addAll(
                dateTableColumn, categoryTableColumn, durationTableColumn, descriptionTableColumn);
        AnchorPane.setTopAnchor(categoryJournalTable, 385.0);
        AnchorPane.setBottomAnchor(categoryJournalTable, 30.0);
        AnchorPane.setRightAnchor(categoryJournalTable, 30.0);
        AnchorPane.setLeftAnchor(categoryJournalTable, 230.0);
        return categoryJournalTable;
    }

    public HBox setCategoryLogButtons() {
        HBox buttons = new HBox();

        Button createNew = new Button("Create");
        createNew.setStyle("-fx-min-width: 100;");

        Button delete = new Button("Delete");
        delete.setStyle("-fx-min-width: 100;");

        Button edit = new Button("Edit");
        edit.setStyle("-fx-min-width: 100;");

        buttons.getChildren().addAll(edit, delete, createNew);
        setCategoryButtonListeners(createNew, delete, edit);
        return buttons;
    }

    public void setCategoryButtonListeners(Button createNew, Button delete, Button edit) {
        createNew.setOnAction(e -> userInterface.getCreateCategoryPopup().renderCategoryPopup());

        delete.setOnAction(e -> {
            if (userInterface.getCategoryCurrentlySelected() == null) {
                return;
            }
            if (!userInterface.getCategoryCurrentlySelected().equals("Uncategorized")) {
                userInterface.getCategoryListScreen().confirmCategoryDelete();
            } else {
                invalidCategoryDeleteAlert();
            }
        });

        edit.setOnAction(e -> {
            if (userInterface.getCategoryCurrentlySelected() == null) {
                return;
            }
            if (!userInterface.getCategoryCurrentlySelected().equals("Uncategorized")) {
                userInterface.getEditCategoryPopup().initializeScreen();
            } else {
                invalidCategoryEditAlert();
            }
        });
    }

    public void invalidCategoryEditAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Cannot modify the Uncategorized category.");
        a.show();
    }

    public void invalidCategoryDeleteAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Sorry, you cannot delete the Uncategorized category.");
        a.show();
    }

    public void deleteCategory() {
        if (userInterface.getCategoryCurrentlySelected() != null) {
            userInterface.getSession().deleteCategory(userInterface.getCategoryCurrentlySelected());
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

    public Pane getPane() {
        return pane;
    }
}
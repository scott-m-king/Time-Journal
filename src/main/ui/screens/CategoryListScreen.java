package ui.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.JournalEntry;
import ui.UserInterface;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Represents a Category List Screen to view/edit/delete all categories and view associated journal entries
public class CategoryListScreen extends Screen {
    private final UserInterface userInterface;
    private ListView<String> categoryListView;
    private TableView<JournalEntry> categoryJournalTable;
    private Pane sideBar;
    private String categoryCurrentSelected;
    private Text title;
    private HBox buttons;
    private Pane pane;

    public CategoryListScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // MODIFIES: this
    // EFFECTS: runs methods needed to render category list screen
    public void renderCategoryListScreen() {
        categoryCurrentSelected = null;
        this.sideBar = userInterface.getSideBarComponent().getSideBarPane();
        Button categoriesMenuButton = userInterface.getSideBarComponent().getViewCategoryListButton();
        categoriesMenuButton.setStyle("-fx-background-color:#787878");
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
    }

    // MODIFIES: this
    // EFFECTS: populates the final pane to load to the scene
    @Override
    protected void initializeFinalPane() {
        pane = new AnchorPane();
        setMainLabel();
        renderCategoryDurationListView();
        setButtonLayout();
        categoryTableListener();
        pane.getChildren().addAll(
                sideBar,
                userInterface.getSideBarComponent().getQuitButton(),
                title,
                categoryListView,
                categoryJournalTable,
                buttons);
    }

    // MODIFIES: this
    // EFFECTS: sets and anchors main page label text
    private void setMainLabel() {
        title = new Text();
        title.setFont(new Font(UserInterface.TITLE_FONT_SIZE));
        title.setText("Category List");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);
    }

    // MODIFIES: this
    // EFFECTS: creates HBox pane for top buttons and sets anchors - implementation in abstract class
    private void setButtonLayout() {
        buttons = makeFormButtons(CATEGORY_LIST, userInterface);
        AnchorPane.setRightAnchor(buttons, 30.0);
        AnchorPane.setTopAnchor(buttons, 30.0);
        setButtonColors();
    }

    // MODIFIES: this
    // EFFECTS: sets the background color and min width of the buttons
    private void setButtonColors() {
        if (categoryCurrentSelected == null) {
            delete.setStyle("-fx-background-color: #c7c7c7; -fx-min-width: 100;");
            edit.setStyle("-fx-background-color: #c7c7c7; -fx-min-width: 100;");
        } else {
            delete.setStyle("-fx-background-color: #585858; -fx-min-width: 100;");
            edit.setStyle("-fx-background-color: #585858; -fx-min-width: 100;");
        }
    }

    // EFFECTS: when create button pressed, render Category Popup screen
    public void createButtonAction() {
        userInterface.getCreateCategoryPopup().renderCategoryPopup();
    }

    // EFFECTS: when delete button pressed, if category is selected and is not 'uncategorized' show confirm delete alert
    //          otherwise, do nothing
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

    // REQUIRES: non-null and not 'Uncategorized' selected category
    // EFFECTS: asks the user if they want to delete the selected category
    public void confirmCategoryDelete() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Are you sure you want to delete this category? All entries assigned with '"
                + categoryCurrentSelected
                + "' will be reassigned to 'Uncategorized'. This cannot be undone.");
        Optional<ButtonType> result = a.showAndWait();
        if (!result.isPresent() || result.get() == ButtonType.CANCEL) {
            a.close();
        } else if (result.get() == ButtonType.OK) {
            deleteCategory();
        }
    }

    // EFFECTS: when delete button pressed, if category selected is not null, delete selected category
    //          otherwise, do nothing
    public void deleteCategory() {
        if (categoryCurrentSelected != null) {
            userInterface.getCurrentSession().deleteCategory(categoryCurrentSelected);
            playDeleteSound();
            userInterface.getCategoryListScreen().renderCategoryListScreen();
        }
    }

    // EFFECTS: alerts user that they cannot delete the Uncategorized category
    public void invalidCategoryDeleteAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Sorry, you cannot delete the Uncategorized category.");
        a.show();
    }

    // EFFECTS: when edit button pressed, if category is selected and is not 'uncategorized' show confirm edit alert
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

    // EFFECTS: alerts user that they cannot modify the Uncategorized category
    public void invalidCategoryEditAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Cannot modify the Uncategorized category.");
        a.show();
    }

    // MODIFIES: this
    // EFFECTS: renders empty category duration list view and anchors it to bottom of this screen
    private void renderCategoryDurationListView() {
        categoryListView = userInterface.getCategoryListComponent().getCategoryListView();
        setCategoryListDimensions();
        categoryJournalTable = userInterface
                .getJournalTableFilterComponent()
                .renderFilteredJournalTable(FXCollections.observableArrayList());
        categoryJournalTable.setPlaceholder(new Text("Select a category to see entries."));
    }

    private void setCategoryListDimensions() {
        categoryListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        categoryListView.setMaxHeight(275);
        AnchorPane.setLeftAnchor(categoryListView, 230.0);
        AnchorPane.setTopAnchor(categoryListView, 95.0);
        AnchorPane.setRightAnchor(categoryListView, 30.0);
    }

    // REQUIRES: category ListView with at least one element
    // MODIFIES: this
    // EFFECTS: adds listener to trigger filter function whenever selection is changed
    public void categoryTableListener() {
        categoryListView.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((observable, oldValue, newValue) -> {
                        try {
                            filterEntries();
                        } catch (IndexOutOfBoundsException exception) {
                            // no action
                        }
                    }
            );
    }

    // MODIFIES: this
    // EFFECTS: initiates the filter function with categoryCurrentlySelected as filter requirement
    //          adds clears final pane and reloads it with the filter result
    private void filterEntries() {
        int index = categoryListView.getSelectionModel().getSelectedIndex();
        categoryCurrentSelected =
                userInterface
                .getCurrentSession()
                .getCategoryList()
                .get(index)
                .getName();
        setButtonColors();
        categoryJournalTable = userInterface
                .getJournalTableFilterComponent()
                .renderFilteredJournalTable(categoryCurrentSelected);
        pane.getChildren().clear();
        pane.getChildren().addAll(
                sideBar,
                userInterface.getSideBarComponent().getQuitButton(),
                title,
                categoryListView,
                categoryJournalTable,
                buttons);
    }

    // Getters
    public Pane getPane() {
        return pane;
    }

    public String getCategoryCurrentSelected() {
        return categoryCurrentSelected;
    }

    public void setCategoryCurrentSelected(String categoryName) {
        categoryCurrentSelected = categoryName;
    }
}
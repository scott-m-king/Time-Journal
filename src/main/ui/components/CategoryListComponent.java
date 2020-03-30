package ui.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import model.Category;
import model.CategoryList;
import ui.UserInterface;

// Represents a component to render a ListView of Strings representing the user's categories and durations
public class CategoryListComponent {
    private ObservableList<Category> categoryObservableList;
    private ListView<String> categoryListView;
    private UserInterface userInterface;

    public CategoryListComponent(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // MODIFIES: this;
    // EFFECTS: renders a list view of strings representing user's categories and durations
    public void renderCategoryListView() {
        categoryListView = new ListView<>();
        generateCategoryList();

        for (Category category : categoryObservableList) {
            categoryListView.getItems().add(category.getDurationString());
        }
    }

    // REQUIRES: valid UserSession with instantiated CategoryList
    // MODIFIES: this
    // EFFECTS: populates ObservableList with categories from user CategoryList
    public void generateCategoryList() {
        categoryObservableList = FXCollections.observableArrayList();
        CategoryList categoryList = userInterface.getCurrentSession().getCategoryList();
        for (int i = 0; i < categoryList.getSize(); i++) {
            categoryObservableList.add(categoryList.get(i));
        }
    }

    // EFFECTS: returns observable list of Category
    public ObservableList<Category> getCategoryObservableList() {
        generateCategoryList();
        return categoryObservableList;
    }

    // EFFECTS: returns ListView of string representing categories and durations
    public ListView<String> getCategoryListView() {
        renderCategoryListView();
        return categoryListView;
    }

}



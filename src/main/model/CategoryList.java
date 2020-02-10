package model;

import java.util.ArrayList;
import java.util.List;

public class CategoryList {

    // Key: Category
    // Value: hours spent for each category
    private List<Category> categoryList;
    private Category uncategorized;

    // Constructor
    // Each new category instantiated with "uncategorized" category that is not deletable or modifiable
    public CategoryList() {
        categoryList = new ArrayList<>();
        uncategorized = new Category("Uncategorized");
        categoryList.add(uncategorized);
    }

    // MODIFIES: this
    // EFFECTS: adds a category to this list of categories and sets initial balance to 0
    public void addCategory(Category c) {
        categoryList.add(c);
    }

    // REQUIRES: category that exists in the list
    // EFFECTS: returns total time spent in a category
    public Integer getCategoryTimeSpent(Category c) {
        for (Category category : categoryList) {
            if (category == c) {
                return category.getDuration();
            }
        }
        return 0;
    }

    // MODIFIES: this
    // EFFECTS: - deletes category from list
    //          - adds duration total to uncategorized
    public void deleteCategory(Category c) {
        for (Category category : categoryList) {
            if (category == uncategorized) {
                category.setDuration(category.getDuration() + c.getDuration());
            }
        }
        categoryList.remove(c);
    }

    // EFFECTS: returns size of category list
    public int getSize() {
        return categoryList.size();
    }

    // EFFECTS: prints list of categories to screen
    public String printList() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < categoryList.size(); i++) {
            builder.append(i + 1);
            builder.append(". ");
            builder.append(categoryList.get(i).getName());
            builder.append(". You spent ");
            builder.append(categoryList.get(i).getDuration());
            builder.append(" minutes on this category. ");
            builder.append("\n");
        }
        return builder.toString();
    }

    // EFFECTS: prints list of categories to screen except for 'Uncategorized'
    public String printListExceptUncategorized() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < categoryList.size(); i++) {
            builder.append(i);
            builder.append(". ");
            builder.append(categoryList.get(i).getName());
            builder.append(". You spent ");
            builder.append(categoryList.get(i).getDuration());
            builder.append(" minutes on this category. ");
            builder.append("\n");
        }
        return builder.toString();
    }

    // EFFECTS: returns category of index in list
    public Category getCategory(int i) {
        return categoryList.get(i);
    }

    // EFFECTS: returns true if category already exists in the list, false otherwise
    public boolean doesCategoryAlreadyExist(String name) {
        for (Category c : categoryList) {
            if (name.equals(c.getName())) {
                return true;
            }
        }
        return false;
    }

}

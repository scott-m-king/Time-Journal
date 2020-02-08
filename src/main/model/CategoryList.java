package model;

import java.util.ArrayList;
import java.util.List;

public class CategoryList {

    // Key: Category
    // Value: hours spent for each category
    private List<Category> categoryList;
    private Category uncategorized;

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

    // TODO: figure out wtf this was for... maybe for recategorizing?
    // MODIFIES: this
    // EFFECTS: increases category time spent by duration amount
    public void setDuration(Category c, int duration) {
        for (Category category : categoryList) {
            if (category == c) {
                category.setDuration(c.getDuration() + duration);
            }
        }
    }

    // EFFECTS: prints ist of categories to screen
    public void printList() {
        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println((i + 1) + ". " + categoryList.get(i).getName());
        }
    }

    // EFFECTS: returns category of index in list
    public Category getCategory(int i) {
        return categoryList.get(i);
    }

}

package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents an ArrayList of categories
public class CategoryList {
    private List<Category> categoryList;

    // Constructor
    public CategoryList() {
        categoryList = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a category to this list of categories and sets initial balance to 0
    public void add(Category c) {
        categoryList.add(c);
    }

    // REQUIRES: category that exists in the list
    // EFFECTS: returns total time spent in a category
    public Integer getDuration(Category c) {
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
    public void delete(Category c) {
        categoryList.get(0).setDuration(categoryList.get(0).getDuration() + c.getDuration());
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
    public Category get(int i) {
        return categoryList.get(i);
    }

    // EFFECTS: returns category in list with matching name
    public Category get(String name) {
        for (Category category : categoryList) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    // EFFECTS: returns true if category already exists in the list, false otherwise
    public boolean isDuplicateName(String name) {
        for (Category c : categoryList) {
            if (name.equals(c.getName())) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns the CategoryList object as is. Used in updateWithLoadedCategories only
    protected List<Category> getCategoryList() {
        return categoryList;
    }

    // EFFECTS: returns the ID of the next category to be made
    public int getNextCategoryID() {
        ArrayList<Integer> arr = new ArrayList<>();
        for (Category c : categoryList) {
            arr.add(c.getCategoryID());
        }
        return Collections.max(arr) + 1;
    }

}

package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents an ArrayList of categories
public class CategoryList {
    private final List<Category> categoryList;

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

    // EFFECTS: returns true if category already exists in the list, false otherwise (case insensitive)
    public boolean isDuplicateName(String name) {
        char[] originalString = name.toCharArray();

        for (int i = 0; i < originalString.length; i++) {
            originalString[i] = Character.toLowerCase(originalString[i]);
        }

        String updatedString = new String(originalString);

        for (Category c : categoryList) {
            if (updatedString.equals(c.getName().toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    // EFFECTS: returns the CategoryList object as is. Used in updateWithLoadedCategories only
    public List<Category> getCategoryList() {
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

    public void insert(Category category) {
        categoryList.add(0, category);
    }

}

package model;

import java.util.ArrayList;
import java.util.List;

// Represents an ArrayList of categories
public class CategoryList {
    private Category uncategorized;
    private List<Category> categoryList;

    /**
     * When CategoryList is instantiated, it comes pre-populated with the 'Uncategorized' Category object that acts as
     * a 'default' category. Uncategorized is just like a normal Category but is non-modifiable and non-deletable.
     * When a Category is deleted from CategoryList, all journal entries tagged with that category will automatically
     * be re-assigned to Uncategorized and the duration will be updated accordingly.
     */

    // Constructor
    public CategoryList() {
        categoryList = new ArrayList<>();
        uncategorized = new Category("Uncategorized");
        categoryList.add(uncategorized);
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
        find(uncategorized).setDuration(find(uncategorized).getDuration() + find(c).getDuration());
        categoryList.remove(find(c));
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

    // EFFECTS: returns true if category already exists in the list, false otherwise
    public boolean isDuplicateName(String name) {
        for (Category c : categoryList) {
            if (name.equals(c.getName())) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: given category, returns the category from this instance of CategoryList (for after loading)
    public Category find(Category category) {
        for (Category c : categoryList) {
            if (c.getName().equals(category.getName())) {
                return c;
            }
        }
        return null;
    }

    // EFFECTS: returns the CategoryList object as is. Used in updateWithLoadedCategories only
    protected List<Category> getCategoryList() {
        return categoryList;
    }

}

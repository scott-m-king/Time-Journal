package model;

// Represents a category with a name and duration (total time spent in this category)
public class Category {
    private final int categoryID;
    private String name;       // User-inputted name of category
    private int duration;      // time spent on category initialized to zero

    // Constructor
    public Category(int categoryID, String name) {
        this.categoryID = categoryID;
        this.name = name;
        this.duration = 0;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int getDuration() {
        return duration;
    }

    // MODIFIES: this
    // EFFECTS: sets duration of category
    public void setDuration(int duration) {
        this.duration = duration;
    }

    // MODIFIES: this
    // EFFECTS: sets name of category
    public void setName(String name) {
        this.name = makeNormalName(name);
    }

    // EFFECTS: sets name of category
    public String makeNormalName(String name) {
        char[] originalName = name.toCharArray();
        originalName[0] = Character.toUpperCase(originalName[0]);
        return new String(originalName);
    }

    // MODIFIES: this
    // EFFECTS: adds d to the current duration of this object
    public void addDuration(int d) {
        duration += d;
    }

    // EFFECTS: returns total time spent in this category in String format
    public String getDurationString() {
        return name + " - " + duration + " minutes spent on this category.";
    }

}

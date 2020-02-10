package model;

public class Category {

    private String name;
    private int duration;

    // Constructor
    public Category(String name) {
        this.name = name;
        this.duration = 0;
    }

    // getters and setters
    public String getName() {
        return name;
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
        this.name = name;
    }

}

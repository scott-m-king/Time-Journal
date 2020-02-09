package model;

public class Category {

    private String name;
    private int duration;

    // Constructor
    public Category(String name) {
        this.name = name;
        this.duration = 0;
    }

    // EFFECTS: returns name of category
    public String getName() {
        return name;
    }

    // EFFECTS: returns duration of category
    public int getDuration() {
        return duration;
    }

    // EFFECTS: sets duration of category
    public void setDuration(int duration) {
        this.duration = duration;
    }

    // EFFECTS: sets name of category
    public void setName(String name) {
        this.name = name;
    }

}

package model;

public class Category {

    private String name;
    private int duration;

    // Constructor
    public Category(String name) {
        this.name = name;
        this.duration = 0;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setName(String name) {
        this.name = name;
    }

}

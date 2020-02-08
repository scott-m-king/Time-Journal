package model;

import java.time.*;

public class JournalEntry {
    private int id;
    private LocalDate date;
    private String description;
    private Category category;
    private Integer duration;

    public JournalEntry(int id, String description, Category category, int duration) {
        this.id = id;
        date = LocalDate.now();
        this.description = description;
        this.category = category;
        this.duration = duration;
    }

    // getters
    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setCategory(Category c) {
        category = c;
    }

}

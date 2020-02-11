package model;

import java.time.LocalDate;

// Represents a Journal Entry with a unique ID, date stamp, description, assigned category, and duration
public class JournalEntry {
    private int id;                // unique ID for each journal entry used to access value in journalLog
    private LocalDate date;        // current system date
    private String description;    // User-inputted description for journal entry
    private Category category;     // Category object user assigned to this journal entry
    private Integer duration;      // Amount of time spent on this entry

    // Constructor
    public JournalEntry(int id, String description, Category category, int duration) {
        this.id = id;
        date = LocalDate.now();
        this.description = description;
        this.category = category;
        this.duration = duration;
    }

    // getters and setters
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

    // MODIFIES: this
    // REQUIRES: category that exists
    // EFFECTS: sets the category of this entry to user specified category
    public void setCategory(Category c) {
        category = c;
    }

    // MODIFIES: this
    // REQUIRES: positive integer
    // EFFECTS: sets the duration of this entry to user specified duration
    public void setDuration(int duration) {
        this.duration = duration;
    }

    // MODIFIES: this
    // EFFECTS: sets the description of this entry to user specified description
    public void setDescription(String newDescription) {
        description = newDescription;
    }

}

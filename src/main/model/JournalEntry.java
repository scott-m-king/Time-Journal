package model;

import com.google.gson.annotations.Expose;

import java.time.LocalDate;

// Represents a Journal Entry with a unique ID, date stamp, description, assigned category, and duration
public class JournalEntry {
    @Expose private int journalID;         // unique ID for each journal entry used to access value in journalLog
    @Expose private String date;           // current system date
    @Expose private String description;    // user-inputted description for journal entry
    @Expose private int categoryID;        // corresponding category ID (for deserialization purposes)
    private Category category;             // reference to Category object user assigned to this journal entry
    @Expose private Integer duration;      // amount of time spent on this entry

    // Constructor
    public JournalEntry(int journalID, String description, int categoryID,  Category category, int duration) {
        this.journalID = journalID;
        date = LocalDate.now().toString();
        this.description = description;
        this.categoryID = categoryID;
        this.category = category;
        this.duration = duration;
    }

    // getters
    public int getJournalID() {
        return journalID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getDate() {
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
        categoryID = c.getCategoryID();
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

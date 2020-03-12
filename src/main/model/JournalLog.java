package model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// Represents a HashMap of journal entries with journalEntry.id as key, and associated journalEntry as value
public class JournalLog {
    @Expose private Map<Integer, JournalEntry> journalLog;

    // Constructor
    public JournalLog() {
        journalLog = new HashMap<>();
    }

    // MODIFIES: this
    // EFFECTS: - adds journal entry to the log
    //          - adds time duration to category total
    public void add(JournalEntry entry) {
        journalLog.put(entry.getJournalID(), entry);
        entry.getCategory().addDuration(entry.getDuration());
    }

    // MODIFIES: this
    // EFFECTS: if category exists, deletes a journal entry from the log and adjusts category totals and returns true
    public boolean delete(int id) {
        if (journalLog.containsKey(id)) {
            Category category = journalLog.get(id).getCategory();
            category.setDuration(category.getDuration() - journalLog.get(id).getDuration());
            journalLog.remove(id);
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: changes all entries categorized with "c" to "uncategorized" (used when deleting a category)
    public void uncategorize(Category c, Category uncategorized) {
        for (Map.Entry<Integer, JournalEntry> entry : journalLog.entrySet()) {
            if (entry.getValue().getCategoryID() == c.getCategoryID()) {
                entry.getValue().setCategory(uncategorized);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: updates journal entries with current instances of categories (for after deserialization)
    public void updateWithLoadedCategories(CategoryList categoryList) {
        for (Map.Entry<Integer, JournalEntry> entry : journalLog.entrySet()) {
            for (Category c : categoryList.getCategoryList()) {
                if (entry.getValue().getCategoryID() == c.getCategoryID()) {
                    entry.getValue().setCategory(c);
                }
            }
        }
    }

    // REQUIRES: at least one journal entry in the log
    // EFFECTS: prints entire log to screen
    public String printLog() {
        StringBuilder builder = new StringBuilder();
        for (JournalEntry entry : journalLog.values()) {
            builder.append("ID: ");
            builder.append(entry.getJournalID());
            builder.append(" | Date: ");
            builder.append(entry.getDate());
            builder.append(" | Category: ");
            builder.append(entry.getCategory().getName());
            builder.append(" | Duration: ");
            builder.append(entry.getDuration());
            builder.append(" mins | Description: ");
            builder.append(entry.getDescription());
            builder.append("\n");
        }
        return builder.toString();
    }

    // EFFECTS: returns number of journal entries
    public int getSize() {
        return journalLog.size();
    }

    // EFFECTS: returns true if journal entry is in the log, false otherwise
    public boolean hasID(int id) {
        return journalLog.containsKey(id);
    }

    // REQUIRES: valid ID of journal entry that exists in the list
    // EFFECTS: returns journal entry in the list given ID
    public JournalEntry getValue(int id) {
        return journalLog.get(id);
    }

    // EFFECTS: returns the next journal ID number to use after loading from save file
    public int getNextJournalID() {
        return Collections.max(journalLog.keySet()) + 1;
    }

    // EFFECTS: returns values of journalLog as ArrayList
    public ArrayList<JournalEntry> getEntriesAsList() {
        ArrayList<JournalEntry> entries = new ArrayList<>();
        for (Map.Entry<Integer, JournalEntry> entry : journalLog.entrySet()) {
            entries.add(entry.getValue());
        }
        return entries;
    }

}

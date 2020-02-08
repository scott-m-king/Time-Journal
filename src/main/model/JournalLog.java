package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JournalLog {
    Map<Integer, JournalEntry> journalLog;

    // Constructor
    public JournalLog() {
        journalLog = new HashMap<>();
    }

    // MODIFIES: this
    // EFFECTS: - adds journal entry to the log
    //          - adds time duration to category total
    public void addJournalEntry(JournalEntry entry) {
        journalLog.put(entry.getId(), entry);

    }

    // MODIFIES: this
    // EFFECTS: if category exists, deletes a journal entry from the log and adjusts category totals and returns true
    //          else, return false
    public boolean deleteJournalEntry(int removeID) {
        if (journalLog.containsKey(removeID)) {
            journalLog.get(removeID).getCategory().setDuration(journalLog.get(removeID).getCategory().getDuration() - journalLog.get(removeID).getDuration());
            journalLog.remove(removeID);
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: changes all entries categorized with "c" to "uncategorized"
    public void uncategorize(Category c) {
        for (Map.Entry<Integer, JournalEntry> entry : journalLog.entrySet()) {
            if (entry.getValue().getCategory() == c) {
                entry.getValue().setCategory(c);
            }
        }
    }

    // EFFECTS: prints entire log to screen
    public void printLog() {
        StringBuilder builder = new StringBuilder();

        for (JournalEntry entry : journalLog.values()) {
            builder.append("ID: ");
            builder.append(entry.getId());
            builder.append(" | Date: ");
            builder.append(entry.getDate());
            builder.append(" | Category: ");
            builder.append(entry.getCategory().getName());
            builder.append(" | Duration: ");
            builder.append(entry.getDuration());
            builder.append("mins | Description: ");
            builder.append(entry.getDescription());
            builder.append("\n");
        }
        System.out.println(builder.toString());
    }

    // EFFECTS: returns number of journal entries
    public int getSize() {
        return journalLog.size();
    }

}

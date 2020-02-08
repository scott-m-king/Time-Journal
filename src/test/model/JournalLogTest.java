package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JournalLogTest {

    JournalLog testJournal;
    JournalEntry testEntry;
    Category testCategory;

    @BeforeEach
    public void runBefore() {
        testJournal = new JournalLog();
        testCategory = new Category("test");
        testEntry = new JournalEntry(1, "test", testCategory, 5);
    }

    @Test
    public void testAddLogEntry() {
        testJournal.addJournalEntry(testEntry);
    }

    @Test
    public void testDeleteJournalEntry() {
        testJournal.addJournalEntry(testEntry);
    }

}

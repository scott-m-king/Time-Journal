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
        testJournal.addJournalEntry(testEntry);
    }

    @Test
    public void testAddLogEntry() {
        assertEquals(1, testJournal.getValue(1).getId());
    }

    @Test
    public void testDeleteJournalEntry() {
    }

    @Test
    public void testUncategorize() {
    }

    @Test
    public void testPrintLog() {
    }

    @Test
    public void testGetSize() {
    }

    @Test
    public void testHasID() {
    }

    @Test
    public void testGetValue() {
    }

}

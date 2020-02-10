package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class JournalLogTest {

    JournalLog testJournal;
    JournalEntry testEntry;
    Category testCategory;
    Category uncategorized = new Category("Uncategorized");

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
        assertFalse(testJournal.deleteJournalEntry(3));
        assertTrue(testJournal.deleteJournalEntry(1));
        assertEquals(0, testJournal.getSize());
    }

    @Test
    public void testUncategorize() {
        JournalEntry test2 = new JournalEntry(2, "test2", testCategory, 15);
        testJournal.addJournalEntry(test2);
        testJournal.uncategorize(testCategory, uncategorized);
        assertEquals(uncategorized, testJournal.getValue(1).getCategory());
        assertEquals(uncategorized, testJournal.getValue(2).getCategory());
    }

    @Test
    public void testHasID() {
        assertFalse(testJournal.hasID(2));
        assertTrue(testJournal.hasID(1));
        JournalEntry test2 = new JournalEntry(2, "test2", testCategory, 10);
        testJournal.addJournalEntry(test2);
        assertTrue(testJournal.hasID(2));
    }

    @Test
    public void testPrintLog() {
        assertEquals("ID: 1 | Date: "
                        + LocalDate.now() + " | Category: test | Duration: 5 mins | Description: test\n",
                testJournal.printLog());
    }

}

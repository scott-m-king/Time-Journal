package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for JournalEntry class
public class JournalEntryTest {
    private JournalEntry testEntry;
    private Category testCategory;

    @BeforeEach
    public void runBefore() {
        testCategory = new Category(1,"test");
        testEntry = new JournalEntry(1, "test", testCategory.getId(), testCategory, 10);
    }

    @Test
    public void testConstructor() {
        assertEquals(1, testEntry.getJournalID());
        assertEquals(LocalDate.now().toString(), testEntry.getDate());
        assertEquals("test", testEntry.getDescription());
        assertEquals(testCategory, testEntry.getCategory());
        assertEquals(10, testEntry.getDuration());
    }

    @Test
    public void testSetCategory() {
        Category testCategory2 = new Category(2,"test2");
        testEntry.setCategory(testCategory2);
        assertEquals(testCategory2, testEntry.getCategory());
    }

    @Test
    public void testSetDuration() {
        testEntry.setDuration(15);
        assertEquals(15, testEntry.getDuration());
    }

    @Test
    public void testSetDescription() {
        testEntry.setDescription("New description");
        assertEquals("New description", testEntry.getDescription());
        testEntry.setDescription("Changed description");
        assertEquals("Changed description", testEntry.getDescription());
    }

}

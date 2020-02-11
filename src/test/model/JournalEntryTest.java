package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for JournalEntryTest class
public class JournalEntryTest {
    private JournalEntry testEntry;
    private Category testCategory;

    @BeforeEach
    public void runBefore() {
        testCategory = new Category("test");
        testEntry = new JournalEntry(1, "test", testCategory, 10);
    }

    @Test
    public void testConstructor() {
        assertEquals(1, testEntry.getId());
        assertEquals(LocalDate.now(), testEntry.getDate());
        assertEquals("test", testEntry.getDescription());
        assertEquals(testCategory, testEntry.getCategory());
        assertEquals(10, testEntry.getDuration());
    }

    @Test
    public void testSetCategory() {
        Category testCategory2 = new Category("test2");
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
    }

}

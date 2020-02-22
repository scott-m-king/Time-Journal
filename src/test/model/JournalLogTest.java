package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for JournalLog class
public class JournalLogTest {
    private JournalLog testJournal;
    private Category testCategory;
    private Category uncategorized = new Category(0, "Uncategorized");
    private CategoryList testCategoryList;

    @BeforeEach
    public void runBefore() {
        testJournal = new JournalLog();
        testCategory = new Category(1, "test");
        testCategoryList = new CategoryList();
        testCategoryList.add(uncategorized);
        testCategoryList.add(testCategory);
        JournalEntry testEntry = new JournalEntry(
                1, "test", testCategory.getCategoryID(), testCategory, 5);
        testJournal.add(testEntry);
    }

    @Test
    public void testAddLogEntry() {
        assertEquals(1, testJournal.getValue(1).getJournalID());
        assertEquals(1, testJournal.getSize());
        JournalEntry testTwo = new JournalEntry(
                2, "test 2", uncategorized.getCategoryID(), uncategorized, 10);
        testJournal.add(testTwo);
        assertEquals(2, testJournal.getValue(2).getJournalID());
        assertEquals(2, testJournal.getSize());
    }

    @Test
    public void testDeleteJournalEntry() {
        assertFalse(testJournal.delete(3));
        assertTrue(testJournal.delete(1));
        assertEquals(0, testJournal.getSize());
    }

    @Test
    public void testUncategorize() {
        JournalEntry test2 = new JournalEntry(
                2, "test2", testCategory.getCategoryID(), testCategory, 15);
        Category anotherCategory = new Category(2, "test2");
        JournalEntry test3 = new JournalEntry(
                3, "test3", anotherCategory.getCategoryID(), anotherCategory, 30);
        testJournal.add(test2);
        testJournal.add(test3);
        testJournal.uncategorize(testCategory, uncategorized);
        assertEquals(uncategorized, testJournal.getValue(1).getCategory());
        assertEquals(uncategorized, testJournal.getValue(2).getCategory());
        assertEquals(anotherCategory, testJournal.getValue(3).getCategory());
    }

    @Test
    public void testHasID() {
        assertFalse(testJournal.hasID(2));
        assertTrue(testJournal.hasID(1));
        JournalEntry test2 = new JournalEntry(
                2, "test2", testCategory.getCategoryID(), testCategory, 10);
        testJournal.add(test2);
        assertTrue(testJournal.hasID(2));
    }

    @Test
    public void testPrintLog() {
        assertEquals("ID: 1 | Date: "
                        + LocalDate.now() + " | Category: test | Duration: 5 mins | Description: test\n",
                testJournal.printLog());
        JournalEntry test2 = new JournalEntry(
                2, "test2", uncategorized.getCategoryID(), uncategorized, 10);
        JournalEntry test3 = new JournalEntry(
                3, "test3", testCategory.getCategoryID(), testCategory, 15);
        testJournal.add(test2);
        testJournal.add(test3);
        assertEquals("ID: 1 | Date: " + LocalDate.now() +
                        " | Category: test | Duration: 5 mins | Description: test\n" +
                        "ID: 2 | Date: " + LocalDate.now() +
                        " | Category: Uncategorized | Duration: 10 mins | Description: test2\n" +
                        "ID: 3 | Date: " + LocalDate.now() +
                        " | Category: test | Duration: 15 mins | Description: test3\n",
                testJournal.printLog());
    }

    @Test
    public void testUpdateWithLoadedCategories() {
        Category unloadedCateogry = new Category(2, "test2");
        testCategoryList.add(unloadedCateogry);
        JournalEntry testEntry2 = new JournalEntry(
                2, "test2", unloadedCateogry.getCategoryID(), unloadedCateogry, 10);
        testJournal.add(testEntry2);

        assertEquals(testCategory, testJournal.getValue(1).getCategory());
        assertEquals(unloadedCateogry, testJournal.getValue(2).getCategory());
        assertEquals("test", testCategoryList.get(1).getName());
        assertEquals("test2", testCategoryList.get(2).getName());

        CategoryList reloadedCategoryList = new CategoryList();
        Category reloadedCategory1 = new Category(1, "test");
        Category reloadedCategory2 = new Category(2, "test2");
        reloadedCategoryList.add(reloadedCategory1);
        reloadedCategoryList.add(reloadedCategory2);

        testJournal.updateWithLoadedCategories(reloadedCategoryList);

        assertEquals(reloadedCategory1, testJournal.getValue(1).getCategory());
        assertEquals(reloadedCategory2, testJournal.getValue(2).getCategory());
    }

    @Test
    public void testGetNextID() {
        JournalEntry test2 = new JournalEntry(
                2, "test2", uncategorized.getCategoryID(), uncategorized, 10);
        JournalEntry test3 = new JournalEntry(
                3, "test3", testCategory.getCategoryID(), testCategory, 15);
        testJournal.add(test2);
        testJournal.add(test3);

        assertEquals(4, testJournal.getNextJournalID());
        testJournal.delete(2);
        assertEquals(4, testJournal.getNextJournalID());
    }

}

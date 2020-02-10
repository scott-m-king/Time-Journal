package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryListTest {
    CategoryList testList;
    Category testCategory;

    @BeforeEach
    public void runBefore() {
        testList = new CategoryList();
        testCategory = new Category("test");
        testList.addCategory(testCategory);
    }

    @Test
    public void testConstructor() {
        assertEquals(2, testList.getSize());
    }

    @Test
    public void testAddCategory() {
        testList.addCategory(testCategory);
        assertEquals(3, testList.getSize());
    }

    @Test
    public void testGetCategoryTimeSpent() {
        JournalLog testJournal = new JournalLog();
        JournalEntry testEntry = new JournalEntry(1, "test entry", testCategory, 27);
        testJournal.addJournalEntry(testEntry);
        assertEquals(27, testList.getCategoryTimeSpent(testCategory));
        Category fakeCategory = new Category("fake");
        assertEquals(0, testList.getCategoryTimeSpent(fakeCategory));
    }

    @Test
    public void testDeleteCategory() {
        Category test2 = new Category("test2");
        testList.addCategory(testCategory);
        testList.addCategory(test2);
        testList.deleteCategory(testCategory);
        assertEquals(3, testList.getSize());
    }

    @Test
    public void testGetCategory() {
        assertEquals(testCategory, testList.getCategory(1));
        Category test2 = new Category("test2");
        Category test3 = new Category("test3");
        testList.addCategory(test2);
        testList.addCategory(test3);
        assertEquals(test2, testList.getCategory(2));
    }

    @Test
    public void testDoesCategoryAlreadyExist() {
        assertTrue(testList.doesCategoryAlreadyExist("test"));
        assertFalse(testList.doesCategoryAlreadyExist("does not exist"));
        Category testTwo = new Category("another test");
        testList.addCategory(testTwo);
        assertTrue(testList.doesCategoryAlreadyExist("another test"));
    }

    @Test
    public void testPrintList() {
        Category sleep = new Category("Sleep");
        testList.addCategory(sleep);
        assertEquals("1. Uncategorized. You spent 0 minutes on this category. \n" +
                "2. test. You spent 0 minutes on this category. \n" +
                "3. Sleep. You spent 0 minutes on this category. \n", testList.printList());
        JournalEntry testEntry = new JournalEntry(1, "test", sleep, 30);
        JournalLog testLog = new JournalLog();
        testLog.addJournalEntry(testEntry);
        assertEquals("1. Uncategorized. You spent 0 minutes on this category. \n" +
                "2. test. You spent 0 minutes on this category. \n" +
                "3. Sleep. You spent 30 minutes on this category. \n", testList.printList());
    }

    @Test
    public void testPrintListExceptUncategorized() {
        Category sleep = new Category("Sleep");
        testList.addCategory(sleep);
        assertEquals("1. test. You spent 0 minutes on this category. \n" +
                "2. Sleep. You spent 0 minutes on this category. \n", testList.printListExceptUncategorized());
        JournalEntry testEntry = new JournalEntry(1, "test", sleep, 30);
        JournalLog testLog = new JournalLog();
        testLog.addJournalEntry(testEntry);
        assertEquals("1. test. You spent 0 minutes on this category. \n" +
                "2. Sleep. You spent 30 minutes on this category. \n", testList.printListExceptUncategorized());
    }

}

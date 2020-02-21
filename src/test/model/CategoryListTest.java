package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for CategoryList class
public class CategoryListTest {
    private CategoryList testList;
    private Category testCategory;

    @BeforeEach
    public void runBefore() {
        testList = new CategoryList();
        testCategory = new Category("test");
        testList.add(testCategory);
    }

    @Test
    public void testConstructor() {
        assertEquals(2, testList.getSize());
    }

    @Test
    public void testAddCategory() {
        testList.add(testCategory);
        assertEquals(3, testList.getSize());
    }

    @Test
    public void testGetCategoryTimeSpent() {
        JournalLog testJournal = new JournalLog();
        JournalEntry testEntry = new JournalEntry(1, "test entry", testCategory, 27);
        testJournal.add(testEntry);
        assertEquals(27, testList.getDuration(testCategory));
        Category fakeCategory = new Category("fake");
        assertEquals(0, testList.getDuration(fakeCategory));
    }

    @Test
    public void testDeleteCategory() {
        Category test2 = new Category("test2");
        testList.add(testCategory);
        testList.add(test2);
        testList.delete(testCategory);
        assertEquals(3, testList.getSize());
    }

    @Test
    public void testGetCategory() {
        assertEquals(testCategory, testList.get(1));
        Category test2 = new Category("test2");
        Category test3 = new Category("test3");
        testList.add(test2);
        testList.add(test3);
        assertEquals(test2, testList.get(2));
    }

    @Test
    public void testDoesCategoryAlreadyExist() {
        assertTrue(testList.isDuplicateName("test"));
        assertFalse(testList.isDuplicateName("does not exist"));
        Category testTwo = new Category("another test");
        testList.add(testTwo);
        assertTrue(testList.isDuplicateName("another test"));
    }

    @Test
    public void testPrintList() {
        Category sleep = new Category("Sleep");
        testList.add(sleep);
        assertEquals("1. Uncategorized. You spent 0 minutes on this category. \n" +
                "2. test. You spent 0 minutes on this category. \n" +
                "3. Sleep. You spent 0 minutes on this category. \n", testList.printList());
        JournalEntry testEntry = new JournalEntry(1, "test", sleep, 30);
        JournalLog testLog = new JournalLog();
        testLog.add(testEntry);
        assertEquals("1. Uncategorized. You spent 0 minutes on this category. \n" +
                "2. test. You spent 0 minutes on this category. \n" +
                "3. Sleep. You spent 30 minutes on this category. \n", testList.printList());
    }

    @Test
    public void testPrintListExceptUncategorized() {
        Category sleep = new Category("Sleep");
        testList.add(sleep);
        assertEquals("1. test. You spent 0 minutes on this category. \n" +
                "2. Sleep. You spent 0 minutes on this category. \n", testList.printListExceptUncategorized());
        JournalEntry testEntry = new JournalEntry(1, "test", sleep, 30);
        JournalLog testLog = new JournalLog();
        testLog.add(testEntry);
        assertEquals("1. test. You spent 0 minutes on this category. \n" +
                "2. Sleep. You spent 30 minutes on this category. \n", testList.printListExceptUncategorized());
    }

    @Test
    public void testFind() {
        Category test2 = new Category("test2");
        assertNull(testList.find(test2));
        testList.add(test2);
        assertEquals("test2", testList.find(test2).getName());
    }

    @Test
    public void testGetCategoryList() {
        assertEquals(testList.get(0), testList.getCategoryList().get(0));
    }

}

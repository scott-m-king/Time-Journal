package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for CategoryList class
public class CategoryListTest {
    private CategoryList testList;
    private Category testCategory;

    @BeforeEach
    public void runBefore() {
        testList = new CategoryList();
        Category uncategorized = new Category(0, "Uncategorized");
        testCategory = new Category(1, "test");
        testList.add(uncategorized);
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
        JournalEntry testEntry = new JournalEntry(
                1, "test entry", testCategory.getCategoryID(), testCategory, 27);
        testJournal.add(testEntry);
        assertEquals(27, testList.getDuration(testCategory));
        Category fakeCategory = new Category(2, "fake");
        assertEquals(0, testList.getDuration(fakeCategory));
    }

    @Test
    public void testDeleteCategory() {
        Category test2 = new Category(2, "test2");
        testList.add(testCategory);
        testList.add(test2);
        testList.delete(testCategory);
        assertEquals(3, testList.getSize());
    }

    @Test
    public void testGetCategory() {
        assertEquals(testCategory, testList.get(1));
        Category test2 = new Category(2, "test2");
        Category test3 = new Category(3, "test3");
        testList.add(test2);
        testList.add(test3);
        assertEquals(test2, testList.get(2));
    }

    @Test
    public void testDoesCategoryAlreadyExist() {
        assertTrue(testList.isDuplicateName("test"));
        assertFalse(testList.isDuplicateName("does not exist"));
        Category testTwo = new Category(2, "another test");
        testList.add(testTwo);
        assertTrue(testList.isDuplicateName("another test"));
    }

    @Test
    public void testIsDuplicateNameTrue() {
        testList.add(new Category(2, "test2"));
        assertTrue(testList.isDuplicateName("TEST"));
        assertTrue(testList.isDuplicateName("test"));
        assertTrue(testList.isDuplicateName("test2"));
        assertTrue(testList.isDuplicateName("TEst2"));
        assertTrue(testList.isDuplicateName("tEST2"));
    }

    @Test
    public void testIsDuplicateNameFalse() {
        testList.add(new Category(2, "test2"));
        assertFalse(testList.isDuplicateName("test 2"));
        assertFalse(testList.isDuplicateName("TestTwo"));
    }

    @Test
    public void testGetCategoryByName() {
        Category test2 = new Category(2, "test2");
        Category test3 = new Category(3, "test3");
        testList.add(test2);
        testList.add(test3);
        assertEquals(test2, testList.get("test2"));
        assertEquals(test3, testList.get("test3"));
        assertNull(testList.get("test4"));
    }

    @Test
    public void testGetCategoryList() {
        assertEquals(testList.get(0), testList.getCategoryList().get(0));
    }

    @Test
    public void testGetNextCategoryID() {
        assertEquals(2, testList.getNextCategoryID());
        testList.add(new Category(2, "test2"));
        testList.add(new Category(3, "test3"));
        assertEquals(4, testList.getNextCategoryID());
    }

}

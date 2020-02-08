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
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testList.getSize());
    }

    @Test
    public void testAddCategory() {
        testList.addCategory(testCategory);
        assertEquals(1, testList.getSize());
    }

    @Test
    public void testGetCategoryTimeSpent() {
        JournalEntry testEntry = new JournalEntry(1, "test entry", testCategory, 5);
        assertEquals(5, testList.getCategoryTimeSpent(testCategory));
    }

    @Test
    public void testDeleteCategory() {
        Category test2 = new Category("test2");
        testList.addCategory(testCategory);
        testList.addCategory(test2);
        testList.deleteCategory(testCategory);
        assertEquals(0, testList.getSize());
    }

}

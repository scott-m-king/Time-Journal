package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {
    Category testCategory;

    @BeforeEach
    public void runBefore() {
        testCategory = new Category("test");
    }

    @Test
    public void testConstructor() {
        assertEquals("test", testCategory.getName());
        assertEquals(0, testCategory.getDuration());
    }

    @Test
    public void testSetDuration() {
        testCategory.setDuration(15);
        assertEquals(15, testCategory.getDuration());
        testCategory.setDuration(20);
        assertEquals(20, testCategory.getDuration());
    }

    @Test
    public void testSetName() {
        testCategory.setName("newTest");
        assertEquals("newTest", testCategory.getName());
    }

    @Test
    public void addDuration() {
        testCategory.setDuration(10);
        testCategory.addDuration(50);
        assertEquals(60, testCategory.getDuration());
    }

}

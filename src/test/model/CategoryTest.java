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
    }

}

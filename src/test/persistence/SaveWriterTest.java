package persistence;

import model.Category;
import model.CategoryList;
import model.JournalEntry;
import model.JournalLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

public class SaveWriterTest {
    private SaveWriter testSaveWriterCategories;
    private SaveWriter testSaveWriterJournal;
    private JournalLog testJournalLog;
    private CategoryList testCategoryList;
    private Category uncategorized;

    public static final String CATEGORY_SAVE_LOCATION = "./data/test/test_category_save.json";
    public static final String JOURNAL_SAVE_LOCATION = "./data/test/test_journal_save.json";

    @BeforeEach
    public void runBefore() {
        testJournalLog = new JournalLog();
        testCategoryList = new CategoryList();
        uncategorized = new Category (0, "Uncategorized");
        testCategoryList.add(uncategorized);

        try {
            testSaveWriterCategories = new SaveWriter(new File(CATEGORY_SAVE_LOCATION));
            testSaveWriterJournal = new SaveWriter(new File(JOURNAL_SAVE_LOCATION));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveFileJournalLog() {
        populateJournalLog(3);
        try {
            testSaveWriterJournal.saveFile(testJournalLog);
            testSaveWriterJournal.close();
        } catch (IOException e) {
            fail("Exception thrown...");
        }

        try {
            SaveReader testJournalReader = new SaveReader(JOURNAL_SAVE_LOCATION);
            JournalLog testLoadJournalLog = testJournalReader.readJournalEntries();

            assertEquals(3, testLoadJournalLog.getSize());
            assertEquals("test1", testLoadJournalLog.getValue(0).getDescription());
            assertEquals("test2", testLoadJournalLog.getValue(1).getDescription());
            assertEquals("test3", testLoadJournalLog.getValue(2).getDescription());
        } catch (IOException e) {
            fail("Exception thrown...");
        }
    }

    @Test
    public void testSaveFileCategories() {
        populateCategoryList(7);
        try {
            testSaveWriterCategories.saveFile(testCategoryList);
            testSaveWriterCategories.close();
        } catch (IOException e) {
            fail("Exception thrown...");
        }

        try {
            SaveReader testCategoryReader = new SaveReader(CATEGORY_SAVE_LOCATION);
            CategoryList testLoadCategories = testCategoryReader.readCategoryList();

            assertEquals(8, testLoadCategories.getSize());
            assertEquals("Uncategorized", testLoadCategories.get(0).getName());
            assertEquals("test1", testLoadCategories.get(1).getName());
            assertEquals("test3", testLoadCategories.get(3).getName());
            assertEquals("test7", testLoadCategories.get(7).getName());
        } catch (IOException e) {
            fail("Exception thrown...");
        }
    }

    private void populateJournalLog(int quantity) {
        for (int i = 0; i < quantity; i++) {
            JournalEntry test = new JournalEntry(
                    i, "test" + (i + 1), uncategorized.getId(), uncategorized, 10);
            testJournalLog.add(test);
        }
    }

    private void populateCategoryList(int quantity) {
        for (int i = 1; i < quantity + 1; i++) {
            Category test = new Category(i, "test" + i);
            testCategoryList.add(test);
        }
    }
}

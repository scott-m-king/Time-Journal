package persistence;

import model.Category;
import model.CategoryList;
import model.JournalEntry;
import model.JournalLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SaveReaderTest {
    private SaveWriter testSaveWriterCategories;
    private SaveWriter testSaveWriterJournal;
    private JournalLog testJournalLog;
    private CategoryList testCategoryList;
    private Category testCat1;
    private Category testCat2;
    private Category testCat3;
    private Category uncategorized;

    public static final String CATEGORY_SAVE_LOCATION = "./data/test/test_category_save.json";
    public static final String JOURNAL_SAVE_LOCATION = "./data/test/test_journal_save.json";
    public static final String FAKE_PATH = "./wonder/if/the/TA/will/notice/this.json";

    @BeforeEach
    public void runBefore() {
        testJournalLog = new JournalLog();
        testCategoryList = new CategoryList();

        uncategorized = new Category (0, "Uncategorized");
        testCat1 = new Category(1, "test category 1");
        testCat2 = new Category(2, "test category 2");
        testCat3 = new Category(3, "test category 3");

        testCategoryList.add(uncategorized);
        testCategoryList.add(testCat1);
        testCategoryList.add(testCat2);
        testCategoryList.add(testCat3);

        try {
            testSaveWriterCategories = new SaveWriter(new File(CATEGORY_SAVE_LOCATION));
            testSaveWriterJournal = new SaveWriter(new File(JOURNAL_SAVE_LOCATION));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIOExceptionReader() {
        try {
            SaveReader testSaveReaderException = new SaveReader(FAKE_PATH);
            testSaveReaderException.readCategoryList();
        } catch (IOException e) {
            // exception expected
        }
    }

    @Test
    public void testIOExceptionWriter() {
        try {
            SaveWriter testSaveWriterException = new SaveWriter(new File(FAKE_PATH));
            testSaveWriterException.saveFile(testJournalLog);
        } catch (IOException e) {
            // exception expected
        }
    }

    @Test
    public void testGetNextCategoryID() {
        assertEquals(4, testCategoryList.getNextCategoryID());
    }

    @Test
    public void testSaveAndLoadFileJournalLog() {
        populateJournalLog(3);
        testJournalLog.getValue(0).setCategory(testCat1);
        testJournalLog.getValue(1).setCategory(testCat2);
        testJournalLog.getValue(2).setCategory(testCat3);

        try {
            testSaveWriterJournal.saveFile(testJournalLog);
            testSaveWriterJournal.close();
            testSaveWriterCategories.saveFile(testCategoryList);
            testSaveWriterCategories.close();
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
            assertEquals(1, testLoadJournalLog.getValue(0).getCategoryID());
            assertEquals(2, testLoadJournalLog.getValue(1).getCategoryID());
            assertEquals(3, testLoadJournalLog.getValue(2).getCategoryID());
        } catch (IOException e) {
            fail("Exception thrown...");
        }
    }

    @Test
    public void testSaveAndLoadFileCategories() {
        try {
            testSaveWriterJournal.saveFile(testJournalLog);
            testSaveWriterJournal.close();
            testSaveWriterCategories.saveFile(testCategoryList);
            testSaveWriterCategories.close();
        } catch (IOException e) {
            fail("Exception thrown...");
        }

        try {
            SaveReader testCategoryReader = new SaveReader(CATEGORY_SAVE_LOCATION);
            CategoryList testLoadCategories = testCategoryReader.readCategoryList();

            assertEquals(4, testLoadCategories.getSize());
            assertEquals("Uncategorized", testLoadCategories.get(0).getName());
            assertEquals("test category 1", testLoadCategories.get(1).getName());
            assertEquals("test category 2", testLoadCategories.get(2).getName());
            assertEquals("test category 3", testLoadCategories.get(3).getName());
        } catch (IOException e) {
            fail("Exception thrown...");
        }
    }

    private void populateJournalLog(int quantity) {
        for (int i = 0; i < quantity; i++) {
            JournalEntry test = new JournalEntry(
                    i, "test" + (i + 1), uncategorized.getCategoryID(), uncategorized, 10);
            testJournalLog.add(test);
        }
    }
}

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
import java.util.ArrayList;

public class SaveWriterTest {
    private SaveWriter testSaveWriterCategories;
    private SaveWriter testSaveWriterJournal;
    private SaveWriter testSaveWriterUsers;
    private ArrayList<String> testUserList;
    private JournalLog testJournalLog;
    private CategoryList testCategoryList;
    private Category uncategorized;

    public static final String CATEGORY_SAVE_LOCATION = "./data/test/test_category_save.json";
    public static final String JOURNAL_SAVE_LOCATION = "./data/test/test_journal_save.json";
    public static final String USERS_SAVE_LOCATION = "./data/test/users_save.json";

    @BeforeEach
    public void runBefore() {
        testJournalLog = new JournalLog();
        testCategoryList = new CategoryList();
        testUserList = new ArrayList<String>();
        uncategorized = new Category (0, "Uncategorized");
        testCategoryList.add(uncategorized);

        try {
            testSaveWriterCategories = new SaveWriter(new File(CATEGORY_SAVE_LOCATION));
            testSaveWriterJournal = new SaveWriter(new File(JOURNAL_SAVE_LOCATION));
            testSaveWriterUsers = new SaveWriter(new File(USERS_SAVE_LOCATION));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveFileJournalLog() {
        populateJournalLog(3);
        JournalLog testLoadJournalLog = new JournalLog();

        try {
            testSaveWriterJournal.save(testJournalLog);
            testSaveWriterJournal.close();
        } catch (IOException e) {
            fail("Exception thrown...");
        }

        try {
            SaveReader testJournalReader = new SaveReader(JOURNAL_SAVE_LOCATION);
            testLoadJournalLog = testJournalReader.readJournalEntries();
        } catch (IOException e) {
            fail("Exception thrown...");
        }

        assertEquals(3, testLoadJournalLog.getSize());
        assertEquals("test1", testLoadJournalLog.getValue(0).getDescription());
        assertEquals("test2", testLoadJournalLog.getValue(1).getDescription());
        assertEquals("test3", testLoadJournalLog.getValue(2).getDescription());
    }

    @Test
    public void testSaveFileCategories() {
        populateCategoryList(7);
        CategoryList testLoadCategories = new CategoryList();

        try {
            testSaveWriterCategories.save(testCategoryList);
            testSaveWriterCategories.close();
        } catch (IOException e) {
            fail("Exception thrown...");
        }

        try {
            SaveReader testCategoryReader = new SaveReader(CATEGORY_SAVE_LOCATION);
            testLoadCategories = testCategoryReader.readCategoryList();
        } catch (IOException e) {
            fail("Exception thrown...");
        }

        assertEquals(8, testLoadCategories.getSize());
        assertEquals("Uncategorized", testLoadCategories.get(0).getName());
        assertEquals("test1", testLoadCategories.get(1).getName());
        assertEquals("test3", testLoadCategories.get(3).getName());
        assertEquals("test7", testLoadCategories.get(7).getName());
    }

    @Test
    public void testSaveFileUsers() {
        ArrayList<String> testLoadUsers = new ArrayList<>();

        testUserList.add("User0");
        testUserList.add("User1");
        testUserList.add("User2");
        try {
            testSaveWriterUsers.save(testUserList);
            testSaveWriterUsers.close();
        } catch (IOException e) {
            fail("Exception thrown...");
        }

        try {
            SaveReader testUsersReader = new SaveReader(USERS_SAVE_LOCATION);
            testLoadUsers = testUsersReader.readUserList();
        } catch (IndexOutOfBoundsException | IOException e) {
            fail("Exception thrown...");
        }

        assertEquals("User0", testLoadUsers.get(0));
        assertEquals("User1", testLoadUsers.get(1));
        assertEquals("User2", testLoadUsers.get(2));
    }

    private void populateJournalLog(int quantity) {
        for (int i = 0; i < quantity; i++) {
            JournalEntry test = new JournalEntry(
                    i, "test" + (i + 1), uncategorized.getCategoryID(), uncategorized, 10);
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
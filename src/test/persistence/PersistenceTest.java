package persistence;

import model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

public class PersistenceTest {
    protected SaveWriter testSaveWriterSession;
    protected SaveWriter testSaveWriterUsers;
    protected ArrayList<String> testUserList;
    protected UserSession testUserSession;
    protected JournalLog testJournalLog;
    protected CategoryList testCategoryList;
    protected Category uncategorized;

    public static final String SESSION_SAVE_LOCATION = "./data/test/test_session_save.json";
    public static final String USERS_SAVE_LOCATION = "./data/test/users_save.json";
    public static final String FAKE_PATH = "./wonder/if/the/TA/will/notice/this.json";

    public void setupPersistenceTest() {
        testJournalLog = new JournalLog();
        testCategoryList = new CategoryList();
        testUserList = new ArrayList<>();
        uncategorized = new Category (0, "Uncategorized");
        testCategoryList.add(uncategorized);
        testUserSession = new UserSession();

        try {
            testUserSession.setCategoryList(testCategoryList);
            testUserSession.setJournalLog(testJournalLog);
            testUserSession.setCurrentUser("test");
            testUserSession.setUserAvatar("File:src/main/ui/resources/pigeon.png");
            testUserSession.setUserList(testUserList);
        } catch (Exception e) {
            fail("Shouldn't have arrived here");
        }

        try {
            testSaveWriterSession = new SaveWriter(new File(SESSION_SAVE_LOCATION));
            testSaveWriterUsers = new SaveWriter(new File(USERS_SAVE_LOCATION));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void populateJournalLog(int quantity) {
        for (int i = 0; i < quantity; i++) {
            JournalEntry test = new JournalEntry(
                    i, "test" + (i + 1), uncategorized.getCategoryID(), uncategorized, 10);
            testJournalLog.add(test);
        }
    }

    protected void populateCategoryList(int quantity) {
        for (int i = 1; i < quantity; i++) {
            Category test = new Category(i, "test" + i);
            testCategoryList.add(test);
        }
    }

    protected void populateUserList(int quantity) {
        for (int i = 0; i < quantity + 1; i++) {
            testUserList.add("test" + i);
        }
    }
}

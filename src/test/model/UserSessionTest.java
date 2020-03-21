package model;

import exceptions.NullEntryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserSessionTest {
    private UserSession testUserSession;
    private ArrayList<String> userList;

    public static final String USER_AVATAR = "File:src/main/ui/resources/dog.png";
    public static final String SESSION_SAVE_LOCATION = "./data/test/test_session_save.json";
    public static final String USERS_SAVE_LOCATION = "./data/test/users_save.json";
    public static final String FAKE_PATH = "./wonder/if/the/TA/will/notice/this.json";

    @BeforeEach
    public void runBefore() {
        testUserSession = new UserSession();
        userList = new ArrayList<>();

        CategoryList testCategoryList = new CategoryList();
        JournalLog testJournalLog = new JournalLog();

        Category testCat1 = new Category(1, "test1");
        Category testCat2 = new Category(2, "test2");

        JournalEntry entry1 = new JournalEntry(1, "test1", 1, testCat1, 10);
        JournalEntry entry2 = new JournalEntry(2, "test2", 2, testCat2, 10);

        testCategoryList.add(testCat1);
        testCategoryList.add(testCat2);

        testJournalLog.add(entry1);
        testJournalLog.add(entry2);

        testUserSession.setCategoryList(testCategoryList);
        testUserSession.setJournalLog(testJournalLog);
        try {
            testUserSession.setCurrentUser("test user");
            testUserSession.setUserAvatar(USER_AVATAR);
        } catch (NullEntryException e) {
            fail("definitely shouldn't have arrived here.");
        }
        testUserSession.setUserList(userList);
    }

    @Test
    public void testIsFirstTimeTrue() {
        assertFalse(testUserSession.isFirstTime(UserSession.USER_SAVE_FILE));
    }

    @Test
    public void testIsFirstTimeFalse() {
        assertTrue(testUserSession.isFirstTime(FAKE_PATH));
    }

    @Test
    public void testSetCurrentUserSuccess() {
        try {
            testUserSession.setCurrentUser("Hello");
        } catch (NullEntryException e) {
            fail("shouldn't have arrived here");
        }
    }

    @Test
    public void testSetCurrentUserFail() {
        try {
            testUserSession.setCurrentUser("");
            fail("Exception expected");
        } catch (NullEntryException e) {
            // exception expected
        }
    }

    @Test
    public void testSetUserAvatarSuccess() {
        try {
            testUserSession.setUserAvatar(USER_AVATAR);
        } catch (NullEntryException e) {
            fail("shouldn't have arrived here");
        }
    }

    @Test
    public void testSetUserAvatarFail() {
        try {
            testUserSession.setUserAvatar(null);
            fail("Exception expected");
        } catch (NullEntryException e) {
            // exception expected
        }
    }

    @Test
    public void testNewSession() {
        testUserSession.newSession();
        assertEquals("test user", testUserSession.getUserList().get(testUserSession.getUserList().size() - 1));
        assertEquals("Uncategorized", testUserSession.getCategoryList().get(0).getName());
        assertEquals("test1", testUserSession.getCategoryList().get(1).getName());
    }

    @Test
    public void testSaveEntriesNoExistingUser() {

    }
}

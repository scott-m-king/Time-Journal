package model;

import exceptions.CategoryExistsException;
import exceptions.NegativeNumberException;
import exceptions.NullEntryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class UserSessionTest {
    private UserSession testUserSession;

    public static final String USER_AVATAR = "File:src/main/ui/resources/dog.png";
    public static final String USERS_SAVE_LOCATION = "./data/test/users_save.json";
    public static final String FAKE_PATH = "./wonder/if/the/TA/will/notice/this.json";
    public static final String TEST_SAVE_LOCATION = "./data/test/users/";

    @BeforeEach
    public void runBefore() {
        testUserSession = new UserSession();
        ArrayList<String> userList = new ArrayList<>();

        CategoryList testCategoryList = new CategoryList();
        JournalLog testJournalLog = new JournalLog();

        Category testCat1 = new Category(1, "test1");
        Category testCat2 = new Category(2, "test2");
        testUserSession.setCategoryID(3);

        JournalEntry entry1 = new JournalEntry(1, "test1", 1, testCat1, 10);
        JournalEntry entry2 = new JournalEntry(2, "test2", 2, testCat2, 10);
        testUserSession.setJournalID(3);

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
        int userNum = new Random().nextInt();
        try {
            testUserSession.setCurrentUser("test user " + userNum);
            int numBefore = Objects.requireNonNull(new File(TEST_SAVE_LOCATION).list()).length;
            testUserSession.saveEntries(TEST_SAVE_LOCATION, USERS_SAVE_LOCATION);
            assertEquals(++numBefore, Objects.requireNonNull(new File(TEST_SAVE_LOCATION).list()).length);
        } catch (Exception e) {
            fail("shouldn't have got here");
        }
    }

    @Test
    public void testSaveEntriesExistingUser() {
        int numBefore = Objects.requireNonNull(new File(TEST_SAVE_LOCATION).list()).length;
        testUserSession.saveEntries(TEST_SAVE_LOCATION, USERS_SAVE_LOCATION);
        assertEquals(numBefore, Objects.requireNonNull(new File(TEST_SAVE_LOCATION).list()).length);
    }

    @Test
    public void testSelectUserSuccess() {
        try {
            testUserSession.selectUser("test user", TEST_SAVE_LOCATION);
            assertEquals("test user", testUserSession.getCurrentUser());
        } catch (NullEntryException | IOException e) {
            fail("shouldn't have got here");
        }
    }

    @Test
    public void testSelectUserNull() {
        try {
            testUserSession.selectUser(null, TEST_SAVE_LOCATION);
            fail("shouldn't have got here");
        } catch (NullEntryException | IOException e) {
            // shouldn't have arrived here
        }
    }

    @Test
    public void testLoadEntriesSuccess() {
        int initialJournalID = testUserSession.getJournalID();
        int initialCategoryID = testUserSession.getCategoryID();
        testUserSession.newSession();
        try {
            testUserSession.saveEntries(TEST_SAVE_LOCATION, USERS_SAVE_LOCATION);
            testUserSession.loadEntries(TEST_SAVE_LOCATION);
        } catch (IOException e) {
            fail("shouldn't have got here");
        }
        assertEquals(initialJournalID, testUserSession.getJournalID());
        assertEquals(initialCategoryID, testUserSession.getCategoryID());
    }

    @Test
    public void testLoadEntriesFreshLists() {
        testUserSession.setJournalLog(new JournalLog());
        testUserSession.setCategoryList(new CategoryList());
        testUserSession.newSession();
        testUserSession.setJournalID(1);
        testUserSession.setCategoryID(1);
        try {
            testUserSession.saveEntries(TEST_SAVE_LOCATION, USERS_SAVE_LOCATION);
            testUserSession.loadEntries(TEST_SAVE_LOCATION);
        } catch (IOException e) {
            fail("shouldn't have got here");
        }
        assertEquals(1, testUserSession.getJournalID());
        assertEquals(1, testUserSession.getCategoryID());
    }

    @Test
    public void testLoadEntriesFileNotFound() {
        try {
            testUserSession.loadEntries(FAKE_PATH);
        } catch (IOException e) {
            fail("shouldn't have got here");
        }
        assertEquals("Uncategorized", testUserSession.getCategoryList().get(0).getName());
    }

    @Test
    public void testCreateNewCategorySuccess() {
        try {
            testUserSession.createNewCategory("Misc");;
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        assertEquals("Misc",
                testUserSession.getCategoryList().get(testUserSession.getCategoryList().getSize() - 1).getName());

        try {
            testUserSession.createNewCategory("lame");;
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        assertEquals("Lame",
                testUserSession.getCategoryList().get(testUserSession.getCategoryList().getSize() - 1).getName());
    }

    @Test
    public void testCreateNewCategoryNullEntry() {
        try {
            testUserSession.createNewCategory("");;
            fail("shouldn't have got here");
        } catch (NullEntryException e) {
            // exception expected
        } catch (Exception e) {
            fail("shouldn't have got here");
        }
    }

    @Test
    public void testCreateNewCategoryDuplicate() {
        try {
            testUserSession.createNewCategory("Misc");;
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        assertEquals("Misc",
                testUserSession.getCategoryList().get(testUserSession.getCategoryList().getSize() - 1).getName());

        try {
            testUserSession.createNewCategory("misc");
            fail("shouldn't have got here");
        } catch (Exception e) {
            // exception expected
        }
    }

    @Test
    public void testCreateNewJournalEntry() {
        testUserSession.createNewJournalEntry("misc", "15", "test1");
        System.out.println(testUserSession.getJournalID());
        assertEquals("misc", testUserSession.getJournalLog().getValue(3).getDescription());
        assertEquals(15, testUserSession.getJournalLog().getValue(3).getDuration());
        assertEquals("test1", testUserSession.getJournalLog().getValue(3).getCategory().getName());
    }

    @Test
    public void testDeleteJournalEntry() {
        try {
            testUserSession.deleteJournalEntry(1);
            System.out.println(testUserSession.getJournalLog().getValue(1).getDescription());
            fail("shouldn't have got here");
        } catch (NullPointerException e) {
            //exception expected
        }
    }

    @Test
    public void testDeleteCategory() {
        testUserSession.newSession();
        assertEquals(0, testUserSession.getCategoryList().get("Uncategorized").getDuration());
        testUserSession.deleteCategory("test1");
        assertEquals(10, testUserSession.getCategoryList().get("Uncategorized").getDuration());
        try {
            int duration = testUserSession.getCategoryList().get("test1").getDuration();
            fail("shouldn't have got here, duration should be null. instead is " + duration);
        } catch (NullPointerException e) {
            // exception expected
        }
    }

    @Test
    public void testEditCategorySuccess() {
        try {
            testUserSession.editCategory("test1", "TestYes");
            assertEquals(10, testUserSession.getCategoryList().get("TestYes").getDuration());
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        try {
            int duration = testUserSession.getCategoryList().get("test1").getDuration();
            fail("shouldn't have got here, duration should be null. instead is " + duration);
        } catch (NullPointerException e) {
            // Exception expected
        }
    }

    @Test
    public void testEditCategoryAlreadyExists() {
        try {
            testUserSession.editCategory("test1", "Test1");
            fail("shouldn't have got here");
        } catch (CategoryExistsException e) {
            // Exception expected
        } catch (Exception e) {
            fail("shouldn't have got here");
        }
    }

    @Test
    public void testEditCategoryEntryNull() {
        try {
            testUserSession.editCategory("test1", "");
            fail("shouldn't have got here");
        } catch (NullEntryException e) {
            // Exception expected
        } catch (Exception e) {
            fail("shouldn't have got here");
        }
    }

    @Test
    public void testCheckValidFormFail() {
        try {
            testUserSession.checkValidForm("Hello", "15");
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        try {
            testUserSession.checkValidForm("", "15");
        } catch (NullEntryException e) {
            // exception expected
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        try {
            testUserSession.checkValidForm(null, "15");
        } catch (NullEntryException e) {
            // exception expected
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        try {
            testUserSession.checkValidForm("hello", "");
        } catch (NullEntryException e) {
            // exception expected
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        try {
            testUserSession.checkValidForm("hello", null);
        } catch (NullEntryException e) {
            // exception expected
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        try {
            testUserSession.checkValidForm(null, null);
        } catch (NullEntryException e) {
            // exception expected
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        try {
            testUserSession.checkValidForm("hello", "-15");
        } catch (NegativeNumberException e) {
            // exception expected
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        try {
            testUserSession.checkValidForm("hello", "0");
        } catch (NegativeNumberException e) {
            // exception expected
        } catch (Exception e) {
            fail("shouldn't have got here");
        }

        try {
            testUserSession.checkValidForm("hello", "abc");
        } catch (NumberFormatException e) {
            // exception expected
        } catch (Exception e) {
            fail("shouldn't have got here");
        }
    }

    @Test
    public void testEditJournalDescription() {
        assertEquals("test1", testUserSession.getJournalLog().getValue(1).getDescription());
        testUserSession.editJournalEntryDescription(1, "hello");
        assertNotEquals("test1", testUserSession.getJournalLog().getValue(1).getDescription());
        assertEquals("hello", testUserSession.getJournalLog().getValue(1).getDescription());
    }

    @Test
    public void testEditJournalEntryDuration() {
        assertEquals(10, (int) testUserSession.getJournalLog().getValue(1).getDuration());
        testUserSession.editJournalEntryDuration(1, "5");
        assertEquals(5, testUserSession.getJournalLog().getValue(1).getDuration());
        assertEquals(5, testUserSession.getJournalLog().getValue(1).getCategory().getDuration());
    }

    @Test
    public void testEditJournalEntryCategory() {
        testUserSession.editJournalEntryCategory(1, "test2");
        assertEquals("test2", testUserSession.getJournalLog().getValue(1).getCategory().getName());;
        assertEquals(0, testUserSession.getCategoryList().get("test1").getDuration());
        assertEquals(20, testUserSession.getCategoryList().get("test2").getDuration());
    }

    @Test
    public void testGetTotalCategoryDuration() {
        assertEquals(20, testUserSession.getTotalCategoryDuration());
        testUserSession.createNewJournalEntry("hello", "15", "test1");
        assertEquals(35, testUserSession.getTotalCategoryDuration());
    }


}

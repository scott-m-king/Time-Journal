package persistence;

import model.Category;
import model.JournalEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.UserSession;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

public class SaveWriterTest extends PersistenceTest {

    @BeforeEach
    public void runBefore() {
        setupPersistenceTest();
    }

    @Test
    public void testSaveUserList() {
        populateUserList(5);
        ArrayList<String> testLoadedUserList;

        try {
            testSaveWriterUsers.save(testUserList);
            testSaveWriterUsers.close();
        } catch (IOException e) {
            fail("Exception thrown...");
        }

        try {
            SaveReader testSessionReader = new SaveReader(USERS_SAVE_LOCATION);
            testLoadedUserList = testSessionReader.readUserList();
            assertEquals(6, testLoadedUserList.size());
            assertEquals("test0", testLoadedUserList.get(0));
            assertEquals("test1", testLoadedUserList.get(1));
            assertEquals("test3", testLoadedUserList.get(3));
        } catch (IOException e) {
            fail("Exception thrown...");
        }
    }

    @Test
    public void testSaveUserSession() {
        populateUserList(10);
        populateJournalLog(10);
        populateCategoryList(5);

        try {
            testSaveWriterSession.save(testUserSession);
            testSaveWriterSession.close();
        } catch (IOException e) {
            fail("Exception thrown...");
        }

        try {
            SaveReader testSaveReaderSession = new SaveReader(SESSION_SAVE_LOCATION);
            UserSession loadedSession = testSaveReaderSession.readUserSession();
            assertEquals(5, loadedSession.getCategoryList().getSize());
            assertEquals(10, loadedSession.getJournalLog().getSize());
            assertEquals("test", loadedSession.getUserName());
            assertEquals("File:src/main/ui/resources/pigeon.png", loadedSession.getUserAvatar());
        } catch (IOException e) {
            fail("Exception thrown...");
        }
    }
}

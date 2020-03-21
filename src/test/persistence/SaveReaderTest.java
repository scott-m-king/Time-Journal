package persistence;

import model.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SaveReaderTest extends PersistenceTest {

    @BeforeEach
    public void runBefore() {
        setupPersistenceTest();
    }

    @Test
    public void testIOExceptionReader() {
        try {
            SaveReader testSaveReaderException = new SaveReader(FAKE_PATH);
            testSaveReaderException.readUserSession();
        } catch (IOException e) {
            // exception expected
        }
    }

    @Test
    public void testIOExceptionWriter() {
        try {
            SaveWriter testSaveWriterException = new SaveWriter(new File(FAKE_PATH));
            testSaveWriterException.save(testUserSession);
        } catch (IOException e) {
            // exception expected
        }
    }

    @Test
    public void testSaveAndLoadFileUserSession() {
        populateJournalLog(3);

        try {
            testSaveWriterSession.save(testUserSession);
            testSaveWriterSession.close();
        } catch (IOException e) {
            fail("Exception thrown...");
        }

        try {
            SaveReader testSessionReader = new SaveReader(SESSION_SAVE_LOCATION);
            UserSession loadedSession = testSessionReader.readUserSession();

            assertEquals(3, loadedSession.getJournalLog().getSize());
            assertEquals("test1", loadedSession.getJournalLog().getValue(0).getDescription());
            assertEquals("test2", loadedSession.getJournalLog().getValue(1).getDescription());
            assertEquals("test3", loadedSession.getJournalLog().getValue(2).getDescription());
            assertEquals("test", loadedSession.getUserName());
            assertEquals("Uncategorized", loadedSession.getCategoryList().get(0).getName());
        } catch (IOException e) {
            fail("Exception thrown...");
        }
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
}

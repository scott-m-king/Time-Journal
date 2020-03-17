package ui;

import exceptions.CategoryExistsException;
import exceptions.NegativeNumberException;
import exceptions.NullEntryException;
import model.Category;
import model.CategoryList;
import model.JournalEntry;
import model.JournalLog;
import persistence.SaveReader;
import persistence.SaveWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Design inspired by Teller App: https://github.students.cs.ubc.ca/CPSC210/TellerApp
 */

// Represents a user session
public class UserSession {
    private CategoryList categoryList;     // declaration of new CategoryList
    private JournalLog journalLog;         // declaration of new JournalLog
    private int journalID = 1;             // starting ID of first journal entry, incremented by 1 for each new entry
    private int categoryID = 1;            // starting ID of first category (after Uncategorized)
    private String currentUser;            // current session user
    private String userAvatar;
    private ArrayList<String> userList;    // list of all users

    public static final String USER_SAVE_FILE = "./data/users_save.json";
    public static final String SESSION_SAVE_FILE = "session_save.json";

    // EFFECTS: runs time journal application and instantiates new category list, journal log, loads users
    public UserSession() {
        categoryList = new CategoryList();
        journalLog = new JournalLog();
        userList = new ArrayList<>();
    }

    // Getters and setters
    public CategoryList getCategoryList() {
        return categoryList;
    }

    public JournalLog getJournalLog() {
        return journalLog;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String fileName) {
        this.userAvatar = fileName;
    }

    public boolean isFirstTime() {
        boolean isFirstTime = false;
        try {
            SaveReader userReader = new SaveReader(USER_SAVE_FILE);
            userList = userReader.readUserList();
        } catch (IOException e) {
            isFirstTime = true;
        }
        return isFirstTime;
    }

    public void setCurrentUser(String user) throws NullEntryException {
        if (user.equals("")) {
            throw new NullEntryException();
        } else {
            this.currentUser = user;
        }
    }

    /**
     * When CategoryList is instantiated in a new session, it comes pre-populated with the 'Uncategorized'
     * Category object that acts as a 'default' category. Uncategorized is just like a normal Category but is
     * non-modifiable and non-deletable. When a Category is deleted from CategoryList, all journal entries tagged
     * with that category will automatically be re-assigned to Uncategorized and the duration will be updated
     * accordingly.
     */

    // EFFECTS: creates a new session by prompting use for first category
    public void newSession() {
        userList.add(currentUser);
        Category uncategorized = new Category(0, "Uncategorized");
        categoryList.add(uncategorized);
    }

    // MODIFIES: save files
    // EFFECTS: saves CategoryList and JournalLog to JSON files
    public void saveEntries() {
        try {
            saveUserSession();
            saveUserList();
        } catch (FileNotFoundException e) {
            new File("./data/users/" + currentUser + "/").mkdir();
            saveEntries();
        } catch (IOException e) {
            // programming error
            System.out.println("We shouldn't have arrived here... Something's probably wrong with save file.");
        }
    }

    private void saveUserSession() throws IOException {
        SaveWriter sessionSave = new SaveWriter(new File("./data/users/"
                + currentUser
                + "/"
                + SESSION_SAVE_FILE));
        sessionSave.save(this);
        sessionSave.close();
    }

    private void saveUserList() throws IOException {
        SaveWriter userListSave = new SaveWriter(new File(USER_SAVE_FILE));
        userListSave.save(userList);
        userListSave.close();
    }

    // MODIFIES: this
    // EFFECTS: sets current session user
    public void selectUser(String choice) throws NullEntryException {
        if (choice == null) {
            throw new NullEntryException();
        }
        currentUser = choice;
        loadEntries();
    }

    public String getUserName() {
        return currentUser;
    }

    // MODIFIES: this
    // EFFECTS: Updates current object instances with deserialized JSON files
    private void loadEntries() {
        try {
            SaveReader journalReader = new SaveReader("./data/users/"
                    + currentUser + "/" + SESSION_SAVE_FILE);
            UserSession loadedSession = journalReader.readUserSession();
            loadSessionFields(loadedSession);
            System.out.println("Save file successfully loaded. \n");
        } catch (FileNotFoundException e) {
            System.out.println("Save file does not exist. New session will be started.\n");
            newSession();
        } catch (IOException e) {
            System.out.println("Invalid save file. New session will be started. \n");
            newSession();
        }
    }

    private void loadSessionFields(UserSession loadedSession) {
        this.journalLog = loadedSession.getJournalLog();
        this.categoryList = loadedSession.getCategoryList();
        this.userAvatar = loadedSession.getUserAvatar();
        this.journalLog.updateWithLoadedCategories(categoryList);
        if (!(journalLog.getSize() == 0)) {
            this.journalID = journalLog.getNextJournalID();
        }
        this.categoryID = categoryList.getNextCategoryID();
    }

    // MODIFIES: this
    // EFFECTS: creates a new category and adds it to categoryList
    public void createNewCategory(String name) throws CategoryExistsException, NullEntryException {
        if (name.equals("")) {
            throw new NullEntryException();
        }
        if (!categoryList.isDuplicateName(name)) {
            Category newCategory = new Category(categoryID++, name);
            categoryList.add(newCategory);
        } else {
            throw new CategoryExistsException();
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new journal entry and adds it to journalLog
    public void createNewJournalEntry(String description, String durationEntry, String categoryEntry) {
        int durationFinal = Integer.parseInt(durationEntry);
        Category category = categoryList.get(categoryEntry);
        JournalEntry newEntry = new JournalEntry(
                journalID++, description, category.getCategoryID(), category, durationFinal);
        journalLog.add(newEntry);
    }

    // MODIFIES: this
    // EFFECTS: asks user which journal entry to delete and deletes it from journalLog
    public void deleteJournalEntry(int toDelete) {
        journalLog.delete(toDelete);
    }

    // MODIFIES: this
    // EFFECTS: - asks user which category to delete
    //          - checks that the category exists
    //          - deletes it from categoryList
    //          - recategorizes journal entries to 'uncategorized'
    //          - adds duration of affected journal entries to 'uncategorized' duration
    public void deleteCategory(String toDelete) {
        journalLog.uncategorize(categoryList.get(toDelete), categoryList.get(0));
        categoryList.delete(categoryList.get(toDelete));
    }


    // MODIFIES: this
    // EFFECTS: - asks user which category to edit
    //          - checks that the category exists
    //          - asks user what they want to rename the category to
    //          - renames category based on user input
    public void editCategory(String changeFrom, String changeTo) throws CategoryExistsException, NullEntryException {
        if (categoryList.isDuplicateName(changeTo)) {
            throw new CategoryExistsException();
        }

        if (changeTo.equals("")) {
            throw new NullEntryException();
        }

        categoryList.get(changeFrom).setName(changeTo);
    }

    public void checkValidForm(String description, String duration)
            throws NumberFormatException, NullEntryException, NegativeNumberException {
        if (duration == null || duration.equals("") || description == null || description.equals("")) {
            throw new NullEntryException();
        }
        int newDuration = Integer.parseInt(duration);
        if (newDuration < 0) {
            throw new NegativeNumberException();
        }
    }

    // REQUIRES: valid journal ID
    // MODIFIES: this
    // EFFECTS: edits journal entry description field based on user input
    public void editJournalEntryDescription(int journalID, String changeTo) {
        journalLog.getValue(journalID).setDescription(changeTo);
    }

    // REQUIRES: valid journal ID and positive duration
    // MODIFIES: this
    // EFFECTS: edits journal entry duration field based on user input
    public void editJournalEntryDuration(int journalID, String inputDuration) {
        int newDuration = Integer.parseInt(inputDuration);
        int oldDuration = journalLog.getValue(journalID).getDuration();
        Category affectedCategory = journalLog.getValue(journalID).getCategory();
        affectedCategory.setDuration(affectedCategory.getDuration() - oldDuration + newDuration);
        journalLog.getValue(journalID).setDuration(newDuration);
    }

    // MODIFIES: this
    // EFFECTS: helper for editJournalEntryCategory - updates category duration of category in which journal entry
    //          is being changed to
    public void editJournalEntryCategory(int journalID, String changeTo) {
        Category toCategory = categoryList.get(changeTo);
        Category fromCategory = journalLog.getValue(journalID).getCategory();
        int toCategoryDuration = toCategory.getDuration();
        int journalEntryDuration = journalLog.getValue(journalID).getDuration();
        toCategory.setDuration(toCategoryDuration + journalEntryDuration);
        fromCategory.setDuration(fromCategory.getDuration() - journalEntryDuration);
        journalLog.getValue(journalID).setCategory(toCategory);
    }

    public int getTotalCategoryDuration() {
        int totalDuration = 0;
        for (int i = 0; i < categoryList.getSize(); i++) {
            totalDuration += categoryList.get(i).getDuration();
        }
        return totalDuration;
    }

}
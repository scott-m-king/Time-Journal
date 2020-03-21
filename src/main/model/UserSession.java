package model;

import exceptions.CategoryExistsException;
import exceptions.NegativeNumberException;
import exceptions.NullEntryException;
import persistence.SaveReader;
import persistence.SaveWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// Represents a user session
public class UserSession {
    private CategoryList categoryList;     // declaration of new CategoryList
    private JournalLog journalLog;         // declaration of new JournalLog
    private int journalID = 1;             // starting ID of first journal entry, incremented by 1 for each new entry
    private int categoryID = 1;            // starting ID of first category (after Uncategorized)
    private String currentUser;            // current session user
    private String userAvatar;             // URL of user's chosen avatar
    private ArrayList<String> userList;    // list of all users

    public static final String USERS_LOCATION = "./data/users/";
    public static final String USER_SAVE_FILE = "./data/users_save.json";   // user list
    public static final String SESSION_SAVE_FILE = "session_save.json";     // user save file

    // EFFECTS: runs time journal application and instantiates new category list, journal log, loads users
    public UserSession() {
        categoryList = new CategoryList();
        journalLog = new JournalLog();
        userList = new ArrayList<>();
    }

    // EFFECTS: tries to load user save file, if successful returns false, otherwise returns true
    public boolean isFirstTime(String userFile) {
        boolean isFirstTime = false;
        try {
            SaveReader userReader = new SaveReader(userFile);
            userList = userReader.readUserList();
        } catch (IOException e) {
            isFirstTime = true;
        }
        return isFirstTime;
    }

    // REQUIRES: non-null input (empty string ok)
    // MODIFIES: this
    // EFFECTS: if input is not an empty string, set currentUser to input
    //          otherwise, throw NullEntryException
    public void setCurrentUser(String user) throws NullEntryException {
        if (user.equals("")) {
            throw new NullEntryException();
        } else {
            this.currentUser = user;
        }
    }

    // REQUIRES: null or existing file URL - doesn't handle URL not found exception
    // MODIFIES: this
    // EFFECTS: if fileURL is not null, set userAvatar to image at fileURL location
    //          otherwise, throw NullEntryException
    public void setUserAvatar(String fileURL) throws NullEntryException {
        if (fileURL != null) {
            this.userAvatar = fileURL;
        } else {
            throw new NullEntryException();
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
        categoryList.insert(uncategorized);
    }

    // EFFECTS: if save file doesn't already exist, create new directory and write save file to that directory
    //          if save file exists, overwrites existing file with new save file
    public void saveEntries(String locationUser, String locationUserList) {
        try {
            saveUserSession(locationUser);
            saveUserList(locationUserList);
        } catch (Exception e) {
            new File(locationUser + currentUser + "/").mkdir();
            saveEntries(locationUser, locationUserList);
        }
    }

    // MODIFIES: SaveWriter
    // EFFECTS: tries to save this UserSession, throws IOException if unsuccessful
    private void saveUserSession(String location) throws IOException {
        SaveWriter sessionSave = new SaveWriter(new File(
                location
                + currentUser
                + "/"
                + SESSION_SAVE_FILE
        ));
        sessionSave.save(this);
        sessionSave.close();
    }

    // MODIFIES: SaveWriter
    // EFFECTS: tries to save userList in current UserSession, throws IOException if unsuccessful
    private void saveUserList(String location) throws IOException {
        SaveWriter userListSave = new SaveWriter(new File(location));
        userListSave.save(userList);
        userListSave.close();
    }

    // MODIFIES: this
    // EFFECTS: if selected user is not null, sets current session user to selected user
    //          otherwise, throw NullEntryException
    public void selectUser(String choice, String location) throws NullEntryException, IOException {
        if (choice == null) {
            throw new NullEntryException();
        }
        currentUser = choice;
        loadEntries(location);
    }

    // MODIFIES: this
    // EFFECTS: tries to load user's previously saved UserSession
    //          if not found or corrupt, print message to console and start a new session
    public void loadEntries(String location) throws IOException {
        try {
            SaveReader journalReader = new SaveReader(location
                    + currentUser + "/" + SESSION_SAVE_FILE);
            UserSession loadedSession = journalReader.readUserSession();
            loadSessionFields(loadedSession);
            System.out.println("Save file successfully loaded. \n");
        } catch (FileNotFoundException e) {
            System.out.println("Save file does not exist. New session will be started.\n");
            newSession();
        }
    }

    // REQUIRES: an existing user with a loaded UserSession (deserialized object)
    // MODIFIES: this
    // EFFECTS: sets UserSession's fields to those in the loaded UserSession
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
            Category newCategory = new Category(categoryID++, null);
            newCategory.setName(newCategory.makeNormalName(name));
            categoryList.add(newCategory);
        } else {
            throw new CategoryExistsException();
        }
    }

    // REQUIRES: validated description, durationEntry, categoryEntry
    // MODIFIES: this, JournalEntry, JournalLog
    // EFFECTS: creates a new journal entry and adds it to journalLog
    public void createNewJournalEntry(String description, String durationEntry, String categoryEntry) {
        int durationFinal = Integer.parseInt(durationEntry);
        Category category = categoryList.get(categoryEntry);
        JournalEntry newEntry = new JournalEntry(
                journalID++, description, category.getCategoryID(), category, durationFinal);
        journalLog.add(newEntry);
    }

    // REQUIRES: existing Journal ID as key (toDelete)
    // MODIFIES: this
    // EFFECTS: asks user which journal entry to delete and deletes it from journalLog
    public void deleteJournalEntry(int toDelete) {
        journalLog.delete(toDelete);
    }

    // MODIFIES: this, Category, CategoryList
    // EFFECTS: - deletes category from categoryList
    //          - recategorizes journal entries to 'uncategorized'
    //          - adds duration of affected journal entries to 'uncategorized' duration
    public void deleteCategory(String toDelete) {
        journalLog.uncategorize(categoryList.get(toDelete), categoryList.get(0));
        categoryList.delete(categoryList.get(toDelete));
    }


    // REQUIRES: non-null changeTo string
    // MODIFIES: this, Category, CategoryList
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

    // MODIFIES: this
    // EFFECTS: tries to parse input
    //          throws exception if any field is not filled out or if duration input is not a positive integer
    public void checkValidForm(String description, String duration)
            throws NumberFormatException, NullEntryException, NegativeNumberException {
        if (duration == null || duration.equals("") || description == null || description.equals("")) {
            throw new NullEntryException();
        }
        int newDuration = Integer.parseInt(duration);
        if (newDuration < 1) {
            throw new NegativeNumberException();
        }
    }

    // REQUIRES: valid journal ID
    // MODIFIES: JournalLog, JournalEntry, this
    // EFFECTS: edits journal entry description field based on user input
    public void editJournalEntryDescription(int journalID, String changeTo) {
        journalLog.getValue(journalID).setDescription(changeTo);
    }

    // REQUIRES: valid journal ID and positive duration
    // MODIFIES: JournalEntry, JournalLog, this
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

    // EFFECTS: returns total duration spent in any given category
    public int getTotalCategoryDuration() {
        int totalDuration = 0;
        for (int i = 0; i < categoryList.getSize(); i++) {
            totalDuration += categoryList.get(i).getDuration();
        }
        return totalDuration;
    }

    // Getters and setters
    public CategoryList getCategoryList() {
        return categoryList;
    }

    public JournalLog getJournalLog() {
        return journalLog;
    }

    public int getJournalID() {
        return journalID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCategoryList(CategoryList categoryList) {
        this.categoryList = categoryList;
    }

    public void setJournalLog(JournalLog journalLog) {
        this.journalLog = journalLog;
    }

    public void setJournalID(int journalID) {
        this.journalID = journalID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

}
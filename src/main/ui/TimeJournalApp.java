package ui;

import exceptions.CategoryExistsException;
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
import java.util.Scanner;

/**
 * Design inspired by Teller App: https://github.students.cs.ubc.ca/CPSC210/TellerApp
 */

// Time Journal Application
public class TimeJournalApp {
    private CategoryList categoryList;     // declaration of new CategoryList
    private JournalLog journalLog;         // declaration of new JournalLog
    private Scanner input;                 // scanner object to take in user input
    private int journalID = 1;             // starting ID of first journal entry, incremented by 1 for each new entry
    private int categoryID = 1;            // starting ID of first category (after Uncategorized)
    private String currentUser;            // current session user
    private ArrayList<String> userList;    // list of all users

    public static final String USER_SAVE_FILE = "./data/users_save.json";
    public static final String JOURNAL_SAVE_FILE = "journal_save.json";
    public static final String CATEGORY_SAVE_FILE = "category_save.json";

    // EFFECTS: runs time journal application and instantiates new category list, journal log, loads users
    public TimeJournalApp() {
        input = new Scanner(System.in);
        categoryList = new CategoryList();
        journalLog = new JournalLog();
        userList = new ArrayList<>();
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

    // EFFECTS: ends user session and prompts to save or not
    public void endSession() {
        System.out.println("See you next time, " + currentUser + "!");
    }

    // MODIFIES: save files
    // EFFECTS: saves CategoryList and JournalLog to JSON files
    public void saveEntries() {
        try {
            SaveWriter journalSave = new SaveWriter(new File("./data/users/"
                    + currentUser + "/" + JOURNAL_SAVE_FILE));
            journalSave.save(journalLog);
            journalSave.close();

            SaveWriter categorySave = new SaveWriter(new File("./data/users/"
                    + currentUser + "/" + CATEGORY_SAVE_FILE));
            categorySave.save(categoryList);
            categorySave.close();

            SaveWriter userListSave = new SaveWriter(new File(USER_SAVE_FILE));
            userListSave.save(userList);
            userListSave.close();

        } catch (FileNotFoundException e) {
            new File("./data/users/" + currentUser + "/").mkdir();
            saveEntries();
        } catch (IOException e) {
            e.printStackTrace();
            // programming error
        }
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

    public String getUserWelcome() {
        if (currentUser.charAt(currentUser.length() - 1) == 's') {
            return currentUser + "'\nTime Journal";
        } else {
            return currentUser + "'s\nTime Journal";
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates current object instances with deserialized JSON files
    private void loadEntries() {
        try {
            SaveReader journalReader = new SaveReader("./data/users/"
                    + currentUser + "/" + JOURNAL_SAVE_FILE);
            SaveReader categoryReader = new SaveReader("./data/users/"
                    + currentUser + "/" + CATEGORY_SAVE_FILE);
            journalLog = journalReader.readJournalEntries();
            categoryList = categoryReader.readCategoryList();
            journalLog.updateWithLoadedCategories(categoryList);
            journalID = journalLog.getNextJournalID();
            categoryID = categoryList.getNextCategoryID();
            System.out.println("Save file successfully loaded. \n");
        } catch (FileNotFoundException e) {
            System.out.println("Save file does not exist. New session will be started.\n");
            newSession();
        } catch (IOException e) {
            System.out.println("Invalid save file. New session will be started. \n");
            newSession();
        }
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
    public void createNewJournalEntry(String description, String durationEntry, String categoryEntry)
            throws NullEntryException, NumberFormatException {
        if (description.equals("") || durationEntry.equals("")) {
            throw new NullEntryException();
        } else {
            int durationFinal = Integer.parseInt(durationEntry);
            Category category = categoryList.get(categoryEntry);
            JournalEntry newEntry = new JournalEntry(
                    journalID++, description, category.getCategoryID(), category, durationFinal);
            journalLog.add(newEntry);
        }
    }

    // EFFECTS: validates user input to make sure it's a positive integer
    // problem: inputs starting with integer and ending in string - eg. "45 minutes"
    private int inputIntegerValidation(String question, String list) {
        input.nextLine();
        int choice;
        do {
            System.out.println(question);
            while (!input.hasNextInt()) {
                System.out.println(list);
                System.out.println("Invalid entry. " + question);
                input.nextLine();
            }
            choice = input.nextInt();
        } while (choice <= 0);
        return choice;
    }

    // EFFECTS: displays list of journal entries and gives user choice to edit/delete/go back to home
    private void journalLogMenu() {
        if (journalLog.getSize() == 0) {
            System.out.println("You currently have no journal entries. \n");
        } else {
            boolean toContinue = true;
            while (toContinue) {
                System.out.println(journalLog.printLog());
                editDeleteMenu();
                String choice = input.next();
                if (choice.equals("1")) {
                    editJournalEntry();
                    toContinue = false;
                } else if (choice.equals("2")) {
                    deleteJournalEntry();
                    toContinue = false;
                } else if (!choice.equals("3")) {
                    System.out.println("Sorry, that's an invalid choice. \n");
                } else {
                    toContinue = false;
                }
            }
        }
    }

    // EFFECTS: displays list of edit options for viewEditJournalLog and viewEditCategoryList
    private void editDeleteMenu() {
        System.out.println("What would you like to do? (Enter number)");
        System.out.println("1. Edit");
        System.out.println("2. Delete");
        System.out.println("3. Back to main menu");
    }

    // MODIFIES: this
    // EFFECTS: asks user which journal entry to delete and deletes it from journalLog
    private void deleteJournalEntry() {
        if (journalLog.getSize() == 0) {
            System.out.println("Nothing to delete - your journal log is currently empty.\n");
        } else {
            System.out.println(journalLog.printLog());
            String question = "Enter the ID number of the journal entry you would like to delete:";
            int choice = inputIntegerValidation(question, journalLog.printLog());
            if (journalLog.hasID(choice)) {
                journalLog.delete(choice);
                System.out.println("The entry has successfully been deleted.");
            } else {
                System.out.println("Sorry, that entry does not exist.");
            }
        }
        journalLogMenu();
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
    public void editCategory(String changeFrom, String changeTo) throws CategoryExistsException {
        if (categoryList.isDuplicateName(changeTo)) {
            throw new CategoryExistsException();
        }
        categoryList.get(changeFrom).setName(changeTo);
    }

    // MODIFIES: this
    // EFFECTS: - asks user which journal entry to edit
    //          - checks if journal entry exists
    //          - edits journal entry
    private void editJournalEntry() {
        boolean toContinue = true;
        while (toContinue) {
            System.out.println(journalLog.printLog());
            String question = "Which entry would you like to edit? Enter ID number";
            int editChoice = inputIntegerValidation(question, journalLog.printLog());
            if (journalLog.hasID(editChoice)) {
                editJournalEntryMenu(editChoice);
                toContinue = false;
            } else {
                System.out.println("Sorry, that was an invalid choice.\n");
                toContinue = true;
            }
        }
        journalLogMenu();
    }

    // REQUIRES: valid journal ID
    // MODIFIES: this
    // EFFECTS: - asks user what field of journal entry they want to edit
    //          - edits journal entry
    private void editJournalEntryMenu(int journalID) {
        boolean toContinue = true;
        while (toContinue) {
            System.out.println("Select what to edit: ");
            System.out.println("1. Category");
            System.out.println("2. Duration");
            System.out.println("3. Description");
            String choice = input.next();
            if (choice.equals("1")) {
                editJournalEntryCategory(journalID);
                toContinue = false;
            } else if (choice.equals("2")) {
                editJournalEntryDuration(journalID);
                toContinue = false;
            } else if (choice.equals("3")) {
                editJournalEntryDescription(journalID);
                toContinue = false;
            } else {
                System.out.println("Sorry, please select another option.\n");
                toContinue = true;
            }
        }
    }

    // REQUIRES: valid journal ID
    // MODIFIES: this
    // EFFECTS: modifies journal entry category field based on user input
    private void editJournalEntryCategory(int journalID) {
        boolean toContinue = true;
        while (toContinue) {
            System.out.println(categoryList.printList());
            int changeTo = inputIntegerValidation("What would you like to change the category to? "
                    +  "Enter number below.", categoryList.printList());
            if (changeTo > 0 && changeTo <= categoryList.getSize()) {
                editJournalEntryCategoryHelper(changeTo, journalID);
                System.out.println("You have successfully edited the entry.\n");
                toContinue = false;
            } else {
                System.out.println("Sorry, that category does not exist. \n");
                toContinue = true;
            }
        }
    }

    // REQUIRES: valid journal ID and positive duration
    // MODIFIES: this
    // EFFECTS: helper for editJournalEntryCategory - updates category duration of category in which journal entry
    //          is being changed to
    private void editJournalEntryCategoryHelper(int changeTo, int journalID) {
        Category toCategory = categoryList.get(changeTo - 1);
        System.out.println(journalID);
        Category fromCategory = journalLog.getValue(journalID).getCategory();
        int toCategoryDuration = toCategory.getDuration();
        int journalEntryDuration = journalLog.getValue(journalID).getDuration();

        toCategory.setDuration(toCategoryDuration + journalEntryDuration);
        fromCategory.setDuration(fromCategory.getDuration() - journalEntryDuration);
        journalLog.getValue(journalID).setCategory(toCategory);
    }

    // REQUIRES: valid journal ID
    // MODIFIES: this
    // EFFECTS: edits journal entry duration field based on user input
    private void editJournalEntryDuration(int journalID) {
        int newDuration = inputIntegerValidation("What would you like to change the duration to? "
                + "Enter number in minutes below:", "");
        int oldDuration = journalLog.getValue(journalID).getDuration();
        Category affectedCategory = journalLog.getValue(journalID).getCategory();

        affectedCategory.setDuration(affectedCategory.getDuration() - oldDuration + newDuration);
        journalLog.getValue(journalID).setDuration(newDuration);
        System.out.println("You have successfully edited the entry.\n");
    }

    // REQUIRES: valid journal ID
    // MODIFIES: this
    // EFFECTS: edits journal entry description field based on user input
    private void editJournalEntryDescription(int journalID) {
        System.out.println("What would you like to change the description to? This will overwrite the current one.");
        input.nextLine();
        String changeTo = input.nextLine();
        journalLog.getValue(journalID).setDescription(changeTo);
        System.out.println("You have successfully edited the entry.\n");
    }

    // Getters
    public CategoryList getCategoryList() {
        return categoryList;
    }

    public JournalLog getJournalLog() {
        return journalLog;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

}
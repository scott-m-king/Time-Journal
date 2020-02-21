package ui;

import model.Category;
import model.CategoryList;
import model.JournalEntry;
import model.JournalLog;
import persistence.SaveReader;
import persistence.SaveWriter;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Design inspired by Teller App: https://github.students.cs.ubc.ca/CPSC210/TellerApp
 */

// Time Journal Application
public class TimeJournalApp {
    private CategoryList categoryList;   // declaration of new CategoryList
    private JournalLog journalLog;       // declaration of new JournalLog
    private Scanner input;               // scanner object to take in user input
    private int journalID = 1;           // starting ID of first journal entry, incremented by 1 for each new entry
    private int categoryID = 1;          // starting ID of first category (after Uncategorized)
    private Category uncategorized;

    public static final String JOURNAL_SAVE_FILE = "./data/journal_save.json";
    public static final String CATEGORY_SAVE_FILE = "./data/category_save.json";

    // EFFECTS: runs time journal application and instantiates new category and journal entry logs
    public TimeJournalApp() {
        categoryList = new CategoryList();
        journalLog = new JournalLog();
        runJournal();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runJournal() {
        boolean keepGoing = true;
        String command;
        input = new Scanner(System.in);
        intro();
        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();
            if (command.equals("5")) {
                endSession();
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
    }

    // EFFECTS: ends user session and prompts to save or not
    private void endSession() {
        System.out.println("Would you like to save your session?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        String choice = input.next();
        if (choice.equals("1")) {
            saveEntries();
            System.out.println("See you next time!");
        } else if (choice.equals("2")) {
            System.out.println("See you next time!");
        } else {
            System.out.println("Invalid selection.");
            endSession();
        }
    }

    /**
     * When CategoryList is instantiated in a new session, it comes pre-populated with the 'Uncategorized'
     * Category object that acts as a 'default' category. Uncategorized is just like a normal Category but is
     * non-modifiable and non-deletable. When a Category is deleted from CategoryList, all journal entries tagged
     * with that category will automatically be re-assigned to Uncategorized and the duration will be updated
     * accordingly.
     */

    // MODIFIES: this
    // EFFECTS: prompts user to enter first category
    private void intro() {
        uncategorized = new Category(0, "Uncategorized");
        System.out.println("Hello! Welcome to Time Journal. What type of user are you? Enter number: ");
        System.out.println("1. New User");
        System.out.println("2. Returning User");

        String choice = input.next();
        if (choice.equals("1")) {
            newSession();
        } else if (choice.equals("2")) {
            System.out.println("Welcome back - what would you like to do? Enter number: ");
            System.out.println("1. Load save file");
            System.out.println("2. Start fresh");
            choice = input.next();
            if (choice.equals("1")) {
                loadEntries();
            } else {
                newSession();
            }
        } else {
            System.out.println("Invalid entry.");
            intro();
        }
    }

    // EFFECTS: creates a new session by prompting use for first category
    public void newSession() {
        categoryList.add(uncategorized);
        System.out.println("Let's start by creating your first category. ");
        createNewCategory();
    }

    // EFFECTS: displays all menu options to the user
    private void displayMenu() {
        System.out.println("What would you like to do now? (Enter number)");
        System.out.println("1. Create journal entry");
        System.out.println("2. Create category");
        System.out.println("3. View/Edit journal entries");
        System.out.println("4. View/Edit categories");
        System.out.println("5. Quit");
    }

    // MODIFIES: this
    // EFFECTS: processes commands
    private void processCommand(String command) {
        switch (command) {
            case "1":
                createNewJournalEntry();
                break;
            case "2":
                createNewCategory();
                break;
            case "3":
                journalLogMenu();
                break;
            case "4":
                categoryListMenu();
                break;
            default:
                System.out.println("Sorry, that is an invalid choice.");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new category and adds it to categoryList
    private void createNewCategory() {
        System.out.println("Enter a name for your category below: ");
        String newName = input.next();
        if (!categoryList.isDuplicateName(newName)) {
            Category newCategory = new Category(categoryID++, newName);
            categoryList.add(newCategory);
            System.out.println("Great! That category has been added to your list. " + "\n");
        } else {
            System.out.println("Sorry, that category already exists.");
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new journal entry and adds it to journalLog
    private void createNewJournalEntry() {
        System.out.println("What did you get up to? Enter a description for your journal entry:");
        Scanner descriptionInput = new Scanner(System.in);
        String description = descriptionInput.nextLine();
        boolean toContinue = true;
        while (toContinue) {
            String categoryQuestion = "What category would you like to place this under? (Enter number)";
            System.out.println(categoryList.printList());
            int choice = inputIntegerValidation(categoryQuestion, categoryList.printList());
            if (choice > 0 && choice <= categoryList.getSize()) {
                int categoryIndex = (choice - 1);
                Category category = categoryList.get(categoryIndex);
                int duration = inputIntegerValidation("How long did you spend on this? (in minutes)", "");
                JournalEntry newEntry = new JournalEntry(
                        journalID++, description, category.getId(), category, duration);
                journalLog.add(newEntry);
                toContinue = false;
            } else {
                System.out.println("Sorry, that was an invalid choice.");
            }
        }
        System.out.println("Your entry has been added. \n");
    }

    // EFFECTS: saves CategoryList and JournalLog to files
    private void saveEntries() {
        try {
            SaveWriter journalSave = new SaveWriter(new File(JOURNAL_SAVE_FILE));
            journalSave.saveFile(journalLog);
            journalSave.close();

            SaveWriter categorySave = new SaveWriter(new File(CATEGORY_SAVE_FILE));
            categorySave.saveFile(categoryList);
            categorySave.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: reads json files and converts back into objects
    private void loadEntries() {
        try {
            SaveReader journalReader = new SaveReader(JOURNAL_SAVE_FILE);
            SaveReader categoryReader = new SaveReader(CATEGORY_SAVE_FILE);

            journalLog = journalReader.readJournalEntries();
            categoryList = categoryReader.readCategoryList();
            journalLog.updateWithLoadedCategories(categoryList);
            journalID = journalLog.getNextJournalID();
            categoryID = categoryList.getNextCategoryID();

            System.out.println("Save file successfully loaded. \n");
        } catch (IOException e) {
            e.printStackTrace();
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

    // EFFECTS: displays list of categories and gives user choice to edit/delete/go back to home
    private void categoryListMenu() {
        if (categoryList.getSize() == 0) {
            System.out.println("You currently have no categories. \n");
        } else {
            boolean toContinue = true;
            while (toContinue) {
                System.out.println(categoryList.printList());
                editDeleteMenu();
                String choice = input.next();
                if (choice.equals("1")) {
                    editCategoryAndPrintList();
                    toContinue = false;
                } else if (choice.equals("2")) {
                    deleteCategory();
                    toContinue = false;
                } else if (!choice.equals("3")) {
                    System.out.println("Sorry, that's an invalid choice.\n");
                    toContinue = true;
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
    private void deleteCategory() {
        if (categoryList.getSize() != 1) {
            System.out.println(categoryList.printListExceptUncategorized());
            boolean toContinue = true;
            while (toContinue) {
                int select = inputIntegerValidation("Which category would you like to delete? (Select number)",
                        categoryList.printListExceptUncategorized());
                if (select > 0 && select <= categoryList.getSize() - 1) {
                    journalLog.uncategorize(categoryList.get(select), categoryList.get(0));
                    categoryList.delete(categoryList.get(select));
                    System.out.println("You have successfully deleted the category.");
                    toContinue = false;
                } else {
                    System.out.println("Invalid choice.");
                }
            }
        } else {
            System.out.println("There's nothing available to delete.\n");
        }
        categoryListMenu();
    }

    // EFFECTS: calls the editCategory method and prints category list menu
    private void editCategoryAndPrintList() {
        editCategory();
        categoryListMenu();
    }

    // MODIFIES: this
    // EFFECTS: - asks user which category to edit
    //          - checks that the category exists
    //          - asks user what they want to rename the category to
    //          - renames category based on user input
    private void editCategory() {
        if (categoryList.getSize() == 1) {
            System.out.println("There's nothing to edit.\n");
            return;
        }

        System.out.println(categoryList.printListExceptUncategorized());
        int editChoice = 1 + inputIntegerValidation("Which category would you like to edit? Enter number:",
                categoryList.printListExceptUncategorized());

        if (editChoice <= 1 || editChoice > categoryList.getSize()) {
            System.out.println("Sorry, that is an invalid choice.\n");
            return;
        }

        System.out.println("What would you like to change the category name to?");
        String editName = input.next();
        if (categoryList.isDuplicateName(editName)) {
            System.out.println("Sorry, that category name already exists.\n");
            return;
        }

        categoryList.get(editChoice - 1).setName(editName);
        System.out.println("You've successfullly edited the category.\n");
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

}
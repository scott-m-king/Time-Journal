package ui;

import model.Category;
import model.CategoryList;
import model.JournalEntry;
import model.JournalLog;

import java.util.Scanner;

/**
 * Design inspired by https://github.students.cs.ubc.ca/CPSC210/TellerApp
 */

// TODO: try to make duration a loop....

// Time Journal Application
public class TimeJournalApp {
    private CategoryList categoryList;
    private JournalLog journalLog;
    private Scanner input;
    private int id = 1;

    // EFFECTS: runs time journal application and initializes category and journal entry logs
    public TimeJournalApp() {
        categoryList = new CategoryList();
        journalLog = new JournalLog();
        runJournal();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    public void runJournal() {
        boolean keepGoing = true;
        String command;
        input = new Scanner(System.in);
        intro();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("5")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to enter first category
    public void intro() {
        System.out.print("Hello! Welcome to Time Journal. Let's start by creating your first category. ");
        createNewCategory();
    }

    // EFFECTS: displays all menu options to the user
    public void displayMenu() {
        System.out.println("What would you like to do now? (Enter number)");
        System.out.println("1. Create journal entry");
        System.out.println("2. Create category");
        System.out.println("3. View/Edit journal entries");
        System.out.println("4. View/Edit categories");
        System.out.println("5. Quit");
    }

    // MODIFIES: this
    // EFFECTS: processes commands
    public void processCommand(String command) {
        if (command.equals("1")) {
            createNewJournalEntry();
        } else if (command.equals("2")) {
            createNewCategory();
        } else if (command.equals("3")) {
            viewEditJournalLog();
        } else if (command.equals("4")) {
            viewEditCategoryList();
        } else {
            System.out.println("Sorry, that is an invalid choice.");
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new category and adds it to categoryList
    public void createNewCategory() {
        System.out.println("Enter a name for your category below: ");
        String newName = input.next();
        if (!categoryList.doesCategoryAlreadyExist(newName)) {
            Category newCategory = new Category(newName);
            categoryList.addCategory(newCategory);
            System.out.println("Great! That category has been added to your list. " + "\n");
        } else {
            System.out.println("Sorry, that category already exists.");
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a new journal entry and adds it to journalLog
    // TODO: error handling for invalid entries
    public void createNewJournalEntry() {
        System.out.println("What did you get up to? Enter a description for your journal entry:");
        input.nextLine();
        String description = input.nextLine();

        System.out.println("What category would you like to place this entry under? (Enter number)");
        categoryList.printList();
        int choice = input.nextInt();
        int categoryIndex = (choice - 1);
        Category category = categoryList.getCategory(categoryIndex);

        System.out.println("How long did you spend on this? (In minutes)");
        int duration = input.nextInt();

        JournalEntry newEntry = new JournalEntry(id++, description, category, duration);
        journalLog.addJournalEntry(newEntry);
    }

    // EFFECTS: displays list of journal entries and gives user choice to edit/delete/go back to home
    public void viewEditJournalLog() {
        if (journalLog.getSize() == 0) {
            System.out.println("You currently have no journal entries. ");
        } else {
            journalLog.printLog();
            boolean toContinue = true;
            while (toContinue) {
                editDeleteMenu();
                String choice = input.next();
                if (choice.equals("1")) {
                    editJournalEntry();
                    toContinue = false;
                } else if (choice.equals("2")) {
                    deleteJournalEntry();
                    toContinue = false;
                } else if (!choice.equals("3")) {
                    System.out.println("Sorry, that's an invalid choice.");
                } else {
                    toContinue = false;
                }
            }
        }
    }

    // EFFECTS: displays list of categories and gives user choice to edit/delete/go back to home
    public void viewEditCategoryList() {
        if (categoryList.getSize() == 0) {
            System.out.println("You currently have no categories. ");
        } else {
            categoryList.printList();
            boolean toContinue = true;
            while (toContinue) {
                editDeleteMenu();
                String choice = input.next();
                if (choice.equals("1")) {
                    editCategory();
                    toContinue = false;
                } else if (choice.equals("2")) {
                    deleteCategory();
                    toContinue = false;
                } else if (!choice.equals("3")) {
                    System.out.println("Sorry, that's an invalid choice.");
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
    public void deleteJournalEntry() {
        if (journalLog.getSize() == 0) {
            System.out.println("Nothing to delete - your journal log is currently empty.");
        } else {
            System.out.println("Enter the ID number of the journal entry you would like to delete:");
            journalLog.deleteJournalEntry(input.nextInt());
            System.out.println("You have successfully deleted the entry.");
        }
    }

    // MODIFIES: this
    // EFFECTS: - asks user which category to delete
    //          - checks that the category exists
    //          - deletes it from categoryList
    //          - recategorizes journal entries to 'uncategorized'
    //          - adds duration of affected journal entries to 'uncategorized' duration
    public void deleteCategory() {
        System.out.println("Which category would you like to delete? (Select number)");
        int select = input.nextInt();
        if (select > 0 && select <= categoryList.getSize()) {
            journalLog.uncategorize(categoryList.getCategory(select - 1), categoryList.getCategory(0));
            categoryList.deleteCategory(categoryList.getCategory(select - 1));
            System.out.println("You have successfully deleted the category.");
        } else {
            System.out.println("Nothing to delete - that category does not exist.");
        }
    }

    // MODIFIES: this
    // EFFECTS: - asks user which category to edit
    //          - checks that the category exists
    //          - asks user what they want to rename the category to
    //          - renames category based on user input
    public void editCategory() {
        System.out.println("Which category would you like to edit? Enter number:");
        categoryList.printListExceptUncategorized();
        int editChoice = input.nextInt();
        if (editChoice > 1 && editChoice <= categoryList.getSize()) {
            System.out.println("What would you like to change the category name to?");
            String editName = input.next();
            categoryList.getCategory(editChoice).setName(editName);
        } else {
            System.out.println("Sorry, that is an invalid choice.");
        }
    }

    // MODIFIES: this
    // EFFECTS: - asks user which journal entry to edit
    //          - checks if journal entry exists
    //          - edits journal entry
    public void editJournalEntry() {
        boolean toContinue = true;
        while (toContinue) {
            System.out.println("Which entry would you like to edit? Enter ID number");
            int editChoice = input.nextInt();
            if (journalLog.hasID(editChoice)) {
                editJournalEntryMenu(editChoice);
                toContinue = false;
            } else {
                System.out.println("Sorry, that was an invalid choice.");
                toContinue = true;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: - asks user what field of journal entry they want to edit
    //          - edits journal entry
    public void editJournalEntryMenu(int journalID) {
        System.out.println("Select what to edit: ");
        System.out.println("1. Category");
        System.out.println("2. Duration");
        System.out.println("3. Description");
        String choice = input.next();
        boolean toContinue = true;
        while (toContinue) {
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
                System.out.println("Sorry, please select another option.");
                toContinue = true;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: modifies journal entry category field based on user input
    public void editJournalEntryCategory(int journalID) {
        System.out.println("What would you like to change the category to? Enter number below.");
        categoryList.printList();
        int changeTo = input.nextInt();
        boolean toContinue = true;
        while (toContinue) {
            if (changeTo > 0 && changeTo <= categoryList.getSize()) {
                updateCategory(changeTo, journalID);
                System.out.println("You have successfully edited the entry.");
                toContinue = false;
            } else {
                System.out.println("Sorry, that category does not exist. ");
                toContinue = true;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: modifies category in categoryLog based on user input
    public void updateCategory(int changeTo, int journalID) {
        Category toCategory = categoryList.getCategory(changeTo - 1);
        Category fromCategory = journalLog.getValue(journalID).getCategory();

        int toCategoryDuration = toCategory.getDuration();
        int journalEntryDuration = journalLog.getValue(journalID).getDuration();

        toCategory.setDuration(toCategoryDuration + journalEntryDuration);
        fromCategory.setDuration(fromCategory.getDuration() - journalEntryDuration);

        journalLog.getValue(journalID).setCategory(toCategory);
    }

    // MODIFIES: this
    // EFFECTS: edits journal entry duration field based on user input
    // TODO: fix duration issue - not adding to duration of category that was switched to
    public void editJournalEntryDuration(int journalID) {
        System.out.println("What would you like to change the duration to? Enter number in minutes below:");
        int changeTo = input.nextInt();
        journalLog.getValue(journalID).setDuration(changeTo);
        System.out.println("You have successfully edited the entry.");
    }

    // MODIFIES: this
    // EFFECTS: edits journal entry description field based on user input
    public void editJournalEntryDescription(int journalID) {
        System.out.println("What would you like to change the description to? This will overwrite the current one.");
        String changeTo = input.next();
        journalLog.getValue(journalID).setDescription(changeTo);
        System.out.println("You have successfully edited the entry.");
    }

}
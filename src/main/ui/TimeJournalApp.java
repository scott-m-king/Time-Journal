package ui;

import model.Category;
import model.CategoryList;
import model.JournalEntry;
import model.JournalLog;

import java.util.Scanner;

/**
 * Design inspired by Teller App: https://github.students.cs.ubc.ca/CPSC210/TellerApp
 */

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
            journalLogMenu();
        } else if (command.equals("4")) {
            categoryListMenu();
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
    public void createNewJournalEntry() {
        System.out.println("What did you get up to? Enter a description for your journal entry:");
        Scanner descriptionInput = new Scanner(System.in);
        String description = descriptionInput.nextLine();

        boolean toContinue = true;
        while (toContinue) {
            String categoryQuestion = "What category would you like to place this under? (Enter number)";
            System.out.println(categoryList.printList());
            int choice = inputIntegerValidation(categoryQuestion);
            if (choice > 0 && choice <= categoryList.getSize()) {
                int categoryIndex = (choice - 1);
                Category category = categoryList.getCategory(categoryIndex);
                String durationQuestion = "How long did you spend on this? (in minutes)";
                int duration = inputIntegerValidation(durationQuestion);
                JournalEntry newEntry = new JournalEntry(id++, description, category, duration);
                journalLog.addJournalEntry(newEntry);
                toContinue = false;
            } else {
                System.out.println("Sorry, that was an invalid choice.");
            }
        }
        System.out.println("Your entry has been added. \n");
    }

    // EFFECTS: validates user input to make sure it's a positive integer
    // problem: inputs starting with integer and ending in string - eg. "45 minutes"
    public int inputIntegerValidation(String question) {
        input.nextLine();
        int choice;
        do {
            System.out.println(question);
            while (!input.hasNextInt()) {
                System.out.println("Invalid entry. " + question);
                input.nextLine();
            }
            choice = input.nextInt();
        } while (choice <= 0);
        return choice;
    }

    // EFFECTS: displays list of journal entries and gives user choice to edit/delete/go back to home
    public void journalLogMenu() {
        if (journalLog.getSize() == 0) {
            System.out.println("You currently have no journal entries. \n");
        } else {
            System.out.println(journalLog.printLog());
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
                    System.out.println("Sorry, that's an invalid choice. \n");
                } else {
                    toContinue = false;
                }
            }
        }
    }

    // EFFECTS: displays list of categories and gives user choice to edit/delete/go back to home
    public void categoryListMenu() {
        if (categoryList.getSize() == 0) {
            System.out.println("You currently have no categories. \n");
        } else {
            boolean toContinue = true;
            while (toContinue) {
                System.out.println(categoryList.printList());
                editDeleteMenu();
                String choice = input.next();
                if (choice.equals("1")) {
                    editCategory();
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
    public void deleteJournalEntry() {
        if (journalLog.getSize() == 0) {
            System.out.println("Nothing to delete - your journal log is currently empty.\n");
        } else {
            String question = "Enter the ID number of the journal entry you would like to delete:";
            int choice = inputIntegerValidation(question);
            if (journalLog.hasID(choice)) {
                journalLog.deleteJournalEntry(choice);
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
    public void deleteCategory() {
        if (categoryList.getSize() != 1) {
            String question = "Which category would you like to delete? (Select number)";
            System.out.println(categoryList.printListExceptUncategorized());
            boolean toContinue = true;
            while (toContinue) {
                int select = inputIntegerValidation(question);
                if (select > 0 && select <= categoryList.getSize() - 1) {
                    journalLog.uncategorize(categoryList.getCategory(select), categoryList.getCategory(0));
                    categoryList.deleteCategory(categoryList.getCategory(select));
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

    // MODIFIES: this
    // EFFECTS: - asks user which category to edit
    //          - checks that the category exists
    //          - asks user what they want to rename the category to
    //          - renames category based on user input
    public void editCategory() {
        if (categoryList.getSize() != 1) {
            System.out.println("Which category would you like to edit? Enter number:");
            System.out.println(categoryList.printListExceptUncategorized());
            int editChoice = input.nextInt() + 1;
            if (editChoice > 1 && editChoice <= categoryList.getSize()) {
                System.out.println("What would you like to change the category name to?");
                String editName = input.next();
                if (!categoryList.doesCategoryAlreadyExist(editName)) {
                    categoryList.getCategory(editChoice - 1).setName(editName);
                    System.out.println("You've successfullly edited the category.\n");
                } else {
                    System.out.println("Sorry, that category name already exists.\n");
                }
            } else {
                System.out.println("Sorry, that is an invalid choice.\n");
            }
        } else {
            System.out.println("There's nothing to edit.\n");
        }
        categoryListMenu();
    }

    // MODIFIES: this
    // EFFECTS: - asks user which journal entry to edit
    //          - checks if journal entry exists
    //          - edits journal entry
    public void editJournalEntry() {
        boolean toContinue = true;
        while (toContinue) {
            String question = "Which entry would you like to edit? Enter ID number";
            int editChoice = inputIntegerValidation(question);
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

    // MODIFIES: this
    // EFFECTS: - asks user what field of journal entry they want to edit
    //          - edits journal entry
    public void editJournalEntryMenu(int journalID) {
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

    // MODIFIES: this
    // EFFECTS: modifies journal entry category field based on user input
    public void editJournalEntryCategory(int journalID) {
        boolean toContinue = true;
        while (toContinue) {
            String question = "What would you like to change the category to? Enter number below.";
            System.out.println(categoryList.printList());
            int changeTo = inputIntegerValidation(question);
            if (changeTo > 0 && changeTo <= categoryList.getSize()) {
                updateCategory(changeTo, journalID);
                System.out.println("You have successfully edited the entry.\n");
                toContinue = false;
            } else {
                System.out.println("Sorry, that category does not exist. \n");
                toContinue = true;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: helper for editJournalEntryCategory - updates category duration of category in which journal entry
    //          is being changed to
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
    public void editJournalEntryDuration(int journalID) {
        String question = "What would you like to change the duration to? Enter number in minutes below:";
        int newDuration = inputIntegerValidation(question);
        int oldDuration = journalLog.getValue(journalID).getDuration();
        Category affectedCategory = journalLog.getValue(journalID).getCategory();
        affectedCategory.setDuration(affectedCategory.getDuration() - oldDuration + newDuration);
        journalLog.getValue(journalID).setDuration(newDuration);
        System.out.println("You have successfully edited the entry.\n");
    }

    // MODIFIES: this
    // EFFECTS: edits journal entry description field based on user input
    public void editJournalEntryDescription(int journalID) {
        System.out.println("What would you like to change the description to? This will overwrite the current one.");
        input.nextLine();
        String changeTo = input.nextLine();
        journalLog.getValue(journalID).setDescription(changeTo);
        System.out.println("You have successfully edited the entry.\n");
    }

}
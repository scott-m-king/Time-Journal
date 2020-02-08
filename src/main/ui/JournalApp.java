package ui;

import model.Category;
import model.CategoryList;
import model.JournalEntry;
import model.JournalLog;

import java.util.Scanner;

/**
 * Design inspired by https://github.students.cs.ubc.ca/CPSC210/TellerApp
 */

// TODO: Make uncategorized undeletable and uneditable
// TODO: Edit description and duration of journal entry

public class JournalApp {
    private CategoryList categoryList;
    private JournalLog journalEntries;
    private Scanner input;
    private int id = 1;

    public JournalApp() {
        categoryList = new CategoryList();
        journalEntries = new JournalLog();
        runJournal();
    }

    public void runJournal() {
        boolean keepGoing = true;
        String command = null;
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

    public void intro() {
        System.out.print("Hello! Welcome to Time Journal. Let's start by creating your first category. ");
        createNewCategory();
    }

    public void displayMenu() {
        System.out.println("What would you like to do now? (Enter number)");
        System.out.println("1. Create journal entry");
        System.out.println("2. Create category");
        System.out.println("3. View/Edit journal entries");
        System.out.println("4. View/Edit categories");
        System.out.println("5. Quit");
    }

    public void processCommand(String command) {
        if (command.equals("1")) {
            createNewJournalEntry();
        } else if (command.equals("2")) {
            createNewCategory();
        } else if (command.equals("3")) {
            viewJournalEntries();
        } else if (command.equals("4")) {
            viewCategoryList();
        } else {
            System.out.println("Sorry, that is an invalid choice.");
        }
    }

    public void createNewCategory() {
        System.out.println("Enter a name for your category below: ");
        Category newCategory = new Category(input.next());
        categoryList.addCategory(newCategory);
        System.out.println("Great! That category has been added to your list. " + "\n");
    }

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
        journalEntries.addJournalEntry(newEntry);
    }

    public void viewJournalEntries() {
        if (journalEntries.getSize() == 0) {
            System.out.println("You currently have no journal entries. ");
        } else {
            journalEntries.printLog();
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
                    System.out.println("Sorry, that's an invalid choice.");;
                    toContinue = true;
                } else {
                    toContinue = false;
                }
            }
        }
    }

    public void viewCategoryList() {
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
                    System.out.println("Sorry, that's an invalid choice.");;
                    toContinue = true;
                } else {
                    toContinue = false;
                }
            }
        }
    }

    private void editDeleteMenu() {
        System.out.println("What would you like to do? (Enter number)");
        System.out.println("1. Edit");
        System.out.println("2. Delete");
        System.out.println("3. Back to main menu");
    }


    public void deleteJournalEntry() {
        if (journalEntries.getSize() == 0) {
            System.out.println("Nothing to delete - your journal log is currently empty.");
        } else {
            System.out.println("Enter the ID number of the journal entry you would like to delete:");
            journalEntries.deleteJournalEntry(input.nextInt());
            System.out.println("You have successfully deleted the entry.");
        }
    }

    // TODO: to implement in category log
    public void deleteCategory() {
        System.out.println("Which category would you like to delete? (Select number)");
        int select = input.nextInt();
        if (select > 0 && select <= categoryList.getSize()) {
            journalEntries.uncategorize(categoryList.getCategory(select - 1), categoryList.getCategory(0));
            categoryList.deleteCategory(categoryList.getCategory(select - 1));
            System.out.println("You have successfully deleted the category.");
        } else {
            System.out.println("Nothing to delete - that category does not exist.");
        }
    }

    public void editCategory() {
        System.out.println("Which category would you like to edit? Enter number");
        int editChoice = input.nextInt();
        if (editChoice > 0 && editChoice <= categoryList.getSize()) {
            System.out.println("What would you like to change the category name to?");
            String editName = input.next();
            categoryList.getCategory(editChoice - 1).setName(editName);
        } else {
            System.out.println("Sorry, that is an invalid choice.");
        }
    }

    public void editJournalEntry() {
        System.out.println("Which entry would you like to edit? Enter ID number");
        int editChoice = input.nextInt();
        if (journalEntries.hasID(editChoice)) {
            editJournalEntryMenu(editChoice);
        } else {
            System.out.println("Sorry, that was an invalid choice.");
        }
    }

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

    public void editJournalEntryCategory(int journalID) {
        System.out.println("What would you like to change the category to? Enter number below.");
        categoryList.printList();
        int changeTo = input.nextInt();
        journalEntries.getValue(journalID).setCategory(categoryList.getCategory(changeTo - 1));
    }

    public void editJournalEntryDuration(int journalID) {

    }

    public void editJournalEntryDescription(int journalID) {

    }

}
package ui;

import model.Category;
import model.CategoryList;
import model.JournalEntry;
import model.JournalLog;

import java.util.Scanner;

/**
 * Design inspired by https://github.students.cs.ubc.ca/CPSC210/TellerApp
 */

// TODO: Recategorize journal entry
// TODO: Delete category

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
        System.out.println("3. View all entries");
        System.out.println("4. Delete Journal Entry");
        System.out.println("5. Quit");
    }

    public void processCommand(String command) {
        if (command.equals("1")) {
            createNewJournalEntry();
        } else if (command.equals("2")) {
            createNewCategory();
        } else if (command.equals("3")) {
            if (journalEntries.getSize() == 0) {
                System.out.println("You currently have no journal entries. ");
            } else {
                journalEntries.printLog();
            }
        } else if (command.equals("4")) {
            deleteJournalEntry();
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

    public void createNewJournalEntry() {
        System.out.println("What did you get up to? Enter a description for your journal entry:");
        input.nextLine();
        String description = input.nextLine();

        System.out.println("What category would you like to place this entry under? (Enter number)");
        categoryList.printList();
        int categoryIndex = (input.nextInt() - 1);
        Category category = categoryList.getCategory(categoryIndex);

        System.out.println("How long did you spend on this? (In minutes)");
        int duration = input.nextInt();

        JournalEntry newEntry = new JournalEntry(id++, description, category, duration);
        journalEntries.addJournalEntry(newEntry);
    }

    private void deleteJournalEntry() {
        if (journalEntries.getSize() == 0) {
            System.out.println("Nothing to delete - your journal log is currently empty.");
        } else {
            System.out.println("Enter the ID number of the journal entry you would like to delete:");
            journalEntries.printLog();
            journalEntries.deleteJournalEntry(input.nextInt());
            System.out.println("You have successfully deleted the entry.");
        }
    }

}

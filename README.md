# Time Journal

## An application to keep track of where you spend your time. 

YouTube Demo: https://www.youtube.com/watch?v=Ef2sltAZlJE&

Have you ever had a week go by and by the end of it, realize that you have no idea what you did that week? By keeping
 track of how we spend our time, we can take control of our actions to live more productive and well-balanced lives. 
 
_**Why Journaling?**_<br>
Research has shown that journaling can have powerful mental health benefits and act as a documentation tool for everyday
life. Time Journal takes journaling a step further by allowing you to record how much time you spend on the activities
and organize them into meaningful categories. 
 
_**How does it work?**_<br>
You will be able to create categories for your activities and see how much time you spend on each one. 
This application will combine common approaches to journaling and budgeting to create visibility into where your time
 goes. <br>

## User Stories
**Phase 1**
- As a user, I want to be able to create/edit/delete categories for where I spend my time 
- As a user, I want to be able to create/edit/delete journal entries with timestamps and amount of time spent
- As a user, I want to be able to categorize my journal entries based on the ones I created
- As a user, I want to be able to view a full log of my journal entries and categories

**Phase 2**
- As a user, I want to be able to save my journal entries to a file
- As a user, I want to be able to be prompted to load my save file or start a new journal
- As a user, I want to be prompted to save my file (or not) when exiting the program

**Phase 3**
- As a user, I want to be able to view a piechart of my categories
- As a user, I want to be able to view a filtered list of journal entries based on a selected category
- As a user, I want to be able to choose a cute display picture 
- As a user, I want to keep my categories consistent by auto-capitalizing the first letter of each new category
- As a user, I want to be able to resize my GUI to my liking 

## Instructions for Grader

- You can generate the first required event by navigating to the Create Journal Entry page and filling in the form for a new journal entry. This will create a new journal entry and add it to the map of Journal Entries which will be updated and refreshed on the Journal Log page after the entry has been entered. Hitting the enter key while on the create page will also trigger the entry (enter key will actually work for all pages with a submit button). You can do the exact same things with Categories, the only difference being that it will prevent you from entering a duplicate category. 
- You can generate the second required event by navigating to the Journal Entry Log screen, clicking on an entry, and hitting the 'Edit" button on the top right. This will show an edit entry popup which you can then use to edit any entry. The enter button will also work here. 
- You can locate my visual component by starting the program, and selecting that you are a new user. After the name page, you will see an Avatar page which will let you choose an avatar as your display picture. These files are located in the src/ui/resources package.
- You can trigger my audio component by creating or deleting either a journal entry or category. Once you hit submit, you will hear a corresponding confirmation/delete sound. These wav files are located in the src/ui/resources folder. 
- You can save the state of my application by exiting the program (either with the exit button at the bottom or the sidebar or with the window exit button). You will be prompted to save with a popup in the middle of the screen.
- You can reload the state of my application by opening the app and selecting 'Returning User". You will be able to find your username from the dropdown list. 

**Full feature list of GUI:**
- Create new user and choose avatar
- View piechart of all categories on home screen (hover over category for more details)
- Add/create/delete Journal Entry
- Add/create/delete Category
- View filtered list of journal entries based on selected category 
- Option to save upon exit and load saved state when starting program again 
- Resize window - (cannot get smaller than initial size)

## Phase 4
**Task 2**
- In the ```UserSession``` class, the ```createNewCategory()``` method throws a ```NullEntryException``` if the user does not enter a category in the category field, and a ```CategoryExistsException``` if the user enters the name of a category that already exists in their category list. This is comprehensively tested in ```UserSessionTest```.
- In the ```ui.screens``` package, each screen in the GUI is a subclass of either the ```Screen``` or ```Popup``` abstract classes, and those abstract classes implement the ```Display``` interface which requires ```initializeScreen()``` to be implemented (reminds the individual working on the system that they need to add a ```Pane``` object to a ```Scene``` for JavaFX).
- The ```JournalLog``` class' only field is a ```HashMap<Integer, JournalEntry>``` and its values are accessed throughout the program using the ```journalID``` as the key. 

**Task 3**
- Extracted ```setMainStageDimensions()``` and ```setMiddle()``` from ```UserInterface``` into a new class called ```StageHelper```. These methods didn't really belong in ```UserInterface``` as its main job is to act as a manager for all the screen/popup objects active in the current session (and not the dimensions of a stage). This is a cohesion fix. 
- From ```CategoryListScreen```, extracted all the methods relating to creating the ```ListView``` in the Category List page to ```CategoryListComponent```. ```CategoryListScreen``` calls ```getCategoryListView()``` from ```CategoryListComponent``` to get the current list of categories in a ```ListView``` object. This behavior belonged in its own class as the ```Screen``` and ```Popup``` classes should be there purely to render content onto a ```Stage```. This is a cohesion fix. 
- Similar to above, all methods relating to generating a filtered ```TableView``` of ```JournalEntry``` have been moved from ```CategoryListScreen``` to ```JournalTableFilterComponent``` to improve the cohesion of the class. 

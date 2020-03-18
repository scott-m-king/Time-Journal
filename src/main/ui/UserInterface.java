package ui;

import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.JournalEntry;
import ui.components.AvatarPickerComponent;
import ui.screens.*;
import ui.components.CategoryChartComponent;
import ui.components.JournalTableComponent;
import ui.components.SideBarComponent;

import java.awt.*;

public class UserInterface extends Application {
    private NewUserWelcomeScreen newUserWelcomeScreen;
    private NewUserNameScreen newUserNameScreen;
    private NewUserAvatarScreen newUserAvatarScreen;
    private AvatarPickerComponent avatarPickerComponent;
    private FirstNewCategoryScreen firstNewCategoryScreen;
    private UserSelectScreen userSelectScreen;
    private SideBarComponent sideBarComponent;
    private JournalEntryCreateScreen journalEntryCreateScreen;
    private JournalEntryEditPopup journalEntryEditPopup;
    private JournalLogScreen journalLogScreen;
    private HomePageScreen homePageScreen;
    private CategoryListScreen categoryListScreen;
    private JournalTableComponent journalTableObject;
    private CategoryChartComponent categoryChartComponent;
    private CreateCategoryPopup createCategoryPopup;
    private EditCategoryPopup editCategoryPopup;
    private SavePromptPopup savePromptPopup;
    private WelcomeScreen welcomeScreen;
    private Dimension screenSize;
    private TableView<JournalEntry> journalTableView;
    private UserSession currentSession;
    private Stage mainStage;

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final int TITLE_FONT_SIZE = 35;

    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    @Override
    public void start(Stage stage) {
        this.mainStage = stage;
        mainStage.setTitle("Time Journal");
        currentSession = new UserSession();
        boolean noSaveFile = currentSession.isFirstTime();

        setMainStageDimensions();
        initializeAllScreens();

        if (noSaveFile) {
            newUserWelcomeScreen.renderNewUserWelcomeScreen();
        } else {
            welcomeScreen.renderWelcomeScreen();
        }
    }

    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void setMainStageDimensions() {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainStage.setWidth(WINDOW_WIDTH);
        mainStage.setHeight(WINDOW_HEIGHT);
        mainStage.setMinWidth(WINDOW_WIDTH);
        mainStage.setMinHeight(WINDOW_HEIGHT);
        setMiddle(mainStage);
    }

    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void initializeAllScreens() {
        // completely new user
        newUserWelcomeScreen = new NewUserWelcomeScreen(this);
        avatarPickerComponent = new AvatarPickerComponent();
        newUserNameScreen = new NewUserNameScreen(this);
        newUserAvatarScreen = new NewUserAvatarScreen(this);
        firstNewCategoryScreen = new FirstNewCategoryScreen(this);

        // save files already exist
        welcomeScreen = new WelcomeScreen(this);
        userSelectScreen = new UserSelectScreen(this);

        // home screen
        homePageScreen = new HomePageScreen(this);

        // create journal entry
        journalEntryCreateScreen = new JournalEntryCreateScreen(this);

        // view journal log
        journalLogScreen = new JournalLogScreen(this);
        journalEntryEditPopup = new JournalEntryEditPopup(this);

        // category list
        categoryListScreen = new CategoryListScreen(this);
        createCategoryPopup = new CreateCategoryPopup(this);
        editCategoryPopup = new EditCategoryPopup(this);

        // components
        sideBarComponent = new SideBarComponent(this);
        savePromptPopup = new SavePromptPopup(this);
        categoryChartComponent = new CategoryChartComponent(this);
        journalTableObject = new JournalTableComponent();
    }

    // EFFECTS: returns the NewUserWelcomeScreen object
    public NewUserWelcomeScreen getNewUserWelcomeScreen() {
        return newUserWelcomeScreen;
    }

    // EFFECTS: returns the NewUserNameScreen object
    public NewUserNameScreen getNewUserNameScreen() {
        return newUserNameScreen;
    }

    // EFFECTS: returns the NewUserAvatarScreen object
    public NewUserAvatarScreen getNewUserAvatarScreen() {
        return newUserAvatarScreen;
    }

    // EFFECTS: returns the AvatarPickerComponent object
    public AvatarPickerComponent getAvatarPickerComponent() {
        return avatarPickerComponent;
    }

    // EFFECTS: returns the FirstNewCategoryScreen object
    public FirstNewCategoryScreen getFirstNewCategoryScreen() {
        return firstNewCategoryScreen;
    }

    // EFFECTS: returns the UserSelectScreen object
    public UserSelectScreen getUserSelectScreen() {
        return userSelectScreen;
    }

    // EFFECTS: returns the SideBarComponent object
    public SideBarComponent getSideBarComponent() {
        return sideBarComponent;
    }

    // EFFECTS: returns the JournalEntryCreateScreen object
    public JournalEntryCreateScreen getJournalEntryCreateScreen() {
        return journalEntryCreateScreen;
    }

    // EFFECTS: returns the JournalEntryEditPopup object
    public JournalEntryEditPopup getJournalEntryEditPopup() {
        return journalEntryEditPopup;
    }

    // EFFECTS: returns the JournalLogScreen object
    public JournalLogScreen getJournalLogScreen() {
        return journalLogScreen;
    }

    // EFFECTS: returns the CategoryListScreen object
    public CategoryListScreen getCategoryListScreen() {
        return categoryListScreen;
    }

    // EFFECTS: returns the JournalTableComponent object
    public JournalTableComponent getJournalTableObject() {
        return journalTableObject;
    }

    // EFFECTS: returns the CategoryChartComponent object
    public CategoryChartComponent getCategoryChartComponent() {
        return categoryChartComponent;
    }

    // EFFECTS: returns the CreateCategoryPopup object
    public CreateCategoryPopup getCreateCategoryPopup() {
        return createCategoryPopup;
    }

    // EFFECTS: returns the EditCategoryPopup object
    public EditCategoryPopup getEditCategoryPopup() {
        return editCategoryPopup;
    }

    // EFFECTS: returns the SavePromptPopup object
    public SavePromptPopup getSavePromptPopup() {
        return savePromptPopup;
    }

    // EFFECTS: returns a copy of the JournalLog as TableView type
    public TableView<JournalEntry> getJournalTableView() {
        return journalTableView;
    }

    // EFFECTS: returns the UserSession object
    public UserSession getCurrentSession() {
        return currentSession;
    }

    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public Stage getMainStage() {
        return mainStage;
    }

    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void homePage(Pane sideBar, Button homePageButton) {
        homePageScreen.homePage(sideBar, homePageButton);
    }

    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void setJournalTableView(TableView<JournalEntry> journalTableView) {
        this.journalTableView = journalTableView;
    }

    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void removeListeners() {
        categoryListScreen.setCategoryCurrentSelected(null);
        journalLogScreen.setJournalEntryCurrentlySelected(null);
    }

    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void setMiddle(Stage s) {
        double middleCoordinateX = screenSize.getWidth() / 2;
        double middleCoordinateY = screenSize.getHeight() / 2;
        double subtractWindowSizeX = s.getWidth() / 2;
        double subtractWindowSizeY = s.getHeight() / 2;
        s.setX(middleCoordinateX - subtractWindowSizeX);
        s.setY(middleCoordinateY - subtractWindowSizeY);
    }

    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void clearButtonColours() {
        sideBarComponent.getNewJournalEntryButton().setStyle("-fx-background-color: #585858;");
        sideBarComponent.getHomePageButton().setStyle("-fx-background-color: #585858;");
        sideBarComponent.getViewJournalLogButton().setStyle("-fx-background-color: #585858;");
        sideBarComponent.getViewCategoryListButton().setStyle("-fx-background-color: #585858;");
    }

}

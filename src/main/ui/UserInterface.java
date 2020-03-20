package ui;

import javafx.application.Application;
import javafx.stage.Stage;
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
    private JournalTableComponent journalTableComponent;
    private CategoryChartComponent categoryChartComponent;
    private CreateCategoryPopup createCategoryPopup;
    private EditCategoryPopup editCategoryPopup;
    private SavePromptPopup savePromptPopup;
    private WelcomeScreen welcomeScreen;
    private Dimension screenSize;
    private UserSession currentSession;
    private Stage mainStage;

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final int TITLE_FONT_SIZE = 35;


    // MODIFIES: this
    // EFFECTS: initializes the program
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

    // MODIFIES: this
    // EFFECTS: sets mainStage dimensions to set width and height
    private void setMainStageDimensions() {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainStage.setWidth(WINDOW_WIDTH);
        mainStage.setHeight(WINDOW_HEIGHT);
        mainStage.setMinWidth(WINDOW_WIDTH);
        mainStage.setMinHeight(WINDOW_HEIGHT);
        setMiddle(mainStage);
    }

    // MODIFIES: this
    // EFFECTS: instantiates all screens at start of program
    private void initializeAllScreens() {
        // new session
        newUserWelcomeScreen = new NewUserWelcomeScreen(this);

        // completely new user
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
        journalTableComponent = new JournalTableComponent();
    }

    // MODIFIES: this
    // EFFECTS: removes listeners for category list and journal entry screens;
    // called when navigating away from those pages
    public void removeListeners() {
        categoryListScreen.setCategoryCurrentSelected(null);
        journalLogScreen.setJournalEntryCurrentlySelected(null);
    }

    // MODIFIES: object that calls this method
    // EFFECTS: sets stage to middle of screen depending on device's screen resolution
    public void setMiddle(Stage s) {
        double middleCoordinateX = screenSize.getWidth() / 2;
        double middleCoordinateY = screenSize.getHeight() / 2;
        double subtractWindowSizeX = s.getWidth() / 2;
        double subtractWindowSizeY = s.getHeight() / 2;
        s.setX(middleCoordinateX - subtractWindowSizeX);
        s.setY(middleCoordinateY - subtractWindowSizeY);
    }

    // MODIFIES: this
    // EFFECTS: sets all sidebar buttons to default colours
    public void clearButtonColours() {
        sideBarComponent.getNewJournalEntryButton().setStyle("-fx-background-color: #585858;");
        sideBarComponent.getHomePageButton().setStyle("-fx-background-color: #585858;");
        sideBarComponent.getViewJournalLogButton().setStyle("-fx-background-color: #585858;");
        sideBarComponent.getViewCategoryListButton().setStyle("-fx-background-color: #585858;");
    }

    // getters for fields in UserInterface
    public NewUserWelcomeScreen getNewUserWelcomeScreen() {
        return newUserWelcomeScreen;
    }

    public NewUserNameScreen getNewUserNameScreen() {
        return newUserNameScreen;
    }

    public NewUserAvatarScreen getNewUserAvatarScreen() {
        return newUserAvatarScreen;
    }

    public AvatarPickerComponent getAvatarPickerComponent() {
        return avatarPickerComponent;
    }

    public FirstNewCategoryScreen getFirstNewCategoryScreen() {
        return firstNewCategoryScreen;
    }

    public UserSelectScreen getUserSelectScreen() {
        return userSelectScreen;
    }

    public SideBarComponent getSideBarComponent() {
        return sideBarComponent;
    }

    public JournalEntryCreateScreen getJournalEntryCreateScreen() {
        return journalEntryCreateScreen;
    }

    public JournalEntryEditPopup getJournalEntryEditPopup() {
        return journalEntryEditPopup;
    }

    public JournalLogScreen getJournalLogScreen() {
        return journalLogScreen;
    }

    public HomePageScreen getHomePageScreen() {
        return homePageScreen;
    }

    public CategoryListScreen getCategoryListScreen() {
        return categoryListScreen;
    }

    public JournalTableComponent getJournalTableComponent() {
        return journalTableComponent;
    }

    public CategoryChartComponent getCategoryChartComponent() {
        return categoryChartComponent;
    }

    public CreateCategoryPopup getCreateCategoryPopup() {
        return createCategoryPopup;
    }

    public EditCategoryPopup getEditCategoryPopup() {
        return editCategoryPopup;
    }

    public SavePromptPopup getSavePromptPopup() {
        return savePromptPopup;
    }

    public UserSession getCurrentSession() {
        return currentSession;
    }

    public Stage getMainStage() {
        return mainStage;
    }

}

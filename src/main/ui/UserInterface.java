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

//TODO: find way to get CategoryList page to refresh after each action (create new and selection) (not working properly)
//TODO: create an avatar page for new users to choose

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
    private Button quitButton;
    private TimeJournalApp session;
    private Stage mainStage;

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final int TITLE_FONT_SIZE = 35;

    @Override
    public void start(Stage stage) {
        this.mainStage = stage;
        mainStage.setTitle("Time Journal");
        session = new TimeJournalApp();
        boolean noSaveFile = session.isFirstTime();
        setMainStageDimensions();
        initializeAllScreens();
        if (noSaveFile) {
            newUserWelcomeScreen.renderNewUserWelcomeScreen();
        } else {
            welcomeScreen.renderWelcomeScreen();
        }
    }

    private void setMainStageDimensions() {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainStage.setWidth(WINDOW_WIDTH);
        mainStage.setHeight(WINDOW_HEIGHT);
        mainStage.setMinWidth(WINDOW_WIDTH);
        mainStage.setMinHeight(WINDOW_HEIGHT);
        setMiddle(mainStage);
    }

    public void initializeAllScreens() {
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

    public void newUserWelcomeScreen() {
        newUserWelcomeScreen.renderNewUserWelcomeScreen();
    }

    public void newUserNameScreen() {
        newUserNameScreen.renderNewUserNameScreen();
    }

    public void userSelect() {
        userSelectScreen.renderUserSelectScreen();
    }

    public void homePage(Pane sideBar, Button homePageButton) {
        homePageScreen.homePage(sideBar, homePageButton);
    }

    public void createJournalEntry() {
        journalEntryCreateScreen.createJournalEntryScreen();
    }

    public void viewJournalEntries() {
        journalLogScreen.renderJournalLogScreen();
    }

    public JournalTableComponent getJournalTableObject() {
        return journalTableObject;
    }

    public void viewAllCategories() {
        categoryListScreen.renderCategoryListScreen();
    }

    public EditCategoryPopup getEditCategoryPopup() {
        return editCategoryPopup;
    }

    public void saveSession() {
        savePromptPopup.renderSavePopup();
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public TimeJournalApp getSession() {
        return session;
    }

    public Button getQuitButton() {
        return quitButton;
    }

    public void setQuitButton(Button quitButton) {
        this.quitButton = quitButton;
    }

    public JournalEntryCreateScreen getJournalEntryCreateScreen() {
        return journalEntryCreateScreen;
    }

    public TableView<JournalEntry> getJournalTableView() {
        return journalTableView;
    }

    public void setJournalTableView(TableView<JournalEntry> journalTableView) {
        this.journalTableView = journalTableView;
    }

    public String getCategoryCurrentlySelected() {
        return categoryListScreen.getCategoryCurrentSelected();
    }

    public Button getJournalEntryButton() {
        return sideBarComponent.getNewJournalEntryButton();
    }

    public JournalLogScreen getJournalLogScreen() {
        return journalLogScreen;
    }

    public CategoryListScreen getCategoryListScreen() {
        return categoryListScreen;
    }

    public SideBarComponent getSideBarComponent() {
        return sideBarComponent;
    }

    public CategoryChartComponent getCategoryChartComponent() {
        return categoryChartComponent;
    }

    public CreateCategoryPopup getCreateCategoryPopup() {
        return createCategoryPopup;
    }

    public JournalEntryEditPopup getJournalEntryEditPopup() {
        return journalEntryEditPopup;
    }

    public AvatarPickerComponent getAvatarPickerComponent() {
        return avatarPickerComponent;
    }

    public FirstNewCategoryScreen getFirstNewCategoryScreen() {
        return firstNewCategoryScreen;
    }

    public NewUserAvatarScreen getNewUserAvatarScreen() {
        return newUserAvatarScreen;
    }

    public NewUserNameScreen getNewUserNameScreen() {
        return newUserNameScreen;
    }

    public void removeListeners() {
        categoryListScreen.setCategoryCurrentSelected(null);
        journalLogScreen.setJournalEntryCurrentlySelected(null);
    }

    public void setMiddle(Stage s) {
        double middleCoordinateX = screenSize.getWidth() / 2;
        double middleCoordinateY = screenSize.getHeight() / 2;
        double subtractWindowSizeX = s.getWidth() / 2;
        double subtractWindowSizeY = s.getHeight() / 2;
        s.setX(middleCoordinateX - subtractWindowSizeX);
        s.setY(middleCoordinateY - subtractWindowSizeY);
    }

    public void clearButtonColours() {
        sideBarComponent.getNewJournalEntryButton().setStyle("-fx-background-color: #585858;");
        sideBarComponent.getHomePageButton().setStyle("-fx-background-color: #585858;");
        sideBarComponent.getViewJournalLogButton().setStyle("-fx-background-color: #585858;");
        sideBarComponent.getViewCategoryListButton().setStyle("-fx-background-color: #585858;");
    }

}

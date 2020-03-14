package ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Category;
import model.JournalEntry;
import ui.screens.*;
import ui.components.CategoryChart;
import ui.components.JournalTable;
import ui.components.SideBar;

import java.awt.*;

//TODO: edit/delete categories
//TODO: edit/delete journal entries
//TODO: add time spent to category list in journal entry
//TODO: find way to get CategoryList page to refresh after each action (create new and selection)
//TODO: think about using an abstract class for all scenes...
//TODO: Add a category (duration) pie chart to the home screen

public class UserInterface extends Application {
    private final NewUserWelcomeScreen newUserWelcomeScreen = new NewUserWelcomeScreen(this);
    private final NewUserNameScreen newUserNameScreen = new NewUserNameScreen(this);
    private final FirstNewCategoryScreen firstNewCategoryScreen = new FirstNewCategoryScreen(this);
    private final UserSelectScreen userSelectScreen = new UserSelectScreen(this);
    private final SideBar sideBar = new SideBar(this);
    private final JournalEntryCreateScreen journalEntryCreateScreen = new JournalEntryCreateScreen(this);
    private final JournalLogScreen journalLogScreen = new JournalLogScreen(this);
    private final HomePageScreen homePageScreen = new HomePageScreen(this);
    private final CategoryListScreen categoryListScreen = new CategoryListScreen(this);
    private final JournalTable journalTable = new JournalTable();
    private final CategoryChart categoryChart = new CategoryChart();
    private final CreateCategoryPopup createCategoryPopup = new CreateCategoryPopup(this);
    private final EditCategoryPopup editCategoryPopup = new EditCategoryPopup(this);
    private final SaveScreen saveScreen = new SaveScreen(this);
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private TableView<JournalEntry> journalTableView;
    private Button quitButton;
    private TimeJournalApp session;
    private Stage mainStage;
    private String categoryCurrentlySelected;

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final int TITLE_FONT_SIZE = 35;

    @Override
    public void start(Stage stage) throws Exception {
        this.mainStage = stage;
        mainStage.setTitle("Time Journal");
        session = new TimeJournalApp();
        boolean noSaveFile = session.isFirstTime();

        mainStage.setWidth(WINDOW_WIDTH);
        mainStage.setHeight(WINDOW_HEIGHT);
        mainStage.setMinWidth(WINDOW_WIDTH);
        mainStage.setMinHeight(WINDOW_HEIGHT);
        setMiddle(mainStage);

        if (noSaveFile) {
            newUserWelcomeScreen.newUserWelcomeScreen();
        } else {
            homePageScreen.selectUserScreen.selectUser();
        }
    }

    public void initializeScene(Pane newPane, Stage stage) {
        Scene scene = new Scene(newPane);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
    }

    public void newUserWelcomeScreen() {
        newUserWelcomeScreen.newUserWelcomeScreen();
    }

    public void newUserNameScreen() {
        newUserNameScreen.renderNewUserNameScreen();
    }

    public void firstNewCategory() {
        firstNewCategoryScreen.firstNewCategory();
    }

    public void userSelect() {
        userSelectScreen.userSelect();
    }

    public void makeSideBar() {
        sideBar.makeSideBar();
    }

    public void homePage(Pane sideBar, Button homePageButton) {
        homePageScreen.homePage(sideBar, homePageButton);
    }

    public PieChart generateCategoryChart() {
        return categoryChart.generateCategoryChart();
    }

    public void createJournalEntry(Pane sideBar, Button newJournalEntryMenuButton) {
        journalEntryCreateScreen.createJournalEntryScreen(sideBar, newJournalEntryMenuButton);
    }

    public void viewJournalEntries() {
        journalLogScreen.renderJournalLogScreen();
    }

    public TableColumn<JournalEntry, Integer> journalTableIdColumn() {
        return journalTable.getIdColumn();
    }

    public TableColumn<JournalEntry, String> journalTableDescriptionColumn() {
        return journalTable.getDescriptionColumn();
    }

    public TableColumn<JournalEntry, Integer> journalTableDurationColumn() {
        return journalTable.getDurationColumn();
    }

    public TableColumn<JournalEntry, String> journalTableCategoryColumn() {
        return journalTable.getCategoryColumn();
    }

    public TableColumn<JournalEntry, String> journalTableDateColumn() {
        return journalTable.getDateColumn();
    }

    public void viewAllCategories() {
        categoryListScreen.renderJournalLogScreen();
    }

    public ObservableList<Category> generateCategoryList() {
        return categoryListScreen.generateCategoryList();
    }

    public void editCategory(Button submit, TextField categoryName) {
        editCategoryPopup.editCategory(submit, categoryName);
    }

    public void createNewCategory(TextField categoryName, Stage createCategoryStage) {
        createCategoryPopup.createNewCategory(categoryName, createCategoryStage);
    }

    public void editCategoryScreen() {
        editCategoryPopup.initializeScreen();
    }

    public void saveSession() {
        saveScreen.savePrompt();
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
        return categoryCurrentlySelected;
    }

    public void setCategoryCurrentlySelected(String categoryCurrentlySelected) {
        this.categoryCurrentlySelected = categoryCurrentlySelected;
    }

    public Button getJournalEntryButton() {
        return sideBar.getNewJournalEntryButton();
    }

    public JournalLogScreen getJournalLogScreen() {
        return journalLogScreen;
    }

    public CategoryListScreen getCategoryListScreen() {
        return categoryListScreen;
    }

    public SideBar getSideBar() {
        return sideBar;
    }

    public CreateCategoryPopup getCreateCategoryPopup() {
        return createCategoryPopup;
    }

    public JournalTable getJournalTable() {
        return journalTable;
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
        sideBar.getNewJournalEntryButton().setStyle("-fx-background-color: #585858;");
        sideBar.getHomePageButton().setStyle("-fx-background-color: #585858;");
        sideBar.getViewJournalLogButton().setStyle("-fx-background-color: #585858;");
        sideBar.getViewCategoryListButton().setStyle("-fx-background-color: #585858;");
    }

}

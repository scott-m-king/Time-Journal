package ui;

import exceptions.CategoryExistsException;
import exceptions.NullEntryException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.stage.StageStyle;
import model.Category;
import model.CategoryList;
import model.JournalEntry;
import model.JournalLog;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//TODO: edit/delete categories
//TODO: edit/delete journal entries
//TODO: add time spent to category list in journal entry
//TODO: find way to get CategoryList page to refresh after each action (create new and selection)
//TODO: think about using an abstract class for all scenes...
//TODO: Add a category (duration) pie chart to the home screen

public class GUI extends Application {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private ComboBox<String> comboBox;
    private TableView<JournalEntry> journalTable;
    private TableView<JournalEntry> categoryJournalTable;
    private ListView<String> categoryDurationList;
    private Button newJournalEntry;
    private Button homePage;
    private Button viewJournalLog;
    private Button viewCategoryList;
    private Button quit;
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
            initNoExistingSaveFile();
        } else {
            initSelectUser();
        }
    }

    public void initSelectUser() {
        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);

        Label title = new Label("Time Journal");
        title.setStyle("-fx-font-size: 70px; -fx-text-fill: #383838;");
        title.setPadding(new Insets(0, 0, 85, 0));

        Button newUserButton = new Button("New User");
        newUserButton.setMinWidth(100);
        GridPane.setConstraints(newUserButton, 0, 0);

        Button returningUserButton = new Button("Returning User");
        returningUserButton.setMinWidth(100);
        GridPane.setConstraints(returningUserButton, 0, 1);

        newUserButton.setOnAction(e -> initNoExistingSaveFile());
        returningUserButton.setOnAction(e -> userSelect());

        grid.setAlignment(Pos.CENTER);
        grid.getChildren().addAll(newUserButton, returningUserButton);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(title, grid);

        initializeScene(vbox, mainStage);
        mainStage.show();
    }

    public void initNoExistingSaveFile() {
        Label title = new Label("Welcome to Time Journal");
        title.setStyle("-fx-font-size: 60px; -fx-text-fill: #383838;");
        title.setPadding(new Insets(0, 0, 75, 0));

        Button newUserButton = new Button(">");
        newUserButton.setStyle("-fx-min-width: 75;");

        newUserButton.setOnAction(e -> completelyNewUser());

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(title, newUserButton);

        initializeScene(vbox, mainStage);
    }

    private void initializeScene(Pane newPane, Stage mainStage) {
        Scene scene = new Scene(newPane);
        scene.getStylesheets().add("ui/style.css");
        mainStage.setScene(scene);
    }

    public void completelyNewUser() {
        Label nameLabel = new Label("What's your name? Enter below: ");
        nameLabel.setStyle("-fx-font-size: 45px; -fx-text-fill: #383838;");
        nameLabel.setAlignment(Pos.CENTER);

        TextField name = new TextField();
        name.setMaxWidth(300);
        name.setStyle("-fx-font-size: 20px;");
        name.setAlignment(Pos.CENTER);

        Button newUserButton = new Button(">");
        newUserButton.setStyle("-fx-min-width: 75;");
        newUserButton.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(nameLabel, name, newUserButton);

        setUserNameListener(newUserButton, name);

        initializeScene(vbox, mainStage);
    }

    public void setUserNameListener(Button newUserButton, TextField name) {
        newUserButton.setOnAction(e -> {
            try {
                session.setCurrentUser(name.getText());
                session.newSession();
                firstNewCategory();
            } catch (NullEntryException exception) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("You must enter at least one character for your name.");
                a.show();
            }
        });
    }

    public void firstNewCategory() {
        Label nameLabel = new Label("Let's start with creating your first category.\n"
                + "Enter a name for your category below:");
        nameLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: #383838;");
        nameLabel.setTextAlignment(TextAlignment.CENTER);

        TextField categoryName = new TextField();
        categoryName.setMaxWidth(300);
        categoryName.setStyle("-fx-font-size: 20px;");
        categoryName.setAlignment(Pos.CENTER);

        Button startJournal = new Button("Get Started");
        startJournal.setAlignment(Pos.CENTER);

        setButtonHandlerForNewUser(startJournal, categoryName);

        VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(nameLabel, categoryName, startJournal);

        initializeScene(vbox, mainStage);
    }

    public void setButtonHandlerForNewUser(Button startJournal, TextField categoryName) {
        startJournal.setOnAction(e -> {
            try {
                session.createNewCategory(categoryName.getText());
                renderSideBar();
            } catch (NullEntryException e1) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("You must enter a name for your category.");
                a.show();
            } catch (CategoryExistsException exception) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("You happened to enter the one category that already exists... try again.");
                a.show();
            }
        });
    }

    public void userSelect() {
        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(50);
        root.setHgap(10);

        Label title = new Label("Which user are you?");
        title.setStyle("-fx-font-size: 40px; -fx-text-fill: #383838;");
        GridPane.setConstraints(title, 0, 0);

        comboBox = new ComboBox<>();
        comboBox.setPromptText("Select one...");
        comboBox.setMinWidth(200);
        comboBox.setStyle("-fx-font-size: 15px;");

        ArrayList<String> list = session.getUserList();

        for (String s : list) {
            comboBox.getItems().add(s);
        }

        GridPane.setConstraints(comboBox, 0, 1);
        GridPane.setHalignment(comboBox, HPos.CENTER);

        Button submit = new Button("Submit");
        submit.setMinWidth(100);
        GridPane.setConstraints(submit, 0, 2);
        GridPane.setHalignment(submit, HPos.CENTER);

        root.getChildren().addAll(title, comboBox, submit);
        root.setAlignment(Pos.CENTER);

        submit.setOnAction(e -> {
            try {
                session.selectUser(comboBox.getSelectionModel().getSelectedItem());
                renderSideBar();
            } catch (NullEntryException exception) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("Please select a user.");
                a.show();
            }
        });

        initializeScene(root, mainStage);
    }

    public void renderSideBar() {
        AnchorPane pane = new AnchorPane();

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        Pane sideBar = new Pane();
        sideBar.setPrefWidth(200);
        sideBar.setStyle("-fx-background-color:#383838");
        AnchorPane.setTopAnchor(sideBar, 0.0);
        AnchorPane.setBottomAnchor(sideBar, 0.0);
        AnchorPane.setLeftAnchor(sideBar, 0.0);

        GridPane menuItems = new GridPane();
        menuItems.setPadding(new Insets(35, 0, 0, 10));
        menuItems.setVgap(15);

        Label userName = new Label(session.getUserWelcome());
        userName.setTextAlignment(TextAlignment.CENTER);
        userName.setWrapText(true);
        GridPane.setConstraints(userName, 0, 0);
        userName.setPadding(new Insets(0, 0, 15, 0));
        GridPane.setHalignment(userName, HPos.CENTER);

        setSideBarButtons();

        menuItems.getChildren().addAll(userName, newJournalEntry, homePage, viewJournalLog, viewCategoryList);
        sideBar.getChildren().add(menuItems);
        pane.getChildren().addAll(sideBar, quit);

        menuButtonListeners(newJournalEntry, homePage, viewJournalLog, viewCategoryList, sideBar);

        homePage(sideBar, homePage);
    }

    private void setSideBarButtons() {
        homePage = new Button("Home");
        GridPane.setConstraints(homePage, 0, 1);

        newJournalEntry = new Button("Create Journal Entry");
        GridPane.setConstraints(newJournalEntry, 0, 2);

        viewJournalLog = new Button("Journal Entry Log");
        GridPane.setConstraints(viewJournalLog, 0, 3);

        viewCategoryList = new Button("Category List");
        GridPane.setConstraints(viewCategoryList, 0, 4);

        quit = new Button("Exit");
        AnchorPane.setBottomAnchor(quit, 14.0);
        AnchorPane.setLeftAnchor(quit, 10.0);
    }

    public void menuButtonListeners(Button newJournalEntry, Button homePage, Button viewJournalLog,
                                    Button viewCategoryList, Pane sideBar) {

        newJournalEntry.setOnAction(e -> {
            clearButtonColours(newJournalEntry, homePage, viewJournalLog, viewCategoryList);
            createJournalEntry(sideBar, newJournalEntry);
        });

        homePage.setOnAction(e -> {
            clearButtonColours(newJournalEntry, homePage, viewJournalLog, viewCategoryList);
            homePage(sideBar, homePage);
        });

        viewJournalLog.setOnAction(e -> {
            clearButtonColours(newJournalEntry, homePage, viewJournalLog, viewCategoryList);
            viewJournalEntries(sideBar, viewJournalLog);
        });

        viewCategoryList.setOnAction(e -> {
            clearButtonColours(newJournalEntry, homePage, viewJournalLog, viewCategoryList);
            viewAllCategories(sideBar, viewCategoryList);
        });

        mainStage.setOnCloseRequest(e -> {
            e.consume();
            savePrompt();
        });

        quit.setOnAction(e -> savePrompt());
    }

    // https://docs.oracle.com/javafx/2/charts/pie-chart.htm
    public void homePage(Pane sideBar, Button homePageButton) {
        AnchorPane pane = new AnchorPane();

        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Home");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        PieChart chart = generateCategoryChart();

        pane.getChildren().addAll(sideBar, quit, title, chart);

        homePageButton.setStyle("-fx-background-color:#787878");

        initializeScene(pane, mainStage);
    }

    private PieChart generateCategoryChart() {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Grapefruit", 13),
                        new PieChart.Data("Oranges", 25),
                        new PieChart.Data("Plums", 10),
                        new PieChart.Data("Pears", 22),
                        new PieChart.Data("Apples", 30));
        PieChart chart = new PieChart(pieChartData);
        chart.setLegendSide(Side.RIGHT);
        AnchorPane.setLeftAnchor(chart, 230.0);
        AnchorPane.setRightAnchor(chart, 30.0);
        AnchorPane.setTopAnchor(chart, 95.0);
        AnchorPane.setBottomAnchor(chart, 30.0);
        return chart;
    }

    public void createJournalEntry(Pane sideBar, Button newJournalEntryMenuButton) {
        Text title = createJournalEntrySetTitle();
        AnchorPane pane = createJournalEntryPageLayout(sideBar, title);
        newJournalEntryMenuButton.setStyle("-fx-background-color:#787878");

        initializeScene(pane, mainStage);
    }

    private Text createJournalEntrySetTitle() {
        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Create New Journal Entry");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);
        return title;
    }

    private AnchorPane createJournalEntryPageLayout(Pane sideBar, Text title) {
        AnchorPane pane = new AnchorPane();

        Text descriptionLabel = createJournalSetLabel();
        TextField descriptionField = createJournalSetDescriptionField();
        Text durationLabel = createJournalSetDurationLabel();
        TextField durationField = createJournalSetDurationField();
        Text categoryLabel = createJournalSetCategoryLabel();
        ComboBox<String> categoryList = createJournalGenerateCategoryList();

        Button submit = new Button("Submit");
        AnchorPane.setBottomAnchor(submit, 30.0);
        AnchorPane.setRightAnchor(submit, 30.0);

        pane.getChildren().addAll(sideBar, quit, title, durationLabel, descriptionLabel, categoryLabel,
                descriptionField, categoryList, durationField, submit);

        setJournalEntrySubmitListener(submit, descriptionField, durationField, categoryList, pane);
        return pane;
    }

    private ComboBox<String> createJournalGenerateCategoryList() {
        ComboBox<String> categoryList = new ComboBox<>();
        ObservableList<Category> listToAdd = generateCategoryList();

        for (Category c : listToAdd) {
            categoryList.getItems().add(c.getName());
        }

        categoryList.setValue("Uncategorized");
        AnchorPane.setTopAnchor(categoryList, 295.0);
        AnchorPane.setLeftAnchor(categoryList, 230.0);
        AnchorPane.setRightAnchor(categoryList, 30.0);
        return categoryList;
    }

    private Text createJournalSetCategoryLabel() {
        Text categoryLabel = new Text("What category would you like to assign this entry to?");
        categoryLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(categoryLabel, 265.0);
        AnchorPane.setLeftAnchor(categoryLabel, 230.0);
        AnchorPane.setRightAnchor(categoryLabel, 30.0);
        return categoryLabel;
    }

    private TextField createJournalSetDurationField() {
        TextField durationField = new TextField();
        AnchorPane.setTopAnchor(durationField, 215.0);
        AnchorPane.setLeftAnchor(durationField, 230.0);
        AnchorPane.setRightAnchor(durationField, 30.0);
        return durationField;
    }

    private Text createJournalSetDurationLabel() {
        Text durationLabel = new Text("How long did you spend on this? (in minutes)");
        durationLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(durationLabel, 185.0);
        AnchorPane.setLeftAnchor(durationLabel, 230.0);
        AnchorPane.setRightAnchor(durationLabel, 30.0);
        return durationLabel;
    }

    private TextField createJournalSetDescriptionField() {
        TextField descriptionField = new TextField();
        AnchorPane.setTopAnchor(descriptionField, 125.0);
        AnchorPane.setLeftAnchor(descriptionField, 230.0);
        AnchorPane.setRightAnchor(descriptionField, 30.0);
        return descriptionField;
    }

    private Text createJournalSetLabel() {
        Text descriptionLabel = new Text("What did you get up to? Enter a description for your journal entry:");
        descriptionLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(descriptionLabel, 95.0);
        AnchorPane.setLeftAnchor(descriptionLabel, 230.0);
        AnchorPane.setRightAnchor(descriptionLabel, 30.0);
        return descriptionLabel;
    }

    public void setJournalEntrySubmitListener(
            Button submit, TextField descriptionField, TextField durationField, ComboBox<String> categoryList,
            AnchorPane pane) {
        submit.setOnAction(e -> {
            doCategoryEntry(descriptionField, durationField, categoryList);
        });

        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                doCategoryEntry(descriptionField, durationField, categoryList);
            }
        });
    }

    public void doCategoryEntry(TextField descriptionField, TextField durationField, ComboBox<String> categoryList) {
        try {
            String description = descriptionField.getText();
            String duration = durationField.getText();
            String category = categoryList.getSelectionModel().getSelectedItem();
            session.createNewJournalEntry(description, duration, category);
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("Entry successfully added!");
            a.show();
            descriptionField.clear();
            durationField.clear();
            categoryList.setValue("Uncategorized");
        } catch (NullEntryException e1) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Please make sure to fill in all fields.");
            a.show();
        } catch (NumberFormatException e2) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("You didn't enter a number for the duration. Please try again.");
            a.show();
        }
    }

    public void viewJournalEntries(Pane sideBar, Button journalLogMenuButton) {
        AnchorPane pane = new AnchorPane();

        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Journal Entry Log");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        journalLogMenuButton.setStyle("-fx-background-color:#787878");

        setJournalTableColumns();

        HBox buttons = setJournalLogButtons(journalLogMenuButton, sideBar);
        buttons.setSpacing(15.0);
        AnchorPane.setRightAnchor(buttons, 30.0);
        AnchorPane.setTopAnchor(buttons, 30.0);

        pane.getChildren().addAll(sideBar, quit, title, journalTable, buttons);

        initializeScene(pane, mainStage);
    }

    public HBox setJournalLogButtons(Button journalLogMenuButton, Pane sideBar) {
        HBox buttons = new HBox();

        Button createNew = new Button("Create");
        createNew.setStyle("-fx-min-width: 100;");
        Button delete = new Button("Delete");

        delete.setStyle("-fx-min-width: 100;");
        Button edit = new Button("Edit");

        edit.setStyle("-fx-min-width: 100;");
        buttons.getChildren().addAll(edit, delete, createNew);

        setJournalLogButtonListeners(journalLogMenuButton, sideBar, createNew, edit);
        return buttons;
    }

    private void setJournalLogButtonListeners(
            Button journalLogMenuButton, Pane sideBar, Button createNew, Button edit) {
        createNew.setOnAction(e -> {
            clearButtonColours(newJournalEntry, homePage, viewJournalLog, journalLogMenuButton);
            createJournalEntry(sideBar, newJournalEntry);
        });

        edit.setOnAction(e -> {
            ObservableList<JournalEntry> entrySelected = journalTable.getSelectionModel().getSelectedItems();
            System.out.println(entrySelected.get(0).getDescription());
        });
    }

    public ObservableList<JournalEntry> getEntries() {
        ObservableList<JournalEntry> entries = FXCollections.observableArrayList();
        JournalLog journalLog = session.getJournalLog();
        ArrayList<JournalEntry> entryList = journalLog.getEntriesAsList();
        entries.addAll(entryList);
        return entries;
    }

    public void setJournalTableColumns() {
        // Journal ID Column
        TableColumn<JournalEntry, Integer> idColumn = new TableColumn<>("Journal ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("journalID"));

        // Date Column
        TableColumn<JournalEntry, String> dateTableColumn = new TableColumn<>("Date");
        dateTableColumn.setMinWidth(100);
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Category Column
        TableColumn<JournalEntry, String> categoryTableColumn = new TableColumn<>("Category");
        categoryTableColumn.setMinWidth(100);
        categoryTableColumn.setCellValueFactory(cellData -> Bindings.selectString(
                cellData.getValue().getCategory(), "name"));

        // Duration Column
        TableColumn<JournalEntry, Integer> durationTableColumn = new TableColumn<>("Duration");
        durationTableColumn.setMinWidth(100);
        durationTableColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        // Description Column
        TableColumn<JournalEntry, String> descriptionTableColumn = new TableColumn<>("Description");
        descriptionTableColumn.setMinWidth(315);
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        renderJournalTable(idColumn, dateTableColumn, categoryTableColumn, durationTableColumn, descriptionTableColumn);
    }

    public void renderJournalTable(
            TableColumn<JournalEntry, Integer> idColumn,
            TableColumn<JournalEntry, String> dateTableColumn,
            TableColumn<JournalEntry, String> categoryTableColumn,
            TableColumn<JournalEntry, Integer> durationTableColumn,
            TableColumn<JournalEntry, String> descriptionTableColumn) {

        journalTable = new TableView<>();
        journalTable.setItems(getEntries());
        journalTable.getColumns().addAll(idColumn, dateTableColumn,
                categoryTableColumn, durationTableColumn, descriptionTableColumn);
        journalTable.setPlaceholder(new Text("No journal entries yet!"));
        AnchorPane.setTopAnchor(journalTable, 95.0);
        AnchorPane.setBottomAnchor(journalTable, 30.0);
        AnchorPane.setLeftAnchor(journalTable, 230.0);
        AnchorPane.setRightAnchor(journalTable, 30.0);
    }

    public void viewAllCategories(Pane sideBar, Button categoriesMenuButton) {
        AnchorPane pane = new AnchorPane();
        categoryCurrentlySelected = null;

        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Category List");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        ObservableList<Category> observableList = generateCategoryDurationListView();
        HBox buttons = setCategoryLogButtons();
        buttons.setSpacing(15.0);
        AnchorPane.setRightAnchor(buttons, 30.0);
        AnchorPane.setTopAnchor(buttons, 30.0);

        // string to filter with
        final String[] toFilter = new String[1];
        categoryTableListener(
                sideBar, title, toFilter, observableList, pane, buttons);

        pane.getChildren().addAll(sideBar, quit, title, categoryDurationList, categoryJournalTable, buttons);

        categoriesMenuButton.setStyle("-fx-background-color:#787878");

        initializeScene(pane, mainStage);
    }

    private ObservableList<Category> generateCategoryDurationListView() {
        categoryDurationList = new ListView<>();
        ObservableList<Category> observableList = generateCategoryList();

        for (Category category : observableList) {
            categoryDurationList.getItems().add(category.getDurationString());
        }

        categoryDurationList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        categoryDurationList.setMaxHeight(275);
        AnchorPane.setLeftAnchor(categoryDurationList, 230.0);
        AnchorPane.setTopAnchor(categoryDurationList, 95.0);
        AnchorPane.setRightAnchor(categoryDurationList, 30.0);
        categoryJournalTable = makeCategoryJournalEntryTable(getEntries());

        return observableList;
    }

    public void categoryTableListener(Pane sideBar, Text title,
                                      String[] toFilter, ObservableList<Category> observableList,
                                      AnchorPane pane, HBox buttons) {
        categoryDurationList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        int index = categoryDurationList.getSelectionModel().getSelectedIndex();
                        categoryCurrentlySelected = session.getCategoryList().get(index).getName();
                        toFilter[0] = observableList.get(index).getName();
                        filterList(toFilter[0]);
                        pane.getChildren().clear();
                        pane.getChildren().addAll(sideBar, quit, title, categoryDurationList,
                                categoryJournalTable, buttons);
                    } catch (IndexOutOfBoundsException exception) {
                        // no action
                    }
                }
        );
    }

    public void filterList(String filterCondition) {
        ObservableList<JournalEntry> entriesToFilter = getEntries();
        List<JournalEntry> result = entriesToFilter.stream()
                .filter(journalEntry -> filterCondition.equals(journalEntry.getCategory().getName()))
                .collect(Collectors.toList());
        if (result.size() == 0) {
            categoryJournalTable = makeCategoryJournalEntryTable(result);
            categoryJournalTable.setPlaceholder(new Text("No entries for " + filterCondition));
        } else {
            categoryJournalTable = makeCategoryJournalEntryTable(result);
        }
    }

    public ObservableList<Category> generateCategoryList() {
        ObservableList<Category> observableCategoryList = FXCollections.observableArrayList();
        CategoryList categoryList = session.getCategoryList();
        for (int i = 0; i < categoryList.getSize(); i++) {
            observableCategoryList.add(categoryList.get(i));
        }
        return observableCategoryList;
    }

    public TableView<JournalEntry> makeCategoryJournalEntryTable(List<JournalEntry> entries) {
        categoryJournalTable = new TableView<>();
        ObservableList<JournalEntry> observableList = FXCollections.observableArrayList();
        observableList.addAll(entries);

        // Date Column
        TableColumn<JournalEntry, String> dateTableColumn = new TableColumn<>("Date");
        dateTableColumn.setMinWidth(100);
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Category Column
        TableColumn<JournalEntry, String> categoryTableColumn = new TableColumn<>("Category");
        categoryTableColumn.setMinWidth(100);
        categoryTableColumn.setCellValueFactory(cellData -> Bindings.selectString(
                cellData.getValue().getCategory(), "name"));

        // Duration Column
        TableColumn<JournalEntry, Integer> durationTableColumn = new TableColumn<>("Duration");
        durationTableColumn.setMinWidth(100);
        durationTableColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        // Description Column
        TableColumn<JournalEntry, String> descriptionTableColumn = new TableColumn<>("Description");
        descriptionTableColumn.setMinWidth(315);
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        return renderCategoryJournalEntryTable(
                observableList,
                dateTableColumn,
                categoryTableColumn,
                durationTableColumn,
                descriptionTableColumn);
    }

    private TableView<JournalEntry> renderCategoryJournalEntryTable(
            ObservableList<JournalEntry> observableList,
            TableColumn<JournalEntry, String> dateTableColumn,
            TableColumn<JournalEntry, String> categoryTableColumn,
            TableColumn<JournalEntry, Integer> durationTableColumn,
            TableColumn<JournalEntry, String> descriptionTableColumn) {
        categoryJournalTable.setItems(observableList);
        categoryJournalTable.getColumns().addAll(
                dateTableColumn, categoryTableColumn, durationTableColumn, descriptionTableColumn);
        AnchorPane.setTopAnchor(categoryJournalTable, 385.0);
        AnchorPane.setBottomAnchor(categoryJournalTable, 30.0);
        AnchorPane.setRightAnchor(categoryJournalTable, 30.0);
        AnchorPane.setLeftAnchor(categoryJournalTable, 230.0);
        return categoryJournalTable;
    }

    public HBox setCategoryLogButtons() {
        HBox buttons = new HBox();

        Button createNew = new Button("Create");
        createNew.setStyle("-fx-min-width: 100;");

        Button delete = new Button("Delete");
        delete.setStyle("-fx-min-width: 100;");

        Button edit = new Button("Edit");
        edit.setStyle("-fx-min-width: 100;");

        buttons.getChildren().addAll(edit, delete, createNew);
        setCategoryButtonListeners(createNew, delete, edit);
        return buttons;
    }

    public void setCategoryButtonListeners(Button createNew, Button delete, Button edit) {
        createNew.setOnAction(e -> createCategoryScreen());

        delete.setOnAction(e -> {
            if (categoryCurrentlySelected == null) {
                return;
            }
            if (!categoryCurrentlySelected.equals("Uncategorized")) {
                confirmCategoryDelete();
            } else {
                invalidCategoryDeleteAlert();
            }
        });

        edit.setOnAction(e -> {
            if (categoryCurrentlySelected == null) {
                return;
            }
            if (!categoryCurrentlySelected.equals("Uncategorized")) {
                editCategoryScreen();
            } else {
                invalidCategoryEditAlert();
            }
        });
    }

    private void invalidCategoryEditAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Cannot modify the Uncategorized category.");
        a.show();
    }

    private void invalidCategoryDeleteAlert() {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText("Sorry, you cannot delete the Uncategorized category.");
        a.show();
    }

    private void confirmCategoryDelete() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Are you sure you want to delete this category? This cannot be undone.");
        Optional<ButtonType> result = a.showAndWait();
        if (!result.isPresent() || result.get() == ButtonType.CANCEL) {
            a.close();
        } else if (result.get() == ButtonType.OK) {
            deleteCategory();
        }
    }

    public void createCategoryScreen() {
        Stage createCategoryStage = new Stage();
        createCategoryStage.setWidth(WINDOW_WIDTH - 600);
        createCategoryStage.setHeight(WINDOW_HEIGHT - 450);
        createCategoryStage.initStyle(StageStyle.UNDECORATED);
        createCategoryStage.initModality(Modality.APPLICATION_MODAL);

        VBox vbox = new VBox();
        vbox.setSpacing(20.0);

        Text text = new Text("Enter a name for your your category:");
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle("-fx-font-size:16px;");

        TextField categoryName = new TextField();
        categoryName.setAlignment(Pos.CENTER);
        categoryName.setMaxWidth(300);
        categoryName.setStyle("-fx-font-size:14px;");

        HBox hbox = makeCategoryButtons(categoryName, createCategoryStage, "createCategoryScreen");

        vbox.getChildren().addAll(text, categoryName, hbox);
        vbox.setAlignment(Pos.CENTER);

        initializeScene(vbox, createCategoryStage);
        setMiddle(createCategoryStage);

        createCategoryStage.show();
    }

    public HBox makeCategoryButtons(TextField categoryName, Stage stage, String cameFrom) {
        HBox hbox = new HBox();
        hbox.setSpacing(10.0);

        Button submit = new Button("Submit");
        submit.setStyle("-fx-min-width: 100; -fx-min-height:35;");

        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-min-width: 100; -fx-min-height:35;");

        hbox.getChildren().addAll(submit, cancel);
        hbox.setAlignment(Pos.CENTER);

        cancel.setOnAction(e -> stage.close());
        categoryButtonListeners(categoryName, stage, cameFrom, submit);

        return hbox;
    }

    private void categoryButtonListeners(TextField categoryName, Stage stage, String cameFrom, Button submit) {
        if (cameFrom.equals("createCategoryScreen")) {
            submit.setOnAction(e -> createNewCategory(categoryName, stage));
        } else {
            editCategory(submit, categoryName, stage);
        }
    }

    public void editCategory(Button submit, TextField categoryName, Stage stage) {
        submit.setOnAction(e -> {
            try {
                session.editCategory(categoryCurrentlySelected, categoryName.getText());
                stage.close();
            } catch (CategoryExistsException exception1) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("That category name already exists.");
                a.show();
            }
        });
    }

    public void createNewCategory(TextField categoryName, Stage createCategoryStage) {
        try {
            session.createNewCategory(categoryName.getText());
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("You've successfully added the category.");
            a.show();
            createCategoryStage.close();
        } catch (NullEntryException e1) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("You must enter a name for your category.");
            a.show();
        } catch (CategoryExistsException exception) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Sorry, that category already exists. Please try again.");
            a.show();
        }
    }

    public void editCategoryScreen() {
        Stage editCategoryScreen = new Stage();
        editCategoryScreen.setWidth(WINDOW_WIDTH - 600);
        editCategoryScreen.setHeight(WINDOW_HEIGHT - 450);
        editCategoryScreen.initStyle(StageStyle.UNDECORATED);
        editCategoryScreen.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Editing " + categoryCurrentlySelected + " category");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-text-fill:#383838; -fx-font-size:25px;");
        Text text = new Text("What would you like to change the name of the category to?");
        text.setStyle("-fx-font-size:15px;");

        TextField categoryName = new TextField();
        categoryName.setMaxWidth(300);

        HBox buttons = makeCategoryButtons(categoryName, editCategoryScreen, "editCategoryScreen");

        VBox screen = new VBox();
        screen.getChildren().addAll(label, text, categoryName, buttons);
        screen.setAlignment(Pos.CENTER);
        screen.setSpacing(15.0);

        initializeScene(screen, editCategoryScreen);
        setMiddle(editCategoryScreen);
        editCategoryScreen.show();
    }

    public void deleteCategory() {
        if (categoryCurrentlySelected != null) {
            session.deleteCategory(categoryCurrentlySelected);
        }
    }

    public void savePrompt() {
        Stage saveStage = new Stage();
        saveStage.setTitle("Exit");
        saveStage.setWidth(300);
        saveStage.setHeight(100);
        saveStage.initStyle(StageStyle.UNDECORATED);
        saveStage.initModality(Modality.APPLICATION_MODAL);

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setVgap(10);
        pane.setAlignment(Pos.CENTER);

        Text text = new Text("Would you like to save your file?");
        GridPane.setConstraints(text, 0, 0);

        HBox choice = new HBox();
        Button yes = new Button("Yes");
        Button no = new Button("No");
        Button cancel = new Button("Cancel");

        choice.getChildren().addAll(yes, no, cancel);
        choice.setAlignment(Pos.CENTER);
        choice.setSpacing(5.0);

        GridPane.setConstraints(choice, 0, 1);
        pane.getChildren().addAll(text, choice);

        Scene scene = new Scene(pane);
        saveStage.setScene(scene);

        saveButtonListeners(yes, no, cancel, saveStage);

        setMiddle(saveStage);
        saveStage.show();
    }

    public void saveButtonListeners(Button yes, Button no, Button cancel, Stage savePromptStage) {
        yes.setOnAction(e -> {
            session.saveEntries();
            session.endSession();
            Platform.exit();
            System.exit(0);
        });

        no.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        cancel.setOnAction(e -> savePromptStage.close());
    }

    private void setMiddle(Stage s) {
        double middleCoordinateX = screenSize.getWidth() / 2;
        double middleCoordinateY = screenSize.getHeight() / 2;
        double subtractWindowSizeX = s.getWidth() / 2;
        double subtractWindowSizeY = s.getHeight() / 2;
        s.setX(middleCoordinateX - subtractWindowSizeX);
        s.setY(middleCoordinateY - subtractWindowSizeY);
    }

    private void clearButtonColours(Button button1, Button button2, Button button3, Button button4) {
        button1.setStyle("-fx-background-color: #585858;");
        button2.setStyle("-fx-background-color: #585858;");
        button3.setStyle("-fx-background-color: #585858;");
        button4.setStyle("-fx-background-color: #585858;");
    }
}

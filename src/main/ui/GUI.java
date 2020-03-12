package ui;

import exceptions.CategoryAlreadyExistsException;
import exceptions.NullEntryException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.stream.Collectors;

//TODO: edit/delete categories
//TODO: edit/delete journal entries
//TODO: add time spent to category list in journal entry
//TODO: find way to get CategoryList page to refresh after each action (create new and selection)

public class GUI extends Application {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    ComboBox<String> comboBox;
    TableView<JournalEntry> journalTable;
    TableView<JournalEntry> categoryJournalTable;
    Button newJournalEntry;
    Button homePage;
    Button viewJournalLog;
    Button viewCategoryList;
    TimeJournalApp session;

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final int TITLE_FONT_SIZE = 35;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Time Journal");
        session = new TimeJournalApp();
        boolean noSaveFile = session.isFirstTime();

        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setMinWidth(WINDOW_WIDTH);
        stage.setMinHeight(WINDOW_HEIGHT);
        setMiddle(stage);

        if (noSaveFile) {
            initNoExistingSaveFile(stage);
        } else {
            initSelectUser(stage);
        }
    }

    public void initSelectUser(Stage stage) {
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

        newUserButton.setOnAction(e -> initNoExistingSaveFile(stage));
        returningUserButton.setOnAction(e -> userSelect(stage));

        grid.setAlignment(Pos.CENTER);
        grid.getChildren().addAll(newUserButton, returningUserButton);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(title, grid);

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void initNoExistingSaveFile(Stage stage) {
        Label title = new Label("Welcome to Time Journal");
        title.setStyle("-fx-font-size: 60px; -fx-text-fill: #383838;");
        title.setPadding(new Insets(0, 0, 75, 0));

        Button newUserButton = new Button(">");
        newUserButton.setStyle("-fx-min-width: 75;");

        newUserButton.setOnAction(e -> completelyNewUser(stage));

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(title, newUserButton);

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void completelyNewUser(Stage stage) {
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

        setUserNameListener(newUserButton, name, stage);

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void setUserNameListener(Button newUserButton, TextField name, Stage stage) {
        newUserButton.setOnAction(e -> {
            try {
                session.setCurrentUser(name.getText());
                session.newSession();
                firstNewCategory(stage, name.getText());
            } catch (NullEntryException exception) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("You must enter at least one character for your name.");
                a.show();
            }
        });
    }

    public void firstNewCategory(Stage stage, String userName) {
        Label nameLabel = new Label("Let's start with creating your first category.\n"
                + "Enter a name for your category below:");
        nameLabel.setStyle("-fx-font-size: 45px; -fx-text-fill: #383838;");
        nameLabel.setAlignment(Pos.CENTER);

        TextField categoryName = new TextField();
        categoryName.setMaxWidth(300);
        categoryName.setStyle("-fx-font-size: 20px;");
        categoryName.setAlignment(Pos.CENTER);

        Button startJournal = new Button("Get Started");
        startJournal.setAlignment(Pos.CENTER);

        setButtonHandlerForNewUser(stage, userName, startJournal, categoryName);

        VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(nameLabel, categoryName, startJournal);

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
        stage.show();
    }

    public void setButtonHandlerForNewUser(Stage stage, String userName, Button startJournal, TextField categoryName) {
        startJournal.setOnAction(e -> {
            try {
                session.createNewCategory(categoryName.getText());
                renderSideBar(stage, userName);
            } catch (NullEntryException e1) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("You must enter a name for your category.");
                a.show();
            } catch (CategoryAlreadyExistsException exception) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("You happened to enter the one category that already exists... try again.");
                a.show();
            }
        });
    }

    public void userSelect(Stage stage) {
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
                renderSideBar(stage, session.getCurrentUser());
            } catch (NullEntryException exception) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("Please select a user.");
                a.show();
            }
        });

        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add("ui/style.css");
        stage.show();
    }

    public void renderSideBar(Stage stage, String name) {
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

        homePage = new Button("Home");
        GridPane.setConstraints(homePage, 0, 1);

        newJournalEntry = new Button("Create Journal Entry");
        GridPane.setConstraints(newJournalEntry, 0, 2);

        viewJournalLog = new Button("Journal Entry Log");
        GridPane.setConstraints(viewJournalLog, 0, 3);

        viewCategoryList = new Button("Category List");
        GridPane.setConstraints(viewCategoryList, 0, 4);

        Button quit = new Button("Exit");
        AnchorPane.setBottomAnchor(quit, 14.0);
        AnchorPane.setLeftAnchor(quit, 10.0);

        menuItems.getChildren().addAll(userName, newJournalEntry, homePage, viewJournalLog, viewCategoryList);
        sideBar.getChildren().add(menuItems);
        pane.getChildren().addAll(sideBar, quit);

        menuButtonListeners(newJournalEntry, homePage, viewJournalLog, viewCategoryList, stage, sideBar, quit);

        homePage(stage, sideBar, quit, homePage);
    }

    public void menuButtonListeners(Button newJournalEntry, Button homePage, Button viewJournalLog,
                                    Button viewCategoryList, Stage stage, Pane sideBar, Button quit) {
        newJournalEntry.setOnAction(e -> {
            clearButtonColours(newJournalEntry, homePage, viewJournalLog, viewCategoryList);
            createJournalEntry(stage, sideBar, quit, newJournalEntry);
        });

        homePage.setOnAction(e -> {
            clearButtonColours(newJournalEntry, homePage, viewJournalLog, viewCategoryList);
            homePage(stage, sideBar, quit, homePage);
        });

        viewJournalLog.setOnAction(e -> {
            clearButtonColours(newJournalEntry, homePage, viewJournalLog, viewCategoryList);
            viewJournalEntries(stage, sideBar, quit, viewJournalLog);
        });

        viewCategoryList.setOnAction(e -> {
            clearButtonColours(newJournalEntry, homePage, viewJournalLog, viewCategoryList);
            viewAllCategories(stage, sideBar, quit, viewCategoryList);
        });

        stage.setOnCloseRequest(e -> {
            e.consume();
            savePrompt();
        });

        quit.setOnAction(e -> savePrompt());
    }

    public void homePage(Stage stage, Pane sideBar, Button quit, Button homePageButton) {
        AnchorPane pane = new AnchorPane();

        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Home");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        pane.getChildren().addAll(sideBar, quit, title);

        homePageButton.setStyle("-fx-background-color:#787878");

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        stage.setScene(scene);
        stage.show();
    }

    public void createJournalEntry(Stage stage, Pane sideBar, Button quit, Button newJournalEntryMenuButton) {
        AnchorPane pane = new AnchorPane();

        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Create New Journal Entry");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        Text descriptionLabel = new Text("What did you get up to? Enter a description for your journal entry:");
        descriptionLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(descriptionLabel, 95.0);
        AnchorPane.setLeftAnchor(descriptionLabel, 230.0);
        AnchorPane.setRightAnchor(descriptionLabel, 30.0);

        TextField descriptionField = new TextField();
        AnchorPane.setTopAnchor(descriptionField, 125.0);
        AnchorPane.setLeftAnchor(descriptionField, 230.0);
        AnchorPane.setRightAnchor(descriptionField, 30.0);

        Text durationLabel = new Text("How long did you spend on this? (in minutes)");
        durationLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(durationLabel, 185.0);
        AnchorPane.setLeftAnchor(durationLabel, 230.0);
        AnchorPane.setRightAnchor(durationLabel, 30.0);

        TextField durationField = new TextField();
        AnchorPane.setTopAnchor(durationField, 215.0);
        AnchorPane.setLeftAnchor(durationField, 230.0);
        AnchorPane.setRightAnchor(durationField, 30.0);

        Text categoryLabel = new Text("What category would you like to assign this entry to?");
        categoryLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(categoryLabel, 265.0);
        AnchorPane.setLeftAnchor(categoryLabel, 230.0);
        AnchorPane.setRightAnchor(categoryLabel, 30.0);

        ComboBox<String> categoryList = new ComboBox<>();
        ObservableList<Category> listToAdd = generateCategoryList();

        for (Category c : listToAdd) {
            categoryList.getItems().add(c.getName());
        }

        categoryList.setValue("Uncategorized");
        AnchorPane.setTopAnchor(categoryList, 295.0);
        AnchorPane.setLeftAnchor(categoryList, 230.0);
        AnchorPane.setRightAnchor(categoryList, 30.0);

        Button submit = new Button("Submit");
        AnchorPane.setBottomAnchor(submit, 30.0);
        AnchorPane.setRightAnchor(submit, 30.0);

        pane.getChildren().addAll(sideBar, quit, title, durationLabel, descriptionLabel, categoryLabel,
                descriptionField, categoryList, durationField, submit);

        setJournalEntrySubmitListener(submit, descriptionField, durationField, categoryList);

        newJournalEntryMenuButton.setStyle("-fx-background-color:#787878");

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        stage.setScene(scene);
        stage.show();
    }

    public void setJournalEntrySubmitListener(
            Button submit, TextField descriptionField, TextField durationField, ComboBox<String> categoryList) {
        submit.setOnAction(e -> {
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
        });
    }

    public void viewJournalEntries(Stage stage, Pane sideBar, Button quit, Button journalLogMenuButton) {
        AnchorPane pane = new AnchorPane();

        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Journal Entry Log");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        journalLogMenuButton.setStyle("-fx-background-color:#787878");

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        setJournalTableColumns();

        Button delete = new Button("Delete");
        delete.setStyle("-fx-min-width: 100;");
        AnchorPane.setRightAnchor(delete, 30.0);
        AnchorPane.setBottomAnchor(delete, 30.0);

        Button edit = new Button("Edit");
        edit.setStyle("-fx-min-width: 100;");
        AnchorPane.setRightAnchor(edit, 145.0);
        AnchorPane.setBottomAnchor(edit, 30.0);

        // to get data from the app
        edit.setOnAction(e -> {
            ObservableList<JournalEntry> entrySelected = journalTable.getSelectionModel().getSelectedItems();
            System.out.println(entrySelected.get(0).getDescription());
        });

        Button createNew = new Button("Create New Entry");
        AnchorPane.setTopAnchor(createNew, 30.0);
        AnchorPane.setRightAnchor(createNew, 30.0);

        createNew.setOnAction(e -> {
            clearButtonColours(newJournalEntry, homePage, viewJournalLog, journalLogMenuButton);
            createJournalEntry(stage, sideBar, quit, newJournalEntry);
        });

        pane.getChildren().addAll(sideBar, quit, title, journalTable, delete, edit, createNew);

        stage.setScene(scene);
        stage.show();
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
        AnchorPane.setBottomAnchor(journalTable, 100.0);
        AnchorPane.setLeftAnchor(journalTable, 230.0);
        AnchorPane.setRightAnchor(journalTable, 30.0);
    }

    public void viewAllCategories(Stage stage, Pane sideBar, Button quit, Button categoriesMenuButton) {
        AnchorPane pane = new AnchorPane();

        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Category List");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        ListView<String> categoryDurationList = new ListView<>();
        ObservableList<Category> observableList = generateCategoryList();

        for (Category category : observableList) {
            categoryDurationList.getItems().add(category.getDurationString());
        }

        categoryDurationList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        categoryDurationList.setMaxHeight(275);
        AnchorPane.setLeftAnchor(categoryDurationList, 230.0);
        AnchorPane.setTopAnchor(categoryDurationList, 95.0);
        AnchorPane.setRightAnchor(categoryDurationList, 30.0);

        categoryJournalTable = renderCategoryJournalEntryTable(getEntries());

        Button createNewCategory = new Button("Create New Category");
        AnchorPane.setRightAnchor(createNewCategory, 30.0);
        AnchorPane.setTopAnchor(createNewCategory, 30.0);
        createNewCategory.setOnAction(e -> createNewCategory());

        // string to filter with
        final String[] toFilter = new String[1];
        categoryTableListener(
                sideBar, quit, title, categoryDurationList, toFilter, observableList, pane, createNewCategory);

        pane.getChildren().addAll(sideBar, quit, title, categoryDurationList, categoryJournalTable, createNewCategory);

        categoriesMenuButton.setStyle("-fx-background-color:#787878");

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        stage.setScene(scene);
        stage.show();
    }

    public void categoryTableListener(Pane sideBar, Button quit, Text title, ListView<String> categoryDurationList,
                                      String[] toFilter, ObservableList<Category> observableList,
                                      AnchorPane pane, Button createNewCategory) {
        categoryDurationList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        int index = categoryDurationList.getSelectionModel().getSelectedIndex();
                        toFilter[0] = observableList.get(index).getName();
                        filterList(toFilter[0]);
                        pane.getChildren().clear();
                        pane.getChildren().addAll(sideBar, quit, title, categoryDurationList,
                                categoryJournalTable, createNewCategory);
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
            categoryJournalTable = renderCategoryJournalEntryTable(result);
            categoryJournalTable.setPlaceholder(new Text("No entries for " + filterCondition));
        } else {
            categoryJournalTable = renderCategoryJournalEntryTable(result);
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

    public TableView<JournalEntry> renderCategoryJournalEntryTable(List<JournalEntry> entries) {
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

        categoryJournalTable.setItems(observableList);
        categoryJournalTable.getColumns().addAll(
                dateTableColumn, categoryTableColumn, durationTableColumn, descriptionTableColumn);
        AnchorPane.setTopAnchor(categoryJournalTable, 385.0);
        AnchorPane.setBottomAnchor(categoryJournalTable, 30.0);
        AnchorPane.setRightAnchor(categoryJournalTable, 30.0);
        AnchorPane.setLeftAnchor(categoryJournalTable, 230.0);
        return categoryJournalTable;
    }

    public void createNewCategory() {
        Stage stage = new Stage();
        stage.setWidth(WINDOW_WIDTH - 600);
        stage.setHeight(WINDOW_HEIGHT - 450);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox vbox = new VBox();
        vbox.setSpacing(20.0);

        Text text = new Text("Enter a name for your your category:");
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle("-fx-font-size:16px;");

        TextField categoryName = new TextField();
        categoryName.setAlignment(Pos.CENTER);
        categoryName.setMaxWidth(300);
        categoryName.setStyle("-fx-font-size:14px;");

        HBox hbox = new HBox();
        hbox.setSpacing(10.0);

        Button submit = new Button("Submit");
        submit.setStyle("-fx-min-width: 100; -fx-min-height:35;");

        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-min-width: 100; -fx-min-height:35;");

        hbox.getChildren().addAll(submit, cancel);
        hbox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(text, categoryName, hbox);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
        setMiddle(stage);

        createCategoryListeners(stage, submit, cancel);
        tryToCreateNewCategory(submit, categoryName, stage);

        stage.show();
    }

    public void tryToCreateNewCategory(Button submit, TextField categoryName, Stage stage) {
        submit.setOnAction(e -> {
            try {
                session.createNewCategory(categoryName.getText());
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setContentText("You've successfully added the category.");
                a.show();
                stage.close();
            } catch (NullEntryException e1) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("You must enter a name for your category.");
                a.show();
            } catch (CategoryAlreadyExistsException exception) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("Sorry, that category already exists. Please try again.");
                a.show();
            }
        });
    }

    public void createCategoryListeners(Stage stage, Button submit, Button cancel) {
        cancel.setOnAction(e -> stage.close());
    }

    public void savePrompt() {
        Stage stage = new Stage();
        stage.setTitle("Exit");
        stage.setWidth(300);
        stage.setHeight(100);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

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
        stage.setScene(scene);

        saveButtonListeners(yes, no, cancel, stage);

        setMiddle(stage);
        stage.show();
    }

    public void saveButtonListeners(Button yes, Button no, Button cancel, Stage stage) {
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

        cancel.setOnAction(e -> stage.close());
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

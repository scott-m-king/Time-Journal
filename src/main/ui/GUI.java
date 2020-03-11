package ui;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.stage.StageStyle;
import model.Category;
import model.JournalEntry;

import java.awt.*;
import java.util.ArrayList;

//TODO: Add homescreen

public class GUI extends Application {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    ComboBox<String> comboBox;
    TableView<JournalEntry> journalTable;
    Button newJournalEntry;
    Button newCategory;
    Button viewJournalLog;
    Button viewCategoryList;

    TimeJournalApp session;

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final int TITLE_FONT_SIZE = 35;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Time Journal");

        stage.setMinWidth(WINDOW_WIDTH);
        stage.setWidth(WINDOW_WIDTH);
        stage.setMinHeight(WINDOW_HEIGHT);
        stage.setHeight(WINDOW_HEIGHT);
        setMiddle(stage);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label title = new Label("Time Journal");
        title.setStyle("-fx-font-size: 70px; -fx-text-fill: #383838;");
        title.setPadding(new Insets(0, 0, 75, 0));

        Button newUserButton = new Button("New User");
        newUserButton.setMinWidth(100);
        GridPane.setConstraints(newUserButton, 0, 0);

        Button returningUserButton = new Button("Returning User");
        returningUserButton.setMinWidth(100);
        GridPane.setConstraints(returningUserButton, 0, 1);

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

        ArrayList<String> list = new ArrayList<>();
        list.add("Scott");
        list.add("Bob");
        list.add("Susan");

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

        submit.setOnAction(e -> renderSideBar(stage, comboBox.getValue()));

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

        Label userName = new Label(name + "'s Journal");
        userName.setWrapText(true);
        GridPane.setConstraints(userName, 0, 0);
        userName.setPadding(new Insets(0, 0, 15, 0));
        GridPane.setHalignment(userName, HPos.CENTER);

        newJournalEntry = new Button("Create Journal Entry");
        GridPane.setConstraints(newJournalEntry, 0, 1);

        newCategory = new Button("Create category");
        GridPane.setConstraints(newCategory, 0, 2);

        viewJournalLog = new Button("Journal Entry Log");
        GridPane.setConstraints(viewJournalLog, 0, 3);

        viewCategoryList = new Button("Category List");
        GridPane.setConstraints(viewCategoryList, 0, 4);

        Button quit = new Button("Exit");
        AnchorPane.setBottomAnchor(quit, 14.0);
        AnchorPane.setLeftAnchor(quit, 10.0);

        menuItems.getChildren().addAll(userName, newJournalEntry, newCategory, viewJournalLog, viewCategoryList);
        sideBar.getChildren().add(menuItems);
        pane.getChildren().addAll(sideBar, quit);

        menuButtonListeners(newJournalEntry, newCategory, viewJournalLog, viewCategoryList, stage, sideBar, quit);

        stage.setScene(scene);
        stage.show();
    }

    public void menuButtonListeners(Button newJournalEntry, Button newCategory, Button viewJournalLog,
                                    Button viewCategoryList, Stage stage, Pane sideBar, Button quit) {
        newJournalEntry.setOnAction(e ->  {
            clearButtonColours(newJournalEntry, newCategory, viewJournalLog, viewCategoryList);
            createJournalEntry(stage, sideBar, quit, newJournalEntry);
        });

        newCategory.setOnAction(e -> {
            clearButtonColours(newJournalEntry, newCategory, viewJournalLog, viewCategoryList);
            createNewCategory(stage, sideBar, quit, newCategory);
        });

        viewJournalLog.setOnAction(e -> {
            clearButtonColours(newJournalEntry, newCategory, viewJournalLog, viewCategoryList);
            viewJournalEntries(stage, sideBar, quit, viewJournalLog);
        });

        viewCategoryList.setOnAction(e -> {
            clearButtonColours(newJournalEntry, newCategory, viewJournalLog, viewCategoryList);
            viewAllCategories(stage, sideBar, quit, viewCategoryList);
        });

        quit.setOnAction(e -> savePrompt());
    }

    public void createJournalEntry(Stage stage, Pane sideBar, Button quit, Button newJournalEntry) {
        AnchorPane pane = new AnchorPane();

        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Create New Journal Entry");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        Text descriptionLabel = new Text("What did you get up to? Enter a description for your journal entry:");
        descriptionLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(descriptionLabel, 95.0);
        AnchorPane.setLeftAnchor(descriptionLabel, 230.0);
        AnchorPane.setRightAnchor(descriptionLabel, 30.0);

        TextField description = new TextField();
        AnchorPane.setTopAnchor(description, 125.0);
        AnchorPane.setLeftAnchor(description, 230.0);
        AnchorPane.setRightAnchor(description, 30.0);

        Text durationLabel = new Text("How long did you spend on this? (in minutes)");
        durationLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(durationLabel, 185.0);
        AnchorPane.setLeftAnchor(durationLabel, 230.0);
        AnchorPane.setRightAnchor(durationLabel, 30.0);

        TextField duration = new TextField();
        AnchorPane.setTopAnchor(duration, 215.0);
        AnchorPane.setLeftAnchor(duration, 230.0);
        AnchorPane.setRightAnchor(duration, 30.0);

        Text categoryLabel = new Text("What category would you like to assign this entry to?");
        categoryLabel.setStyle("-fx-font-size:17px;");
        AnchorPane.setTopAnchor(categoryLabel, 265.0);
        AnchorPane.setLeftAnchor(categoryLabel, 230.0);
        AnchorPane.setRightAnchor(categoryLabel, 30.0);

        ComboBox<String> categoryList = new ComboBox<>();
        categoryList.setPromptText("Choose category...");
        AnchorPane.setTopAnchor(categoryList, 295.0);
        AnchorPane.setLeftAnchor(categoryList, 230.0);
        AnchorPane.setRightAnchor(categoryList, 30.0);

        Button submit = new Button("Submit");
        AnchorPane.setBottomAnchor(submit, 30.0);
        AnchorPane.setRightAnchor(submit, 30.0);

        pane.getChildren().addAll(sideBar, quit, title, durationLabel, descriptionLabel, categoryLabel,
                description, categoryList, duration, submit);

        newJournalEntry.setStyle("-fx-background-color:#787878");

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        stage.setScene(scene);
        stage.show();
    }

    public void createNewCategory(Stage stage, Pane sideBar, Button quit, Button newCategory) {
        AnchorPane pane = new AnchorPane();

        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Create New Category");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        pane.getChildren().addAll(sideBar, quit, title);

        newCategory.setStyle("-fx-background-color:#787878");

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        stage.setScene(scene);
        stage.show();
    }

    public void viewJournalEntries(Stage stage, Pane sideBar, Button quit, Button viewCategoryList) {
        AnchorPane pane = new AnchorPane();

        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Journal Entry Log");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        viewCategoryList.setStyle("-fx-background-color:#787878");

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        renderJournalEntryTable();

        Button delete = new Button("Delete");
        delete.setStyle("-fx-min-width: 100;");
        AnchorPane.setRightAnchor(delete, 30.0);
        AnchorPane.setBottomAnchor(delete, 30.0);

        Button edit = new Button("Edit");
        edit.setStyle("-fx-min-width: 100;");
        AnchorPane.setRightAnchor(edit, 145.0);
        AnchorPane.setBottomAnchor(edit, 30.0);

        edit.setOnAction(e -> {
            ObservableList<JournalEntry> entrySelected = journalTable.getSelectionModel().getSelectedItems();
            System.out.println(entrySelected.get(0).getDescription());
        });

        Button createNew = new Button("Create New...");
        createNew.setStyle("-fx-min-width: 100;");
        AnchorPane.setBottomAnchor(createNew, 30.0);
        AnchorPane.setRightAnchor(createNew, 260.0);

        createNew.setOnAction(e -> {
            clearButtonColours(newJournalEntry, newCategory, viewJournalLog, viewCategoryList);
            createJournalEntry(stage, sideBar, quit, newJournalEntry);
        });

        pane.getChildren().addAll(sideBar, quit, title, journalTable, delete, edit, createNew);

        stage.setScene(scene);
        stage.show();
    }

    public ObservableList<JournalEntry> getEntries() {
        Category sleep = new Category(0, "Sleep");
        Category homework = new Category(1, "Homework");
        ObservableList<JournalEntry> entries = FXCollections.observableArrayList();
        entries.add(new JournalEntry(0, "nap", 0, sleep, 45));
        entries.add(new JournalEntry(1, "more nap", 0, sleep, 60));
        entries.add(new JournalEntry(2, "210 hwk", 0, homework, 120));
        return entries;
    }

    public void renderJournalEntryTable() {
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

        journalTable = new TableView<>();
        journalTable.setItems(getEntries());
        journalTable.getColumns().addAll(idColumn, dateTableColumn,
                categoryTableColumn, durationTableColumn, descriptionTableColumn);
        AnchorPane.setTopAnchor(journalTable, 95.0);
        AnchorPane.setBottomAnchor(journalTable, 100.0);
        AnchorPane.setLeftAnchor(journalTable, 230.0);
        AnchorPane.setRightAnchor(journalTable, 30.0);
    }

    public void viewAllCategories(Stage stage, Pane sideBar, Button quit, Button viewJournalLog) {
        AnchorPane pane = new AnchorPane();

        Text title = new Text();
        title.setFont(new Font(TITLE_FONT_SIZE));
        title.setText("Category List");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);

        pane.getChildren().addAll(sideBar, quit, title);

        viewJournalLog.setStyle("-fx-background-color:#787878");

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        stage.setScene(scene);
        stage.show();
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

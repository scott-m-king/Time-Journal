package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.stage.StageStyle;

import java.awt.*;
import java.util.ArrayList;

public class GUI extends Application {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    ComboBox<String> comboBox;
    TimeJournalApp session;

    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 700;

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
        title.setStyle("-fx-font-size: 70px");
        title.setPadding(new Insets(0, 0, 75, 0));

        Button button1 = new Button("New User");
        button1.setMinWidth(100);
        GridPane.setConstraints(button1, 0, 0);

        Button button2 = new Button("Returning User");
        button2.setMinWidth(100);
        GridPane.setConstraints(button2, 0, 1);

        button2.setOnAction(e -> userSelect(stage));

        grid.setAlignment(Pos.CENTER);
        grid.getChildren().addAll(button1, button2);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(title, grid);

        Scene scene = new Scene(vbox);
        stage.setScene(scene);

        stage.show();
    }

    public void userSelect(Stage stage) {
        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(50);
        root.setHgap(10);

        Label title = new Label("Which user are you?");
        title.setStyle("-fx-font-size: 40px");
        GridPane.setConstraints(title, 0, 0);

        comboBox = new ComboBox<>();
        comboBox.setPromptText("Select one...");
        comboBox.setMinWidth(200);

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

        submit.setOnAction(e -> {
            System.out.println(comboBox.getValue());
            mainMenu(stage, comboBox.getValue());
        });

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void mainMenu(Stage stage, String name) {
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
        menuItems.setPadding(new Insets(35, 0, 0, 0));
        menuItems.setVgap(15);
        menuItems.setMaxWidth(200);

        Label userName = new Label(name + "'s Journal");
        userName.setWrapText(true);
        GridPane.setConstraints(userName, 0, 0);
        userName.setPadding(new Insets(0, 0, 15, 0));
        GridPane.setHalignment(userName, HPos.CENTER);

        Button newJournalEntry = new Button("Create Journal Entry");
        GridPane.setConstraints(newJournalEntry, 0, 1);

        Button newCategory = new Button("Create category");
        GridPane.setConstraints(newCategory, 0, 2);

        Button viewJournalLog = new Button("Journal Entry Log");
        GridPane.setConstraints(viewJournalLog, 0, 3);

        Button viewCategoryList = new Button("Category Log");
        GridPane.setConstraints(viewCategoryList, 0, 4);

        Button quit = new Button("Exit");
        AnchorPane.setBottomAnchor(quit, 25.0);

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
        pane.getChildren().addAll(sideBar, quit);

        newJournalEntry.setStyle("-fx-background-color:#787878");

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        stage.setScene(scene);
        stage.show();
    }

    public void createNewCategory(Stage stage, Pane sideBar, Button quit, Button newCategory) {
        AnchorPane pane = new AnchorPane();
        pane.getChildren().addAll(sideBar, quit);

        newCategory.setStyle("-fx-background-color:#787878");

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        stage.setScene(scene);
        stage.show();
    }

    public void viewJournalEntries(Stage stage, Pane sideBar, Button quit, Button viewCategoryList) {
        AnchorPane pane = new AnchorPane();
        pane.getChildren().addAll(sideBar, quit);

        viewCategoryList.setStyle("-fx-background-color:#787878");

        Scene scene = new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("ui/style.css");

        stage.setScene(scene);
        stage.show();
    }

    public void viewAllCategories(Stage stage, Pane sideBar, Button quit, Button viewJournalLog) {
        AnchorPane pane = new AnchorPane();
        pane.getChildren().addAll(sideBar, quit);

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
        no.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });
        cancel.setOnAction(e -> stage.close());

        choice.getChildren().addAll(yes, no, cancel);
        choice.setAlignment(Pos.CENTER);

        GridPane.setConstraints(choice, 0, 1);

        pane.getChildren().addAll(text, choice);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        setMiddle(stage);
        stage.show();
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

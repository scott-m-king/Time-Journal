package ui.components;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import ui.UserInterface;

public class SideBar {
    private final UserInterface userInterface;
    private Button newJournalEntryButton;
    private Button homePageButton;
    private Button viewJournalLogButton;
    private Button viewCategoryListButton;
    private Pane sideBarPane;

    public SideBar(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void makeSideBar() {
        sideBarPane = populateSideBar();
        setSideBarButtonListeners(sideBarPane);
        initializeSideBar(sideBarPane);
        userInterface.homePage(sideBarPane, homePageButton);
    }

    public Pane populateSideBar() {
        Pane sideBar = setSideBarColorAndAnchors();
        GridPane menuItems = createMenuItems();
        sideBar.getChildren().add(menuItems);
        return sideBar;
    }

    public GridPane createMenuItems() {
        GridPane menuItems = new GridPane();
        menuItems.setPadding(new Insets(35, 0, 0, 10));
        menuItems.setVgap(15);

        Label userName = sideBarWelcomeLabel();
        setSideBarButtons();

        menuItems.getChildren().addAll(
                userName,
                newJournalEntryButton,
                homePageButton,
                viewJournalLogButton,
                viewCategoryListButton);
        return menuItems;
    }

    public Label sideBarWelcomeLabel() {
        Label userName = new Label(userInterface.getSession().getUserName());
        userName.setTextAlignment(TextAlignment.CENTER);
        userName.setWrapText(true);
        GridPane.setConstraints(userName, 0, 0);
        userName.setPadding(new Insets(0, 0, 15, 0));
        GridPane.setHalignment(userName, HPos.CENTER);
        return userName;
    }

    public Pane setSideBarColorAndAnchors() {
        Pane sideBar = new Pane();
        sideBar.setPrefWidth(200);
        sideBar.setStyle("-fx-background-color:#383838");
        AnchorPane.setTopAnchor(sideBar, 0.0);
        AnchorPane.setBottomAnchor(sideBar, 0.0);
        AnchorPane.setLeftAnchor(sideBar, 0.0);
        return sideBar;
    }

    public void initializeSideBar(Pane sideBar) {
        AnchorPane anchorPane = new AnchorPane();
        Scene scene = new Scene(anchorPane);
        scene.getStylesheets().add("ui/style.css");
        anchorPane.getChildren().addAll(sideBar, userInterface.getQuitButton());
    }

    public void setSideBarButtons() {
        homePageButton = new Button("Home");
        GridPane.setConstraints(homePageButton, 0, 1);

        newJournalEntryButton = new Button("Create Journal Entry");
        GridPane.setConstraints(newJournalEntryButton, 0, 2);

        viewJournalLogButton = new Button("Journal Entry Log");
        GridPane.setConstraints(viewJournalLogButton, 0, 3);

        viewCategoryListButton = new Button("Category List");
        GridPane.setConstraints(viewCategoryListButton, 0, 4);

        userInterface.setQuitButton(new Button("Exit"));
        AnchorPane.setBottomAnchor(userInterface.getQuitButton(), 14.0);
        AnchorPane.setLeftAnchor(userInterface.getQuitButton(), 10.0);
    }

    public void setSideBarButtonListeners(Pane sideBar) {

        newJournalEntryButton.setOnAction(e -> {
            userInterface.clearButtonColours();
            userInterface.createJournalEntry(sideBar, newJournalEntryButton);
        });

        homePageButton.setOnAction(e -> {
            userInterface.clearButtonColours();
            userInterface.homePage(sideBar, homePageButton);
        });

        viewJournalLogButton.setOnAction(e -> {
            userInterface.clearButtonColours();
            userInterface.viewJournalEntries();
        });

        viewCategoryListButton.setOnAction(e -> {
            userInterface.clearButtonColours();
            userInterface.viewAllCategories();
        });

        userInterface.getMainStage().setOnCloseRequest(e -> {
            e.consume();
            userInterface.saveSession();
        });

        userInterface.getQuitButton().setOnAction(e -> userInterface.saveSession());
    }

    public Button getNewJournalEntryButton() {
        return newJournalEntryButton;
    }

    public Button getHomePageButton() {
        return homePageButton;
    }

    public Button getViewJournalLogButton() {
        return viewJournalLogButton;
    }

    public Button getViewCategoryListButton() {
        return viewCategoryListButton;
    }

    public Pane getSideBarPane() {
        return sideBarPane;
    }
}
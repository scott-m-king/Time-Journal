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

public class SideBarComponent {
    private final UserInterface userInterface;
    private Button newJournalEntryButton;
    private Button homePageButton;
    private Button viewJournalLogButton;
    private Button viewCategoryListButton;
    private Label userName;
    private Pane paneBackground;
    private GridPane menuItems;
    private Pane sideBarPane;

    public SideBarComponent(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderSideBar() {
        sideBarPane = populateSideBar();
        setSideBarButtonListeners(sideBarPane);
        initializeSideBar(sideBarPane);
        userInterface.homePage(sideBarPane, homePageButton);
    }

    public Pane populateSideBar() {
        setSideBarColorAndAnchors();
        createMenuItems();
        paneBackground.getChildren().add(menuItems);
        return paneBackground;
    }

    public void createMenuItems() {
        menuItems = new GridPane();
        menuItems.setPadding(new Insets(35, 0, 0, 10));
        menuItems.setVgap(15);

        sideBarWelcomeLabel();
        setSideBarButtons();

        menuItems.getChildren().addAll(
                userName,
                newJournalEntryButton,
                homePageButton,
                viewJournalLogButton,
                viewCategoryListButton);
    }

    public void sideBarWelcomeLabel() {
        userName = new Label(userInterface.getCurrentSession().getUserName());
        userName.setTextAlignment(TextAlignment.CENTER);
        userName.setWrapText(true);
        GridPane.setConstraints(userName, 0, 0);
        userName.setPadding(new Insets(0, 0, 15, 0));
        GridPane.setHalignment(userName, HPos.CENTER);
    }

    public void setSideBarColorAndAnchors() {
        paneBackground = new Pane();
        paneBackground.setPrefWidth(200);
        paneBackground.setStyle("-fx-background-color:#383838");
        AnchorPane.setTopAnchor(paneBackground, 0.0);
        AnchorPane.setBottomAnchor(paneBackground, 0.0);
        AnchorPane.setLeftAnchor(paneBackground, 0.0);
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
        setCreateJournalEntryButtonListener();
        setHomePageButtonListener(sideBar);
        setJournalLogButtonListener();
        setCategoryListButtonListener();
        setCloseAndSaveListeners();
    }

    private void setHomePageButtonListener(Pane sideBar) {
        homePageButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.homePage(sideBar, homePageButton);
        });
    }

    private void setCreateJournalEntryButtonListener() {
        newJournalEntryButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.createJournalEntry();
        });
    }

    private void setJournalLogButtonListener() {
        viewJournalLogButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.viewJournalEntries();
        });
    }

    private void setCategoryListButtonListener() {
        viewCategoryListButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.viewAllCategories();
        });
    }

    private void setCloseAndSaveListeners() {
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
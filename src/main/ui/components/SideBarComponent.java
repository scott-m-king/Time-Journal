package ui.components;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import ui.UserInterface;

// Represents a SideBar component that is used in Screen classes
public class SideBarComponent {
    private final UserInterface userInterface;
    private Button newJournalEntryButton;
    private Button homePageButton;
    private Button viewJournalLogButton;
    private Button viewCategoryListButton;
    private Button quit;
    private Label userName;
    private Label timeJournal;
    private Pane paneBackground;
    private ImageView userAvatar;
    private GridPane menuItems;
    private Pane sideBarPane;
    private VBox vbox;

    public SideBarComponent(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // REQUIRES: valid UserInterface object
    // MODIFIES: this
    // EFFECTS: runs initialization of all components of sidebar
    public void renderSideBar() {
        populateSideBar();
        setSideBarButtonListeners();
        setSideBarAnchorPane();
        userInterface.getHomePageScreen().renderHomePage();
    }

    // MODIFIES: this
    // EFFECTS: renders final sidebar pane
    public void populateSideBar() {
        setSideBarColorAndAnchors();
        createMenuItems();
        paneBackground.getChildren().add(menuItems);
        sideBarPane = paneBackground;
    }

    // MODIFIES: this
    // EFFECTS: creates a gridpane to populate with menu buttons, labels, and avatar picture
    public void createMenuItems() {
        menuItems = new GridPane();
        menuItems.setPadding(new Insets(15, 0, 0, 10));
        menuItems.setVgap(15);

        setSideBarTitleAndUser();
        setSideBarButtons();

        menuItems.getChildren().addAll(
                vbox,
                timeJournal,
                newJournalEntryButton,
                homePageButton,
                viewJournalLogButton,
                viewCategoryListButton
        );
    }

    // MODIFIES: this
    // EFFECTS: calls all methods relating to main title, avatar picture, and user name
    //          sets the position of components in gridpane
    public void setSideBarTitleAndUser() {
        setAvatarAndName();
        setTitle();
        createAvatarAndUsernameVBox();
        GridPane.setConstraints(timeJournal, 0, 0);
        GridPane.setHalignment(timeJournal, HPos.CENTER);
        GridPane.setConstraints(vbox, 0, 1);
        GridPane.setHalignment(vbox, HPos.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: creates a VBox to house avatar and username
    private void createAvatarAndUsernameVBox() {
        vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinWidth(180);
        vbox.setMaxWidth(180);
        vbox.getChildren().addAll(userAvatar, userName);
    }

    // MODIFIES: this
    // EFFECTS: sets title of sidebar to 'Time Journal'
    private void setTitle() {
        timeJournal = new Label("Time Journal");
        timeJournal.setStyle("-fx-font-size: 25px;");
        timeJournal.setAlignment(Pos.CENTER);
    }

    // REQUIRES: valid username and user avatar picture
    // MODIFIES: this
    // EFFECTS: sets the avatar and username to
    private void setAvatarAndName() {
        userAvatar = new ImageView(new Image(userInterface
                .getCurrentSession()
                .getUserAvatar(),
                105,
                105,
                false,
                true));
        userName = new Label(userInterface.getCurrentSession().getUserName());
        userName.setStyle("-fx-font-size: 22px;");
        userName.setWrapText(true);
    }

    // MODIFIES: this
    // EFFECTS: sets the colour of the sidebar background and anchors the dimensions to the top, left, bottom of stage
    public void setSideBarColorAndAnchors() {
        paneBackground = new Pane();
        paneBackground.setPrefWidth(200);
        paneBackground.setStyle("-fx-background-color:#383838");
        AnchorPane.setTopAnchor(paneBackground, 0.0);
        AnchorPane.setBottomAnchor(paneBackground, 0.0);
        AnchorPane.setLeftAnchor(paneBackground, 0.0);
    }

    // MODIFIES: this
    // EFFECTS: creates AnchorPane to keep sidebar locked to the left of the screen when stage is resized
    public void setSideBarAnchorPane() {
        AnchorPane anchorPane = new AnchorPane();
        Scene scene = new Scene(anchorPane);
        scene.getStylesheets().add("ui/style.css");
        anchorPane.getChildren().addAll(sideBarPane, quit);
    }

    // MODIFIES: this
    // EFFECTS: sets the text and gridpane constraints of the sidebar buttons, sets anchors of quit button
    public void setSideBarButtons() {
        homePageButton = new Button("Home");
        GridPane.setConstraints(homePageButton, 0, 2);

        newJournalEntryButton = new Button("Create Journal Entry");
        GridPane.setConstraints(newJournalEntryButton, 0, 3);

        viewJournalLogButton = new Button("Journal Entry Log");
        GridPane.setConstraints(viewJournalLogButton, 0, 4);

        viewCategoryListButton = new Button("Category List");
        GridPane.setConstraints(viewCategoryListButton, 0, 5);

        quit = new Button("Exit");
        AnchorPane.setBottomAnchor(quit, 14.0);
        AnchorPane.setLeftAnchor(quit, 10.0);
    }

    // REQUIRES: all sidebar buttons to be instantiated
    // EFFECTS: runs event listener methods on all sidebar buttons
    public void setSideBarButtonListeners() {
        setCreateJournalEntryButtonListener();
        setHomePageButtonListener();
        setJournalLogButtonListener();
        setCategoryListButtonListener();
        setCloseAndSaveListeners();
    }

    // MODIFIES: this
    // EFFECTS: sets button click listener for homepage button
    private void setHomePageButtonListener() {
        homePageButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.getHomePageScreen().renderHomePage();
        });
    }

    // MODIFIES: this
    // EFFECTS: sets button click listener for CreateJournalEntry button
    private void setCreateJournalEntryButtonListener() {
        newJournalEntryButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.getJournalEntryCreateScreen().renderJournalEntryCreateScreen();
        });
    }

    // MODIFIES: this
    // EFFECTS: sets button click listener for JournalLog button
    private void setJournalLogButtonListener() {
        viewJournalLogButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.getJournalLogScreen().renderJournalLogScreen();
        });
    }

    // MODIFIES: this
    // EFFECTS: sets button click listener for CategoryList button
    private void setCategoryListButtonListener() {
        viewCategoryListButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.getCategoryListScreen().renderCategoryListScreen();
        });
    }

    // MODIFIES: this
    // EFFECTS: sets button click listener for Exit button
    private void setCloseAndSaveListeners() {
        userInterface.getMainStage().setOnCloseRequest(e -> {
            e.consume();
            userInterface.getSavePromptPopup().renderSavePopup();
        });
        quit.setOnAction(e -> userInterface.getSavePromptPopup().renderSavePopup());
    }

    // Getters
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

    public Button getQuitButton() {
        return quit;
    }
}
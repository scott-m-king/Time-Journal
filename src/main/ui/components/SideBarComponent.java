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

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void renderSideBar() {
        sideBarPane = populateSideBar();
        setSideBarButtonListeners(sideBarPane);
        initializeSideBar(sideBarPane);
        userInterface.homePage(sideBarPane, homePageButton);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public Pane populateSideBar() {
        setSideBarColorAndAnchors();
        createMenuItems();
        paneBackground.getChildren().add(menuItems);
        return paneBackground;
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
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
                viewCategoryListButton);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void setSideBarTitleAndUser() {
        setAvatarAndName();
        setTitle();
        setVBox();
        GridPane.setConstraints(timeJournal, 0, 0);
        GridPane.setHalignment(timeJournal, HPos.CENTER);
        GridPane.setConstraints(vbox, 0, 1);
        GridPane.setHalignment(vbox, HPos.CENTER);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void setVBox() {
        vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMinWidth(180);
        vbox.setMaxWidth(180);
        vbox.getChildren().addAll(userAvatar, userName);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void setTitle() {
        timeJournal = new Label("Time Journal");
        timeJournal.setStyle("-fx-font-size: 25px;");
        timeJournal.setAlignment(Pos.CENTER);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
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

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void setSideBarColorAndAnchors() {
        paneBackground = new Pane();
        paneBackground.setPrefWidth(200);
        paneBackground.setStyle("-fx-background-color:#383838");
        AnchorPane.setTopAnchor(paneBackground, 0.0);
        AnchorPane.setBottomAnchor(paneBackground, 0.0);
        AnchorPane.setLeftAnchor(paneBackground, 0.0);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void initializeSideBar(Pane sideBar) {
        AnchorPane anchorPane = new AnchorPane();
        Scene scene = new Scene(anchorPane);
        scene.getStylesheets().add("ui/style.css");
        anchorPane.getChildren().addAll(sideBar, quit);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
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

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void setSideBarButtonListeners(Pane sideBar) {
        setCreateJournalEntryButtonListener();
        setHomePageButtonListener(sideBar);
        setJournalLogButtonListener();
        setCategoryListButtonListener();
        setCloseAndSaveListeners();
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void setHomePageButtonListener(Pane sideBar) {
        homePageButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.homePage(sideBar, homePageButton);
        });
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void setCreateJournalEntryButtonListener() {
        newJournalEntryButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.getJournalEntryCreateScreen().renderJournalEntryCreateScreen();
        });
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void setJournalLogButtonListener() {
        viewJournalLogButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.getJournalLogScreen().renderJournalLogScreen();
        });
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    private void setCategoryListButtonListener() {
        viewCategoryListButton.setOnAction(e -> {
            userInterface.removeListeners();
            userInterface.clearButtonColours();
            userInterface.getCategoryListScreen().renderCategoryListScreen();
        });
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
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
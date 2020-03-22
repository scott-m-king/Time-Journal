package ui.screens;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import ui.UserInterface;

// Represents a home screen where the user is directed to after logging in
public class HomePageScreen extends Screen {
    private final UserInterface userInterface;
    private PieChart chart;
    private Label hoverLabel;
    private Label text;
    private Text hoverHelper;
    private Pane pane;

    public HomePageScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // MODIFIES: this
    // EFFECTS: generates category chart and runs methods needed to render this screen
    public void renderHomePage() {
        chart = userInterface.getCategoryChartComponent().generateCategoryChart();
        initializeFinalPane();
        initializeScreen(pane, userInterface.getMainStage());
    }

    // MODIFIES: this
    // EFFECTS: renders final pane to pass into scene
    @Override
    protected void initializeFinalPane() {
        Text title = setHomePageTitle();
        pane = createPane(
                userInterface.getSideBarComponent().getSideBarPane(),
                userInterface.getSideBarComponent().getHomePageButton(),
                title
        );
    }

    // EFFECTS: returns text for home page title with anchors
    public Text setHomePageTitle() {
        Text title = new Text();
        title.setFont(new Font(UserInterface.TITLE_FONT_SIZE));
        title.setText("Home");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);
        return title;
    }

    // REQUIRES: valid sidebar, homepage button, and title
    // MODIFIES: this
    // EFFECTS: populates anchorpane with all page elements and returns result
    private AnchorPane createPane(Pane sideBar, Button homePageButton, Text title) {
        AnchorPane pane = new AnchorPane();
        setHoverLabelForChart();
        setHoverToolTip();
        if (userInterface.getCurrentSession().getJournalLog().getSize() == 0) {
            setLabelForNoEntries();
            pane.getChildren().addAll(sideBar, userInterface.getSideBarComponent().getQuitButton(), title, text);
        } else {
            pane.getChildren().addAll(
                    sideBar,
                    userInterface.getSideBarComponent().getQuitButton(),
                    title,
                    chart,
                    hoverLabel,
                    hoverHelper
            );
        }
        homePageButton.setStyle("-fx-background-color:#787878");
        return pane;
    }

    // MODIFIES: this
    // EFFECTS: sets label for when user has no journal entries
    private void setLabelForNoEntries() {
        text = new Label("You currently have no journal entries. "
                + "A chart with your category data will be displayed here after you enter your first journal entry. ");
        AnchorPane.setTopAnchor(text, 30.0);
        AnchorPane.setBottomAnchor(text, 30.0);
        AnchorPane.setLeftAnchor(text, 230.0);
        AnchorPane.setRightAnchor(text, 30.0);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrapText(true);
        text.setStyle("-fx-text-fill:#585858;");
    }

    // MODIFIES: this
    // EFFECTS: renders a label for when user hovers over a category
    private void setHoverLabelForChart() {
        hoverLabel = userInterface.getCategoryChartComponent().setHoverEffects(chart);
        AnchorPane.setTopAnchor(hoverLabel, 40.0);
        AnchorPane.setRightAnchor(hoverLabel, 30.0);
    }

    // MODIFIES: this
    // EFFECTS: places a tooltip at bottom right of screen prompting to hover over a category for more details
    private void setHoverToolTip() {
        hoverHelper = new Text("Hover over a category for more details");
        hoverHelper.setTextAlignment(TextAlignment.CENTER);
        hoverHelper.setStyle("-fx-font-size: 16;");
        AnchorPane.setBottomAnchor(hoverHelper, 30.0);
        AnchorPane.setRightAnchor(hoverHelper, 30.0);
        AnchorPane.setLeftAnchor(hoverHelper, 230.0);
    }
}
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

// TODO: refactor to match other screen pages

public class HomePageScreen extends Screen {
    private final UserInterface userInterface;
    private PieChart chart;
    private Label hoverLabel;
    private Text hoverHelper;
    private Pane pane;

    public HomePageScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    protected void initializeFinalPane() {
        pane = new AnchorPane();
    }

    public void homePage(Pane sideBar, Button homePageButton) {
        Text title = setHomePageTitle();
        chart = userInterface.getCategoryChartComponent().generateCategoryChart();
        pane = createPane(sideBar, homePageButton, title);
        initializeScreen(pane, userInterface.getMainStage());
    }

    private AnchorPane createPane(Pane sideBar, Button homePageButton, Text title) {
        AnchorPane pane = new AnchorPane();
        setHoverLabel();
        setHoverHelper();
        pane.getChildren().addAll(sideBar, userInterface.getQuitButton(), title, chart, hoverLabel, hoverHelper);
        homePageButton.setStyle("-fx-background-color:#787878");
        return pane;
    }

    private void setHoverLabel() {
        hoverLabel = userInterface.getCategoryChartComponent().setHoverEffects(chart);
        AnchorPane.setTopAnchor(hoverLabel, 40.0);
        AnchorPane.setRightAnchor(hoverLabel, 30.0);
    }

    private void setHoverHelper() {
        hoverHelper = new Text("Hover over a category for more details");
        hoverHelper.setTextAlignment(TextAlignment.CENTER);
        hoverHelper.setStyle("-fx-font-size: 16;");
        AnchorPane.setBottomAnchor(hoverHelper, 15.0);
        AnchorPane.setRightAnchor(hoverHelper, 30.0);
        AnchorPane.setLeftAnchor(hoverHelper, 230.0);
    }

    public Text setHomePageTitle() {
        Text title = new Text();
        title.setFont(new Font(UserInterface.TITLE_FONT_SIZE));
        title.setText("Home");
        title.setStyle("-fx-text-fill: #383838;");
        AnchorPane.setLeftAnchor(title, 230.0);
        AnchorPane.setTopAnchor(title, 30.0);
        return title;
    }
}
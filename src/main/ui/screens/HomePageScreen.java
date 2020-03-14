package ui.screens;

import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ui.UserInterface;

public class HomePageScreen extends Screen {
    private final UserInterface userInterface;
    private Pane pane;

    public HomePageScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    protected void initializeFinalPane() {
        pane = new AnchorPane();
    }

    // https://docs.oracle.com/javafx/2/charts/pie-chart.htm

    public void homePage(Pane sideBar, Button homePageButton) {
        Text title = setHomePageTitle();
        PieChart chart = userInterface.generateCategoryChart();
        pane = createPane(sideBar, homePageButton, title, chart);
        initializeScreen(pane, userInterface.getMainStage());
    }

    private AnchorPane createPane(Pane sideBar, Button homePageButton, Text title, PieChart chart) {
        AnchorPane pane = new AnchorPane();
        pane.getChildren().addAll(sideBar, userInterface.getQuitButton(), title, chart);
        homePageButton.setStyle("-fx-background-color:#787878");
        return pane;
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
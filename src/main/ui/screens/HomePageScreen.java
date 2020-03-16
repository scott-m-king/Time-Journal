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
    private Label text;
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
        setHoverLabelForChart();
        setHoverToolTip();
        if (userInterface.getCurrentSession().getJournalLog().getSize() == 0) {
            setLabelIfNoEntries();
            pane.getChildren().addAll(sideBar, userInterface.getQuitButton(), title, text);
        } else {
            pane.getChildren().addAll(sideBar, userInterface.getQuitButton(), title, chart, hoverLabel, hoverHelper);
        }
        homePageButton.setStyle("-fx-background-color:#787878");
        return pane;
    }

    private void setLabelIfNoEntries() {
        text = new Label("You currently have no journal entries. "
                + "A chart with your category data will be displayed here after you enter your first journal entry. "
                + "\n\nClick here to enter your first journal entry.");
        AnchorPane.setTopAnchor(text, 30.0);
        AnchorPane.setBottomAnchor(text, 30.0);
        AnchorPane.setLeftAnchor(text, 230.0);
        AnchorPane.setRightAnchor(text, 30.0);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrapText(true);
        text.setStyle("-fx-text-fill:#585858;");

//        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
//                text.setTranslateX(((userInterface.getMainStage().getWidth() - 200) / 2));
//        userInterface.getMainStage().widthProperty().addListener(stageSizeListener);
//        userInterface.getMainStage().heightProperty().addListener(stageSizeListener);
    }

    private void setHoverLabelForChart() {
        hoverLabel = userInterface.getCategoryChartComponent().setHoverEffects(chart);
        AnchorPane.setTopAnchor(hoverLabel, 40.0);
        AnchorPane.setRightAnchor(hoverLabel, 30.0);
    }

    private void setHoverToolTip() {
        hoverHelper = new Text("Hover over a category for more details");
        hoverHelper.setTextAlignment(TextAlignment.CENTER);
        hoverHelper.setStyle("-fx-font-size: 16;");
        AnchorPane.setBottomAnchor(hoverHelper, 30.0);
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
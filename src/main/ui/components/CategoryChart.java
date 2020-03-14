package ui.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;

public class CategoryChart {
    public CategoryChart() {
    }

    public PieChart generateCategoryChart() {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Grapefruit", 13),
                        new PieChart.Data("Oranges", 25),
                        new PieChart.Data("Plums", 10),
                        new PieChart.Data("Pears", 22),
                        new PieChart.Data("Apples", 30));
        PieChart chart = new PieChart(pieChartData);
        chart.setLegendSide(Side.RIGHT);
        AnchorPane.setLeftAnchor(chart, 230.0);
        AnchorPane.setRightAnchor(chart, 30.0);
        AnchorPane.setTopAnchor(chart, 95.0);
        AnchorPane.setBottomAnchor(chart, 30.0);
        return chart;
    }
}
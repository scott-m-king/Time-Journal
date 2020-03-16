package ui.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.CategoryList;
import ui.UserInterface;

// https://docs.oracle.com/javafx/2/charts/pie-chart.htm

public class CategoryChartComponent {
    private UserInterface userInterface;

    public CategoryChartComponent(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public PieChart generateCategoryChart() {
        CategoryList categoryList = userInterface.getCurrentSession().getCategoryList();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (int i = 0; i < categoryList.getSize(); i++) {
            pieChartData.add(new PieChart.Data(categoryList.get(i).getName(), categoryList.get(i).getDuration()));
        }

        PieChart chart = new PieChart(pieChartData);
        setPieChartPosition(chart);
        return chart;
    }

    private void setPieChartPosition(PieChart chart) {
        chart.setLegendSide(Side.RIGHT);
        AnchorPane.setLeftAnchor(chart, 230.0);
        AnchorPane.setRightAnchor(chart, 30.0);
        AnchorPane.setTopAnchor(chart, 95.0);
        AnchorPane.setBottomAnchor(chart, 30.0);
    }

    public Label setHoverEffects(PieChart chart) {
        final Label caption = new Label("");
        caption.setStyle("-fx-font: 22 arial; -fx-text-fill: #383838 ");
        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED,
                    event -> {
                        setHoverLabel(caption, data);
                    });
        }
        return caption;
    }

    private void setHoverLabel(Label caption, PieChart.Data data) {
        int currentCategoryDuration = userInterface.getCurrentSession().getTotalCategoryDuration();
        double percentage = Math.round(data.getPieValue() / currentCategoryDuration * 100);
        int resultDuration = (int) data.getPieValue();
        int resultPercentage = (int) percentage;
        caption.setText(resultDuration
                + " minutes spent on "
                + data.getName()
                + ". "
                + resultPercentage
                + "% overall. ");
    }
}
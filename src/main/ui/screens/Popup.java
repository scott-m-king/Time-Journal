package ui.screens;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class Popup {

    protected abstract Pane initializeFinalPane();

    protected Stage createStandardStage() {
        Stage stage = new Stage();
        stage.setWidth(400);
        stage.setHeight(250);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

    protected Stage createCustomDimensionStage(int width, int height, StageStyle style, Modality modality) {
        Stage stage = new Stage();
        stage.setWidth(width);
        stage.setHeight(height);
        stage.initStyle(style);
        stage.initModality(modality);
        return stage;
    }

    protected void initializeScreen(Pane pane, Stage stage) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
    }
}

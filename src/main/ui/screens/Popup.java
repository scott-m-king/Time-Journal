package ui.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.UserInterface;

public abstract class Popup {
    public static final int STANDARD_POPUP_WIDTH = 400;
    public static final int STANDARD_POPUP_HEIGHT = 250;
    public static final String EDIT_JOURNAL_ENTRY = "editJournalEntry";
    public static final String CREATE_JOURNAL_ENTRY = "createJournalEntry";
    public static final String EDIT_CATEGORY = "editCategoryScreen";
    public static final String CREATE_CATEGORY = "createCategoryScreen";

    protected abstract void initializeFinalPane();

    protected Stage createPopupStage(int width, int height) {
        Stage stage = new Stage();
        stage.setWidth(width);
        stage.setHeight(height);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

    protected void initializeScreen(Pane pane, Stage stage) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
        stage.show();
    }

    // THIS STAYS HERE!! called by popups that edit anything
    protected HBox makeFormButtons(Stage stage, String cameFrom, UserInterface ui) {
        HBox hbox = new HBox();
        hbox.setSpacing(10.0);

        Button submit = new Button("Submit");
        submit.setStyle("-fx-min-width: 100; -fx-min-height:35;");

        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-min-width: 100; -fx-min-height:35;");

        hbox.getChildren().addAll(submit, cancel);
        hbox.setAlignment(Pos.CENTER);

        setFormButtonListeners(stage, cameFrom, submit, cancel, ui);

        return hbox;
    }

    public void setFormButtonListeners(Stage stage, String cameFrom, Button submit, Button cancel, UserInterface ui) {
        cancel.setOnAction(e -> stage.close());
        switch (cameFrom) {
            case CREATE_CATEGORY:
                submit.setOnAction(e -> ui.getCreateCategoryPopup().createNewCategory());
                break;
            case EDIT_CATEGORY:
                submit.setOnAction(e -> ui.getEditCategoryPopup().editCategory());
                break;
            case EDIT_JOURNAL_ENTRY:
                submit.setOnAction(e -> ui.getJournalEntryEditPopup().editJournalEntry());
                break;
        }
    }
}

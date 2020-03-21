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

import java.applet.Applet;
import java.applet.AudioClip;

public abstract class Popup {
    Button submit;
    Button cancel;

    public static final int STANDARD_POPUP_WIDTH = 400;
    public static final int STANDARD_POPUP_HEIGHT = 250;
    public static final String EDIT_JOURNAL_ENTRY = "editJournalEntry";
    public static final String EDIT_CATEGORY = "editCategoryScreen";
    public static final String CREATE_CATEGORY = "createCategoryScreen";
    public static final String SUCCESS_SOUND = "/ui/resources/success.wav";
    public static final String FAILURE_SOUND = "/ui/resources/delete.wav";

    protected abstract void initializeFinalPane();

    // REQUIRES: screen dimensions width and height that are less than user's screen resolution
    // EFFECTS: creates a stage with custom dimensions based on width and height parameters
    protected Stage createPopupStage(int width, int height) {
        Stage stage = new Stage();
        stage.setWidth(width);
        stage.setHeight(height);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

    // REQUIRES: a pane with at least one child node and an active stage
    // EFFECTS: loads pane onto stage and displays stage
    protected void initializeScreen(Pane pane, Stage stage) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
        stage.show();
    }

    // REQUIRES: an active stage, non-null string denoting which page used this method, active UserInterface
    // EFFECTS: returns a generic hbox pane containing submit and cancel buttons (for popup forms)
    protected HBox makeFormButtons(Stage stage, String cameFrom, UserInterface ui) {
        HBox hbox = new HBox();
        hbox.setSpacing(10.0);

        submit = new Button("Submit");
        submit.setStyle("-fx-min-width: 100; -fx-min-height:35;");

        cancel = new Button("Cancel");
        cancel.setStyle("-fx-min-width: 100; -fx-min-height:35;");

        hbox.getChildren().addAll(submit, cancel);
        hbox.setAlignment(Pos.CENTER);

        setFormButtonListeners(stage, cameFrom, ui);

        return hbox;
    }

    // REQUIRES: active stage, non-null cameFrom string, active UserInterface
    // EFFECTS: sets button listeners and actions depending on class that called this method
    public void setFormButtonListeners(Stage stage, String cameFrom, UserInterface ui) {
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

    // EFFECTS: plays a success sound once
    public void playSuccessSound() {
        AudioClip successSound = Applet.newAudioClip(getClass().getResource(SUCCESS_SOUND));
        successSound.play();
    }
}

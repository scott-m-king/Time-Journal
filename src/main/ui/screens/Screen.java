package ui.screens;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ui.UserInterface;

import java.applet.Applet;
import java.applet.AudioClip;

import static ui.screens.Popup.FAILURE_SOUND;
import static ui.screens.Popup.SUCCESS_SOUND;

// Represents a generic 'screen' class which subclasses extend to access shared functionality
public abstract class Screen {
    protected Button edit;
    protected Button delete;
    protected Button create;

    public static final String JOURNAL_LOG = "journalLog";
    public static final String CATEGORY_LIST = "categoryList";

    protected abstract void initializeFinalPane();

    // REQUIRES: pane with at least one child node, active stage
    // EFFECTS: creates loads pane to new scene, adds CSS stylesheet, sets scene to stage
    protected void initializeScreen(Pane pane, Stage stage) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
    }

    // REQUIRES: non-null string denoting which page used this method, active UserInterface
    // MODIFIES: this
    // EFFECTS: returns a generic hbox pane containing submit and cancel buttons (for popup forms)
    protected HBox makeFormButtons(String cameFrom, UserInterface ui) {
        HBox hbox = new HBox();
        hbox.setSpacing(15.0);

        edit = new Button("Edit");
        edit.setStyle("-fx-background-color: #c7c7c7; -fx-min-width: 100;");

        delete = new Button("Delete");
        delete.setStyle("-fx-background-color: #c7c7c7; -fx-min-width: 100;");

        create = new Button("Create");
        create.setStyle("-fx-background-color: #585858; -fx-min-width: 100;");

        hbox.getChildren().addAll(edit, delete, create);

        setFormButtonListeners(cameFrom, ui);

        return hbox;
    }

    // REQUIRES: active stage, non-null cameFrom string, active UserInterface
    // EFFECTS: sets button listeners and actions depending on class that called this method
    public void setFormButtonListeners(String cameFrom, UserInterface ui) {
        switch (cameFrom) {
            case JOURNAL_LOG:
                edit.setOnAction(e -> ui.getJournalLogScreen().editButtonAction());
                delete.setOnAction(e -> ui.getJournalLogScreen().deleteButtonAction());
                create.setOnAction(e -> ui.getJournalLogScreen().createButtonAction());
                break;
            case CATEGORY_LIST:
                edit.setOnAction(e -> ui.getCategoryListScreen().editButtonAction());
                delete.setOnAction(e -> ui.getCategoryListScreen().deleteButtonAction());
                create.setOnAction(e -> ui.getCategoryListScreen().createButtonAction());
                break;
        }
    }

    // EFFECTS: plays a success sound once
    public void playSuccessSound() {
        AudioClip successSound = Applet.newAudioClip(getClass().getResource(SUCCESS_SOUND));
        successSound.play();
    }

    // EFFECTS: plays a delete sound once
    public void playDeleteSound() {
        AudioClip failSound = Applet.newAudioClip(getClass().getResource(FAILURE_SOUND));
        failSound.play();
    }
}

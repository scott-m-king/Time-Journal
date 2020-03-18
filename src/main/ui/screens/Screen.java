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

public abstract class Screen {
    protected Button edit;
    protected Button delete;
    protected Button create;

    public static final String JOURNAL_LOG = "journalLog";
    public static final String CATEGORY_LIST = "categoryList";

    protected abstract void initializeFinalPane();

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    protected void initializeScreen(Pane pane, Stage stage) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("ui/style.css");
        stage.setScene(scene);
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
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

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
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

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void playSuccessSound() {
        AudioClip successSound = Applet.newAudioClip(getClass().getResource(SUCCESS_SOUND));
        successSound.play();
    }

    // TODO
    // MODIFIES:
    // REQUIRES:
    // EFFECTS:
    public void playDeleteSound() {
        AudioClip failSound = Applet.newAudioClip(getClass().getResource(FAILURE_SOUND));
        failSound.play();
    }
}

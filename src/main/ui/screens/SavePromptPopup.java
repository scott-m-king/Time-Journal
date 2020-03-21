package ui.screens;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.UserInterface;

import java.io.IOException;

import static model.UserSession.USERS_LOCATION;
import static model.UserSession.USER_SAVE_FILE;

public class SavePromptPopup extends Popup {
    private final UserInterface userInterface;
    private Button yes;
    private Button no;
    private Button cancel;
    private HBox buttonPane;
    private Pane pane;
    private Stage stage;

    public SavePromptPopup(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    // MODIFIES: this
    // EFFECTS: runs methods needed to render save prompt popup
    public void renderSavePopup() {
        stage = createPopupStage(300, 100);
        initializeFinalPane();
        initializeScreen();
    }

    // MODIFIES: this
    // EFFECTS: creates and aligns final gridpane to display label and buttons
    @Override
    protected void initializeFinalPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        Text text = new Text("Would you like to save your file?");
        GridPane.setConstraints(text, 0, 0);

        setButtonLayout();
        gridPane.getChildren().addAll(text, buttonPane);
        pane = gridPane;
    }

    // MODIFIES: this
    // EFFECTS: loads final pane onto stage and shows stage
    private void initializeScreen() {
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        userInterface.setMiddle(stage);
        stage.show();
    }

    // MODIFIES: this
    // EFFECTS: creates button and sets hbox layout
    private void setButtonLayout() {
        buttonPane = new HBox();
        yes = new Button("Yes");
        no = new Button("No");
        cancel = new Button("Cancel");
        buttonPane.getChildren().addAll(yes, no, cancel);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setSpacing(5.0);
        GridPane.setConstraints(buttonPane, 0, 1);
        saveButtonListeners();
    }

    // MODIFIES: this
    // EFFECTS: sets button listeners for yes/no/cancel, defines actions if triggered
    public void saveButtonListeners() {
        yes.setOnAction(e -> {
            userInterface.getCurrentSession().saveEntries(USERS_LOCATION, USER_SAVE_FILE);
            Platform.exit();
            System.exit(0);
        });

        no.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        cancel.setOnAction(e -> stage.close());
    }
}
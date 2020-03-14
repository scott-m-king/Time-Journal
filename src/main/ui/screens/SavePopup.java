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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.UserInterface;

public class SavePopup extends Popup {
    private final UserInterface userInterface;
    private Button yes;
    private Button no;
    private Button cancel;
    private HBox choice;
    private Pane pane;
    private Stage saveStage;

    public SavePopup(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void renderSavePopup() {
        saveStage = createCustomDimensionStage(
                300,
                100,
                StageStyle.UNDECORATED,
                Modality.APPLICATION_MODAL);
        initializeFinalPane();
        initializeScreen();
    }

    private void initializeScreen() {
        Scene scene = new Scene(pane);
        saveStage.setScene(scene);
        userInterface.setMiddle(saveStage);
        saveStage.show();
    }

    @Override
    protected void initializeFinalPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        Text text = new Text("Would you like to save your file?");
        GridPane.setConstraints(text, 0, 0);

        setButtonLayout();
        gridPane.getChildren().addAll(text, choice);
        pane = gridPane;
    }

    private void setButtonLayout() {
        choice = new HBox();
        yes = new Button("Yes");
        no = new Button("No");
        cancel = new Button("Cancel");
        choice.getChildren().addAll(yes, no, cancel);
        choice.setAlignment(Pos.CENTER);
        choice.setSpacing(5.0);
        GridPane.setConstraints(choice, 0, 1);
        saveButtonListeners();
    }

    public void saveButtonListeners() {
        yes.setOnAction(e -> {
            userInterface.getSession().saveEntries();
            userInterface.getSession().endSession();
            Platform.exit();
            System.exit(0);
        });

        no.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        cancel.setOnAction(e -> saveStage.close());
    }
}
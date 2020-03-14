package ui.screens;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.UserInterface;

public class SaveScreen {
    private final UserInterface userInterface;

    public SaveScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void savePrompt() {
        Stage saveStage = new Stage();
        saveStage.setTitle("Exit");
        saveStage.setWidth(300);
        saveStage.setHeight(100);
        saveStage.initStyle(StageStyle.UNDECORATED);
        saveStage.initModality(Modality.APPLICATION_MODAL);

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setVgap(10);
        pane.setAlignment(Pos.CENTER);

        Text text = new Text("Would you like to save your file?");
        GridPane.setConstraints(text, 0, 0);

        HBox choice = new HBox();
        Button yes = new Button("Yes");
        Button no = new Button("No");
        Button cancel = new Button("Cancel");

        choice.getChildren().addAll(yes, no, cancel);
        choice.setAlignment(Pos.CENTER);
        choice.setSpacing(5.0);

        GridPane.setConstraints(choice, 0, 1);
        pane.getChildren().addAll(text, choice);

        Scene scene = new Scene(pane);
        saveStage.setScene(scene);

        saveButtonListeners(yes, no, cancel, saveStage);

        userInterface.setMiddle(saveStage);
        saveStage.show();
    }

    public void saveButtonListeners(Button yes, Button no, Button cancel, Stage savePromptStage) {
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

        cancel.setOnAction(e -> savePromptStage.close());
    }
}
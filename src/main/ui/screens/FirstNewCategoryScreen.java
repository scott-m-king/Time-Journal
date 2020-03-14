package ui.screens;

import exceptions.CategoryExistsException;
import exceptions.NullEntryException;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import ui.UserInterface;

public class FirstNewCategoryScreen {
    private final UserInterface userInterface;

    public FirstNewCategoryScreen(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void firstNewCategory() {
        Label nameLabel = setScreenLabel();
        TextField categoryName = setTextField();
        Button startJournal = setStartButton(categoryName);
        initializeScreen(nameLabel, categoryName, startJournal);
    }

    public Label setScreenLabel() {
        Label nameLabel = new Label("Let's start with creating your first category.\n"
                + "Enter a name for your category below:");
        nameLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: #383838;");
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        return nameLabel;
    }

    public TextField setTextField() {
        TextField categoryName = new TextField();
        categoryName.setMaxWidth(300);
        categoryName.setStyle("-fx-font-size: 20px;");
        categoryName.setAlignment(Pos.CENTER);
        return categoryName;
    }

    public Button setStartButton(TextField categoryName) {
        Button startJournal = new Button("Get Started");
        startJournal.setAlignment(Pos.CENTER);
        setButtonHandler(startJournal, categoryName);
        return startJournal;
    }

    public void initializeScreen(Label nameLabel, TextField categoryName, Button startJournal) {
        VBox vbox = new VBox();
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(nameLabel, categoryName, startJournal);
        userInterface.initializeScene(vbox, userInterface.getMainStage());
    }

    public void setButtonHandler(Button startJournal, TextField categoryName) {
        startJournal.setOnAction(e -> {
            try {
                userInterface.getSession().createNewCategory(categoryName.getText());
                userInterface.makeSideBar();
            } catch (NullEntryException e1) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("You must enter a name for your category.");
                a.show();
            } catch (CategoryExistsException exception) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("You happened to enter the one category that already exists... try again.");
                a.show();
            }
        });
    }
}
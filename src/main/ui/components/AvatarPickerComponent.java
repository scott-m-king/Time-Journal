package ui.components;

// Icons made by https://www.flaticon.com/authors/freepik from https://www.flaticon.com/

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class AvatarPickerComponent {
    private ImageView boyImage;
    private ImageView boy1Image;
    private ImageView girlImage;
    private ImageView girl1Image;
    private ImageView manImage;
    private ImageView man1Image;
    private ImageView man2Image;
    private ImageView woman1Image;
    private ImageView woman2Image;
    private ObservableList<ImageView> avatarObservableList;
    private ImageView selectedAvatarImageView;
    private GridPane grid;

    public static final int IMAGE_DIMENSION = 125;

    public AvatarPickerComponent() {
    }

    public GridPane renderAvatarPicker() {
        grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setAlignment(Pos.CENTER);
        setImagesFromResources();
        setImageConstraints();
        makeAvatarObservableList();
        updateSelectedAvatar();
        grid.getChildren().addAll(avatarObservableList);
        return grid;
    }

    private void setImagesFromResources() {
        boyImage = new ImageView(new Image("File:data/resources/boy.png",
                IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        boy1Image = new ImageView(new Image("File:data/resources/boy-1.png",
                IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        girlImage = new ImageView(new Image("File:data/resources/girl.png",
                IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        girl1Image = new ImageView(new Image("File:data/resources/girl-1.png",
                IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        manImage = new ImageView(new Image("File:data/resources/man.png",
                IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        man1Image = new ImageView(new Image("File:data/resources/man-1.png",
                IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        man2Image = new ImageView(new Image("File:data/resources/man-2.png",
                IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        woman1Image = new ImageView(new Image("File:data/resources/man-3.png",
                IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        woman2Image = new ImageView(new Image("File:data/resources/man-4.png",
                IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
    }

    private void setImageConstraints() {
        GridPane.setConstraints(boyImage, 0, 0);
        GridPane.setConstraints(boy1Image, 1, 0);
        GridPane.setConstraints(girlImage, 2, 0);
        GridPane.setConstraints(girl1Image, 0, 1);
        GridPane.setConstraints(manImage, 1, 1);
        GridPane.setConstraints(man1Image, 2, 1);
        GridPane.setConstraints(man2Image, 0, 2);
        GridPane.setConstraints(woman1Image, 1, 2);
        GridPane.setConstraints(woman2Image, 2, 2);
    }

    public void makeAvatarObservableList() {
        avatarObservableList = FXCollections.observableArrayList();
        avatarObservableList.add(boyImage);
        avatarObservableList.add(boy1Image);
        avatarObservableList.add(girlImage);
        avatarObservableList.add(girl1Image);
        avatarObservableList.add(manImage);
        avatarObservableList.add(man1Image);
        avatarObservableList.add(man2Image);
        avatarObservableList.add(woman1Image);
        avatarObservableList.add(woman2Image);
    }

    private void updateSelectedAvatar() {
        for (int i = 0; i < avatarObservableList.size(); i++) {
            ImageView imageView = avatarObservableList.get(i);
            int finalI = i;
            imageView.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    event -> imageView.setStyle("-fx-cursor: hand;"));
            imageView.addEventHandler(MouseEvent.MOUSE_EXITED,
                    event -> imageView.setStyle("-fx-cursor: default;"));
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                selectedAvatarImageView = imageView;
                grid.getChildren().remove(imageView);
                setSelectedImage(finalI);
                grid.getChildren().add(selectedAvatarImageView);
            });
        }
    }

    public void setSelectedImage(int i) {
        System.out.println(i);
        int col = i % 3;
        int row = i / 3;
        System.out.println(col + ", " + row);
        GridPane.setConstraints(selectedAvatarImageView, col, row);
    }

    public Image getSelectedAvatarImage() {
        return selectedAvatarImageView.getImage();
    }
}

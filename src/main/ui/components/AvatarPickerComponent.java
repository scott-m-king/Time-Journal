package ui.components;

// Icons made by Freepik (https://www.flaticon.com/authors/freepik) from www.flaticon.com

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AvatarPickerComponent {
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;
    private ImageView image6;
    private ImageView image7;
    private ImageView image8;
    private ImageView image9;
    private ObservableList<ImageView> avatarObservableList;
    private ImageView selectedAvatarImageView;
    private GridPane grid;
    private ImageView previouslySelectedImageViewUnBordered;
    private ImageView previouslySelectedImageViewBordered;
    private int previouslySelectedPosition;
    private String selectedAvatarURL;

    public static final int IMAGE_DIMENSION = 125;
    public static final String IMAGE_1 = "File:data/resources/dog.png";
    public static final String IMAGE_2 = "File:data/resources/bee.png";
    public static final String IMAGE_3 = "File:data/resources/cloud.png";
    public static final String IMAGE_4 = "File:data/resources/coffee.png";
    public static final String IMAGE_5 = "File:data/resources/balloon.png";
    public static final String IMAGE_6 = "File:data/resources/ladybug.png";
    public static final String IMAGE_7 = "File:data/resources/pigeon.png";
    public static final String IMAGE_8 = "File:data/resources/sandwich.png";
    public static final String IMAGE_9 = "File:data/resources/sun.png";

    public AvatarPickerComponent() {
    }

    public GridPane renderAvatarPicker() {
        setImagesFromResources();
        makeAvatarObservableList();
        setImagePositionsInGrid();
        updateSelectedAvatar();
        populateGridPane();
        return grid;
    }

    private void populateGridPane() {
        grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setAlignment(Pos.CENTER);
        grid.getChildren().addAll(avatarObservableList);
    }

    private void setImagesFromResources() {
        image1 = new ImageView(new Image(IMAGE_1, IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        image2 = new ImageView(new Image(IMAGE_2, IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        image3 = new ImageView(new Image(IMAGE_3, IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        image4 = new ImageView(new Image(IMAGE_4, IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        image5 = new ImageView(new Image(IMAGE_5, IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        image6 = new ImageView(new Image(IMAGE_6, IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        image7 = new ImageView(new Image(IMAGE_7, IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        image8 = new ImageView(new Image(IMAGE_8, IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
        image9 = new ImageView(new Image(IMAGE_9, IMAGE_DIMENSION, IMAGE_DIMENSION, false, true));
    }

    public void makeAvatarObservableList() {
        avatarObservableList = FXCollections.observableArrayList();
        avatarObservableList.add(image1);
        avatarObservableList.add(image2);
        avatarObservableList.add(image3);
        avatarObservableList.add(image4);
        avatarObservableList.add(image5);
        avatarObservableList.add(image6);
        avatarObservableList.add(image7);
        avatarObservableList.add(image8);
        avatarObservableList.add(image9);
    }

    private void setImagePositionsInGrid() {
        List<String> listOfURLs = listOfImageURLs();
        for (int i = 0; i < avatarObservableList.size(); i++) {
            GridPane.setConstraints(avatarObservableList.get(i), i % 3, i / 3);
            avatarObservableList.get(i).setId(listOfURLs.get(i));
        }
    }

    private List<String> listOfImageURLs() {
        List<String> imageURLs = new ArrayList<>();
        imageURLs.add(IMAGE_1);
        imageURLs.add(IMAGE_2);
        imageURLs.add(IMAGE_3);
        imageURLs.add(IMAGE_4);
        imageURLs.add(IMAGE_5);
        imageURLs.add(IMAGE_6);
        imageURLs.add(IMAGE_7);
        imageURLs.add(IMAGE_8);
        imageURLs.add(IMAGE_9);
        return imageURLs;
    }

    // https://stackoverflow.com/questions/20489908/border-radius-and-shadow-on-imageview
    private void updateSelectedAvatar() {
        for (int i = 0; i < avatarObservableList.size(); i++) {
            ImageView imageView = avatarObservableList.get(i);
            int finalI = i;
            imageView.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> imageView.setStyle("-fx-cursor: hand; "
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)"));
            imageView.addEventHandler(MouseEvent.MOUSE_EXITED,
                    event -> imageView.setStyle("-fx-cursor: default;"));
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                selectedAvatarImageView = new ImageView(imageView.getImage());
                selectedAvatarURL = imageView.getId();
                makeSelectionVFX(imageView, finalI);
            });
        }
    }

    private void makeSelectionVFX(ImageView imageView, int finalI) {
        putBackNoBorderImage();
        grid.getChildren().remove(imageView);
        putInSelectedImage(finalI);
        storeNoBorderImage(imageView, selectedAvatarImageView, finalI);
    }

    public void putBackNoBorderImage() {
        if (previouslySelectedImageViewUnBordered != null) {
            int col = previouslySelectedPosition % 3;
            int row = previouslySelectedPosition / 3;
            grid.getChildren().remove(selectedAvatarImageView);
            GridPane.setConstraints(previouslySelectedImageViewUnBordered, col, row);
            grid.getChildren().remove(previouslySelectedImageViewBordered);
            grid.getChildren().add(previouslySelectedImageViewUnBordered);
        }
    }

    public void putInSelectedImage(int i) {
        int r = new Random().nextInt(255);
        int g = new Random().nextInt(255);
        int b = new Random().nextInt(255);
        int col = i % 3;
        int row = i / 3;
        selectedAvatarImageView.setStyle(
                "-fx-effect: dropshadow(three-pass-box, rgba(" + r + "," + g + "," + b + ",0.8), 45, 0, 0, 0)");
        GridPane.setConstraints(selectedAvatarImageView, col, row);
        grid.getChildren().add(selectedAvatarImageView);
    }

    public void storeNoBorderImage(ImageView unBordered, ImageView bordered, int position) {
        previouslySelectedImageViewUnBordered = unBordered;
        previouslySelectedImageViewBordered = bordered;
        previouslySelectedPosition = position;
    }

    public String getSelectedAvatarImageURL() {
        return selectedAvatarURL;
    }
}

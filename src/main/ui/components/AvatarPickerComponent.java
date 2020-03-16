package ui.components;

// Icons made by https://www.flaticon.com/authors/freepik from https://www.flaticon.com/

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


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
    private ImageView previouslySelectedImageViewUnBordered;
    private ImageView previouslySelectedImageViewBordered;
    private int previouslySelectedPosition;

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
                    event -> imageView.setStyle(
                            "-fx-cursor: hand; "
                            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0)"));
            imageView.addEventHandler(MouseEvent.MOUSE_EXITED,
                    event -> imageView.setStyle("-fx-cursor: default;"));
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                selectedAvatarImageView = new ImageView(imageView.getImage());
                putPreviousImageBack();
                grid.getChildren().remove(imageView);
                putBorderedIntoGrid(finalI);
                storeImageViewForLater(imageView, selectedAvatarImageView, finalI);
            });
        }
    }

    public void storeImageViewForLater(ImageView unBordered, ImageView bordered, int position) {
        previouslySelectedImageViewUnBordered = unBordered;
        previouslySelectedImageViewBordered = bordered;
        previouslySelectedPosition = position;
    }

    public void putPreviousImageBack() {
        if (previouslySelectedImageViewUnBordered != null) {
            grid.getChildren().remove(selectedAvatarImageView);
            int col = previouslySelectedPosition % 3;
            int row = previouslySelectedPosition / 3;
            GridPane.setConstraints(previouslySelectedImageViewUnBordered, col, row);
            grid.getChildren().remove(previouslySelectedImageViewBordered);
            grid.getChildren().add(previouslySelectedImageViewUnBordered);
        }
    }

    // https://stackoverflow.com/questions/20489908/border-radius-and-shadow-on-imageview
    public void putBorderedIntoGrid(int i) {
        int col = i % 3;
        int row = i / 3;
        selectedAvatarImageView.setStyle("-fx-effect: dropshadow(three-pass-box, #383838, 30, 0, 0, 0)");
        GridPane.setConstraints(selectedAvatarImageView, col, row);
        grid.getChildren().add(selectedAvatarImageView);
    }

    public Image getSelectedAvatarImage() {
        return selectedAvatarImageView.getImage();
    }
}

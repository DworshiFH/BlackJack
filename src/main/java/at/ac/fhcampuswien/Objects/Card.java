package at.ac.fhcampuswien.Objects;

import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Card {

    private String ID;
    private int value;
    private ImageView imageView;
    private boolean isAce;

    public Card(String ID, int value){
        this.ID = ID;
        this.value = value;
        this.imageView =null;
        this.isAce =false;
    }

    public Card(String ID, int value, boolean isAce) {
        this.ID = ID;
        this.value = value;
        this.imageView = null;
        this.isAce = isAce;
    }

    public Card(Card card){
        this.ID= card.getID();
        this.value= card.getValue();
        this.imageView= card.getImageView();
        this.isAce = card.getIsAce();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        //source: https://stackoverflow.com/questions/20489908/border-radius-and-shadow-on-imageview
        // set a clip to apply rounded border to the original image.
        Rectangle clip = new Rectangle(
                imageView.getFitWidth(), imageView.getFitHeight()
        );
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        imageView.setClip(clip);

        // snapshot the rounded image.
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        // remove the rounding clip so that our effect can show through.
        imageView.setClip(null);

        // apply a shadow effect.
        imageView.setEffect(new DropShadow(20, Color.BLACK));

        // store the rounded image in the imageView.
        imageView.setImage(image);

        this.imageView = imageView;
    }

    public void setImageViewX(double offset){
        this.imageView.setLayoutX(offset);
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getID() {
        return ID;
    }

    public boolean getIsAce() {
        return isAce;
    }

    public void setImage(Image image) {
        this.imageView.setImage(image);

        //source: https://stackoverflow.com/questions/20489908/border-radius-and-shadow-on-imageview
        // set a clip to apply rounded border to the original image.
        Rectangle clip = new Rectangle(
                this.imageView.getFitWidth(), imageView.getFitHeight()
        );
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        this.imageView.setClip(clip);

        // snapshot the rounded image.
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage img = this.imageView.snapshot(parameters, null);

        // remove the rounding clip so that our effect can show through.
        this.imageView.setClip(null);

        // apply a shadow effect.
        this.imageView.setEffect(new DropShadow(20, Color.BLACK));

        // store the rounded image in the imageView.
        this.imageView.setImage(img);
    }
}
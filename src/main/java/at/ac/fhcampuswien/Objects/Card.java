package at.ac.fhcampuswien.Objects;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card {

    private String ID;
    private int value;
    private ImageView imageView;
    private boolean isAs;

    public Card(String ID, int value){
        this.ID = ID;
        this.value = value;
        this.imageView =null;
        this.isAs=false;
    }

    public Card(String ID, int value, boolean isAs) {
        this.ID = ID;
        this.value = value;
        this.imageView = null;
        this.isAs = isAs;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
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
        return isAs;
    }

    public void setImage(Image image) {
        this.imageView.setImage(image);
    }
}

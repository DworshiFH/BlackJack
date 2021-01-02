package at.ac.fhcampuswien.Objects;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card {

    private String ID;
    private int value;
    private ImageView imageView;

    public Card(String ID, int value){
        this.ID = ID;
        this.value = value;
        this.imageView =null;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getValue(){
        return value;
    }

    public String getID() {
        return ID;
    }

    public void setImage(Image image) {
        this.imageView.setImage(image);
    }
}

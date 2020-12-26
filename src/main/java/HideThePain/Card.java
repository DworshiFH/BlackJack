package HideThePain;


import javafx.scene.image.ImageView;

public class Card {

    private String ID;
    private int value;
    private ImageView texture;

    public Card(String ID, int value){
        this.ID = ID;
        this.value = value;
        this.texture=null;
    }

    public ImageView getTexture() {
        return texture;
    }

    public void setTexture(ImageView texture) {
        this.texture = texture;
    }

    public int getValue(){
        return value;
    }

    public String getID() {
        return ID;
    }
}

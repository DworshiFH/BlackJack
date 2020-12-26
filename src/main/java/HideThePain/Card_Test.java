package HideThePain;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Card_Test {

    private String ID;
    private int value;
    private ImageView texture;
    private int texPosX;
    private int texPosY;

    public Card_Test(String ID, int value) throws FileNotFoundException{
        this.ID = ID;
        this.value = value;
        this.texture=textureAssigner(this.ID);
        this.texPosX=50;
        this.texPosY=50;
    }

    public ImageView textureAssigner(String ID){
        ImageView imageView = new ImageView();
        try{
            Image image = new Image(new FileInputStream("@../../rsc/textures/cards/"+ID+".jpg"));
            imageView.setImage(image);
            imageView.setFitHeight(455);
            imageView.setFitWidth(500);
        }catch (IOException e){
            e.printStackTrace();
        }
        return imageView;
    }

    public ImageView getTexture() {
        return texture;
    }

    public int getTexPosX() {
        return texPosX;
    }

    public void setTexPosX(int texPosX) {
        this.texPosX = texPosX;
    }

    public int getTexPosY() {
        return texPosY;
    }

    public void setTexPosY(int texPosY) {
        this.texPosY = texPosY;
    }

    public int getValue(){
        return value;
    }

    public String getID() {
        return ID;
    }
}

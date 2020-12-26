package HideThePain;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.w3c.dom.Text;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UI_Handler {

    private static final int offset = 50;

    //just handles Card Textures
    public static ImageView CardTextureAssigner(Card card, int cardnum) throws FileNotFoundException {
        ImageView imageView = new ImageView();
        try{
            Image image = new Image(new FileInputStream("@../../../../rsc/textures/cards/"+card.getID()+".jpg"));
            imageView.setImage(image);
            imageView.setFitHeight(78);
            imageView.setFitWidth(51);
            imageView.setLayoutX(50+offset*cardnum);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            //use Logger for future builds
        }
        return imageView;
    }

    @FXML
    private Text splitButton;
    @FXML protected void handleSplitButtonAction(ActionEvent event){

    }

}

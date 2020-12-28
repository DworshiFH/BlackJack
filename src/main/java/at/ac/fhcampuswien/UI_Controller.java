package at.ac.fhcampuswien;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class UI_Controller {

    @FXML
    private TextArea textTest;

    public void initialize(){
        textTest.setText("Test");
    }


}

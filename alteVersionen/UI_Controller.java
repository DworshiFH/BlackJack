package at.ac.fhcampuswien.alteVersionen;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class UI_Controller {

    @FXML
    private TextArea textArea;

    public void initialize(){
        load();

        textArea.setText("Test");
    }

    private void load() {

    }

    public void exit() {
        Platform.exit();
    }
}

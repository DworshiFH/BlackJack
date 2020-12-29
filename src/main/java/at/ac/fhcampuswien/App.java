package at.ac.fhcampuswien;

import at.ac.fhcampuswien.alteVersionen.UI_Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("UI.fxml")); // only looks into /resources folder for files!
        Parent root = loader.load();
        AppController ctrl = loader.getController();  // get the controller instance of UI.fxml which is of type NotesController

        stage.setOnCloseRequest(e -> {
            e.consume();
            ctrl.exit();
            //loader.<NotesController>getController().exit(); // bug - controller is null - seems to be deprecated code
        });

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

/*@Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Hello JavaFX!");
        btn.setOnAction( (event) -> Platform.exit() );
        Pane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 150));
        primaryStage.show();
    }*/

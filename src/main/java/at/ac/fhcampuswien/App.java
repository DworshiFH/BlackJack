package at.ac.fhcampuswien;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("UI.fxml")); // only looks into /resources folder for files!
        Parent root = loader.load();
        AppController ctrl = loader.getController();  // get the controller instance of UI.fxml which is of type NotesController

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            ctrl.exit();
        });

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setTitle("Black Jack");
    }
}
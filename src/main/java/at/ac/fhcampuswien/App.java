package at.ac.fhcampuswien;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("UI.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));

        primaryStage.show();




        /*//Parent ui = FXMLLoader.load(getClass().getResource("UI_backup.fxml"));
        primaryStage.setTitle("Black Jack");
        Pane ui = new StackPane();
        Scene scene = new Scene(ui, 800,600);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();*/

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

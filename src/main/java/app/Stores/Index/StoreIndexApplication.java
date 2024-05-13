package app.Stores.Index;

import app.Index.IndexApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StoreIndexApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StoreIndexApplication.class.getResource("stores-index-views.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1288, 579);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();


    }
}

package app.Log;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.io.IOException;

public class LogApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LogApplication.class.getResource("logs.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1288, 579);

        primaryStage.setScene(scene);
        LogController logController = fxmlLoader.getController();
        logController.load_cards();
        primaryStage.show();
    }
}

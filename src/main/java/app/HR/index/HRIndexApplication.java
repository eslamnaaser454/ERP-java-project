package app.HR.index;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HRIndexApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HRIndexApplication.class.getResource("HR-index-views.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1288, 579);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

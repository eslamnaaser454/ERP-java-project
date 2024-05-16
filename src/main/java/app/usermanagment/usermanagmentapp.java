package app.usermanagment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class usermanagmentapp extends Application {
    public usermanagmentapp(){

    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(usermanagmentapp.class.getResource("usermanagment.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1288, 579 );
        stage.setTitle("ERP!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
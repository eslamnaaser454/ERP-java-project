package app.usermanagment.Createuser;
import app.Login.LoginApplication;
import app.usermanagment.usermanagmentapp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateuserApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(CreateuserApplication.class.getResource("Createuser.fxml"));
        AnchorPane anchorPane  = new AnchorPane();

        Scene scene = new Scene(fxmlLoader.load() );
        stage.setTitle("ERP!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
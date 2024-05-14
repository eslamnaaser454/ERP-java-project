package app.Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    public LoginApplication(){

    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login-view.fxml"));
        AnchorPane anchorPane  = new AnchorPane();

        Scene scene = new Scene(fxmlLoader.load() );
        stage.setTitle("ERP!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    //test push

    public static void main(String[] args) {
        launch();
    }
}
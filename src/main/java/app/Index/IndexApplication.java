package app.Index;

import app.Classes.Authentication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class IndexApplication extends Application {
    Authentication authentication;

    public IndexApplication(){

    }

  public IndexApplication(Authentication authentication){
        this.authentication = authentication;

    }
    public static boolean isMaximized = false;

    @Override
    public void start(Stage stage)  throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(IndexApplication.class.getResource("Index-view.fxml"));
        Scene scene =  scene = new Scene(fxmlLoader.load() ,1288, 579);

        stage.setTitle("ERP!");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
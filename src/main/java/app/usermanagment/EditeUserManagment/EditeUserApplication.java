package app.usermanagment.EditeUserManagment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EditeUserApplication extends Application {
private String id;
    public EditeUserApplication() {
    }
    public EditeUserApplication(String id) {
    this.id=id;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(app.usermanagment.EditeUserManagment.EditeUserApplication.class.getResource("EditeUser.fxml"));
        Scene scene = new Scene(fxmlLoader.load() );
        EditeUserController editeUserController=fxmlLoader.getController();
        stage.setTitle("ERP!");
        stage.setResizable(false);
        stage.setScene(scene);
        editeUserController.setId(id);
        editeUserController.load_data();


        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package app.Stores.Manage.Create;

import app.Stores.Manage.StoreManageApplication;
import app.Stores.Manage.StoreManageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateStoreFormApplication extends Application {



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CreateStoreFormApplication.class.getResource("create-store-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CreateStoreFormController createStoreFormController = fxmlLoader.getController();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

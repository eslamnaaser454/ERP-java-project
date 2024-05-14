package app.Stores.Manage;

import app.Stores.Index.StoreIndexApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StoreManageApplication extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StoreManageApplication.class.getResource("store-manage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1288, 579);
        StoreManageController storeManageController = fxmlLoader.getController();
        new Thread(() -> {
            storeManageController.refreshTable();
        }).start();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

package app.Stores.Manage.Edite;

import app.Stores.Manage.StoreManageController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EditeStoreFormApplication extends Application {

    private String id;
    public EditeStoreFormApplication(){

    }
    public EditeStoreFormApplication(String id) {
        this.id = id;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditeStoreFormApplication.class.getResource("edit-store-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        EditeStoreFormController editeStoreFormController = fxmlLoader.getController();
        editeStoreFormController.setStoreManageController(storeManageController);
        editeStoreFormController.setId(id);
        System.out.println("Id from application class : "+id);
        editeStoreFormController.setData();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private StoreManageController storeManageController;
    public void setStoreManageController(StoreManageController storeManageController) {
        this.storeManageController = storeManageController;
    }
}

package app.HR.Manage.Edite;

import app.HR.Manage.ManageEmplyessController;
import app.Stores.Manage.Edite.EditeStoreFormController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EditeEmployessApplication extends Application {
    private String id;
    public void setId(String id) {
        this.id = id;
    }
    public EditeEmployessApplication() {
    }
    public EditeEmployessApplication(String id) {
        this.id = id;
    }
    public void setData() {

    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditeEmployessApplication.class.getResource("EditeEmployess.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        EditeEmployessController editeEmployessController = fxmlLoader.getController();
        editeEmployessController.setManageEmplyessController(manageEmplyessController);
        editeEmployessController.setId(id);
        System.out.println("Id from application class : "+id);
        editeEmployessController.setData();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private ManageEmplyessController manageEmplyessController;
    public void setManageEmplyessController(ManageEmplyessController manageEmplyessController) {
        this.manageEmplyessController = manageEmplyessController;
    }
}

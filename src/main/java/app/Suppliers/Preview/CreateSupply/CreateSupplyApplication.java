package app.Suppliers.Preview.CreateSupply;

import app.Suppliers.Preview.PreviewSupplierApplication;
import app.Suppliers.Preview.PreviewSupplierController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateSupplyApplication extends Application {
    private String id;

    public CreateSupplyApplication(String id) {
        this.id = id;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CreateSupplyApplication.class.getResource("create-supply-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        {}


        primaryStage.setScene(scene);
        CreateSupplyController createSupplyController = fxmlLoader.getController();
        createSupplyController.setId(id);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}

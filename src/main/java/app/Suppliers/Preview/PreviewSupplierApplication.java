package app.Suppliers.Preview;

import app.Suppliers.SuppliersApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PreviewSupplierApplication extends Application {
    private String id;
    public PreviewSupplierApplication(String id){
        this.id = id;
    }
    public PreviewSupplierApplication(){

    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PreviewSupplierApplication.class.getResource("preview-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1288, 579);
        PreviewSupplierController previewSupplierController =  fxmlLoader.getController();

        previewSupplierController.setId(this.id);
        previewSupplierController.setData();
        System.out.println("Id from application class : "+id);

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
}

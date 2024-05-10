package app.Suppliers.Preview.PreviewSupply.EditeSupply;

import app.Suppliers.Preview.PreviewSupply.PreviewSupplyApplication;
import app.Suppliers.Preview.PreviewSupply.PreviewSupplyController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOError;
import java.io.IOException;

public class EditeSupplyApplication extends Application {
    private String id;
    private PreviewSupplyController previewSupplyController;

    public EditeSupplyApplication(String id,PreviewSupplyController previewSupplyController) {
        this.id = id;
        this.previewSupplyController = previewSupplyController;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditeSupplyApplication.class.getResource("edite-supply-view.fxml"));
        Scene scene=new Scene(fxmlLoader.load());
        EditeSupplyController editeSupplyController = fxmlLoader.getController();

        primaryStage.setScene(scene);
        editeSupplyController.setId(id);
        System.out.println("Supply = "+id);
        editeSupplyController.setPreviewSupplyController(previewSupplyController);
        editeSupplyController.setData();
        primaryStage.show();

    }
}

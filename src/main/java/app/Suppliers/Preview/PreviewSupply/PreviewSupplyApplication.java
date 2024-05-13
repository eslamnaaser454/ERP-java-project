package app.Suppliers.Preview.PreviewSupply;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
//PreviewSupplyController
public class PreviewSupplyApplication extends Application {
    private String id;

    public PreviewSupplyApplication(String id) {
        this.id = id;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader =new FXMLLoader(PreviewSupplyApplication.class.getResource("supply-preview.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1288, 579);
        primaryStage.setScene(scene);
        PreviewSupplyController previewSupplyController = fxmlLoader.getController();
        previewSupplyController.setId(id);
        previewSupplyController.setData();

        primaryStage.show();


    }
}

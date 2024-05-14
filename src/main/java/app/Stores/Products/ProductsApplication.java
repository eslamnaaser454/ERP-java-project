package app.Stores.Products;

import app.Sales.SalesApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductsApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductsApplication.class.getResource("product.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1288, 579);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

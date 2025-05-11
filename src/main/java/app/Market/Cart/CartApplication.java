package app.Market.Cart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class CartApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlURL = getClass().getResource("Cart.fxml");
        System.out.println(fxmlURL); // Confirming the path

        if (fxmlURL == null) {
            System.err.println("FXML file not found!");
            return;
        }

        Parent root = FXMLLoader.load(fxmlURL);
        Scene scene = new Scene(root, 1288, 579);
        primaryStage.setTitle("ERP!");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

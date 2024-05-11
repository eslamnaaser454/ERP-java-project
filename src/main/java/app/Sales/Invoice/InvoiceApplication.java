package app.Sales.Invoice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InvoiceApplication extends Application {
    private String id;

    public InvoiceApplication(String id) {
        this.id = id;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InvoiceApplication.class.getResource("invoice-preview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        InvoiceController invoiceController = fxmlLoader.getController();
        invoiceController.setId(id);
        invoiceController.setData();
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}

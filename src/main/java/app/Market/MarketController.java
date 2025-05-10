package app.Market;

import app.Classes.DataBaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class MarketController implements Initializable {
    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    private DataBaseConnection dataBaseConnection;

    @FXML
    private VBox supplyContainer;

    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerPhoneField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataBaseConnection = new DataBaseConnection(dbPath);
        loadSupplies();
    }

    public void loadSupplies() {
        dataBaseConnection = new DataBaseConnection(dbPath);
        List<Map<String, String>> supplies = dataBaseConnection.select("SELECT * FROM supply;");
        supplyContainer.getChildren().clear();

        for (Map<String, String> supply : supplies) {
            File file = new File(System.getProperty("user.dir") + supply.get("image"));
            if (!file.exists()) {
                file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\app\\images\\defaultImages\\NoneImage.jpg");
            }

            ImageView imageView;
            try {
                imageView = new ImageView(new Image(file.toURI().toURL().toString()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);

            // Retrieve specific supplier and stock based on IDs
            Map<String, String> supplier = dataBaseConnection.select("SELECT * FROM supplier WHERE id = " + supply.get("supplier_id") + ";").get(0);
            Map<String, String> stock = dataBaseConnection.select("SELECT * FROM stock WHERE id = " + supply.get("stock_id") + ";").get(0);

            // Labels
            Label productNameLabel = new Label("Product Name: ");
            Label productNameValueLabel = new Label(supply.get("name"));
            HBox nameHbox = new HBox(productNameLabel, productNameValueLabel);

            Label quantityLabel = new Label("Quantity: ");
            Label quantityValueLabel = new Label(supply.get("qnt"));
            HBox qntHbox = new HBox(quantityLabel, quantityValueLabel);

            Label unitPriceLabel = new Label("Unit Price: ");
            Label unitPriceValueLabel = new Label(supply.get("unite_price"));
            HBox unitPriceHbox = new HBox(unitPriceLabel, unitPriceValueLabel);

            Label additionalFeesLabel = new Label("Additional Fees: ");
            Label additionalFeesValueLabel = new Label(supply.get("additional_fees"));
            HBox additionalFeesHbox = new HBox(additionalFeesLabel, additionalFeesValueLabel);

            Label sellingPriceLabel = new Label("Selling Price: ");
            Label sellingPriceValueLabel = new Label(supply.get("sell_price"));
            HBox sellingPriceHbox = new HBox(sellingPriceLabel, sellingPriceValueLabel);

            Label supplierLabel = new Label("Supplier: ");
            Hyperlink supplierHyperlink = new Hyperlink(supplier.get("name"));
            HBox supplierHbox = new HBox(supplierLabel, supplierHyperlink);

            Label stockLabel = new Label("Stock: ");
            Hyperlink stockHyperlink = new Hyperlink(stock.get("name"));
            HBox stockHbox = new HBox(stockLabel, stockHyperlink);

            // Sell Button
            Button sellButton = new Button("Sell");
            sellButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            sellButton.setPadding(new Insets(5, 15, 5, 15));
            sellButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String customerName = customerNameField.getText();
                    String customerPhone = customerPhoneField.getText();
                    TextInputDialog quantityDialog = new TextInputDialog();
                    quantityDialog.setTitle("Sell Product");
                    quantityDialog.setHeaderText("Enter quantity to sell:");
                    Optional<String> result = quantityDialog.showAndWait();
                    result.ifPresent(quantityStr -> {
                        try {
                            int quantityToSell = Integer.parseInt(quantityStr);
                            int currentQuantity = Integer.parseInt(supply.get("qnt"));
                            if (quantityToSell <= 0 || quantityToSell > currentQuantity) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid quantity entered.");
                                alert.show();
                                return;
                            }

                            // Update supply quantity
                            int newQuantity = currentQuantity - quantityToSell;
                            dataBaseConnection.excute("UPDATE supply SET qnt = " + newQuantity + " WHERE id = " + supply.get("id") + ";");

                            // Insert sale record
                            double unitPrice = Double.parseDouble(supply.get("sell_price"));
                            double totalPrice = unitPrice * quantityToSell;
                            String insertSale = String.format("INSERT INTO sale (customer_name, customer_phone, supply_id, quantity, total_price) VALUES ('%s', '%s', %s, %d, %.2f);",
                                    customerName, customerPhone, supply.get("id"), quantityToSell, totalPrice);
                            dataBaseConnection.excute(insertSale);

                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sale completed successfully.");
                            alert.show();

                            // Refresh the supplies list
                            loadSupplies();
                        } catch (NumberFormatException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number for quantity.");
                            alert.show();
                        }
                    });
                }
            });

            VBox detailsVBox = new VBox(nameHbox, qntHbox, unitPriceHbox, additionalFeesHbox, sellingPriceHbox, supplierHbox, stockHbox);
            detailsVBox.setSpacing(5);
            for (Node node : detailsVBox.getChildren()) {
                if (node instanceof HBox) {
                    for (Node l : ((HBox) node).getChildren()) {
                        if (l instanceof Label) {
                            ((Label) l).setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 12));
                        }
                    }
                }
            }

            VBox buttonsBox = new VBox(sellButton);
            buttonsBox.setAlignment(Pos.CENTER);
            buttonsBox.setSpacing(10);

            HBox mainBox = new HBox(imageView, detailsVBox, buttonsBox);
            HBox.setHgrow(detailsVBox, Priority.ALWAYS);
            mainBox.setPadding(new Insets(10));
            mainBox.setSpacing(10);
            mainBox.setAlignment(Pos.CENTER_LEFT);
            mainBox.setStyle("-fx-background-color: rgba(203,203,203,0.4);");

            supplyContainer.getChildren().add(mainBox);
        }
    }

    // Implement the method for the button's action
    public void setData(ActionEvent actionEvent) {
        // You can trigger a refresh of the supplies or perform some other action here
        loadSupplies(); // Example action: Reload all supplies
    }
}

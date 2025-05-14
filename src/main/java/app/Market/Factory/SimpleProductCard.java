package app.Market.Factory;

import app.Market.Cart.CartController;
import app.Market.Cart.CartItem;
import app.Suppliers.Preview.PreviewSupply.PreviewSupplyApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleProductCard implements ProductCard {
    private final MarketService marketService;

    public SimpleProductCard(MarketService marketService) {
        this.marketService = marketService;
    }

    @Override
    public HBox createCard(Map<String, String> supply) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(5, 15, 5, 15));
        card.setAlignment(Pos.CENTER_LEFT);

        ImageView imageView = new ImageView();
        try {
            File file = new File(System.getProperty("user.dir") + supply.get("image"));
            if (!file.exists()) {
                file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\app\\images\\defaultImages\\NoneImage.jpg");
            }
            imageView.setImage(new Image(file.toURI().toURL().toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        imageView.setFitHeight(120);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);

        VBox details = new VBox(8);
        details.getChildren().addAll(
                createDetailRow("Product:", supply.get("name")),
                createDetailRow("Price:", "$" + supply.get("sell_price")),
                createDetailRow("Stock:", supply.get("qnt")),
                createDetailRow("Supplier:", marketService.getSupplierName(supply.get("supplier_id"))),
                createDetailRow("Location:", marketService.getStockLocation(supply.get("stock_id")))
        );
        details.setPrefWidth(350);

        Spinner<Integer> spinner = new Spinner<>(1, Integer.parseInt(supply.get("qnt")), 1);
        spinner.setEditable(true);
        spinner.setPrefWidth(80);
        spinner.getEditor().setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*") ? c : null));



        Button addButton = new Button("Add to Cart");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> marketService.handleAddToCart(supply, spinner.getValue()));

        VBox controlsContainer = new VBox(10, addButton, spinner);
        controlsContainer.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(imageView, details, spacer, controlsContainer);
        return card;
    }

    private HBox createDetailRow(String label, String value) {
        Label lbl = new Label(label);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        Label val = new Label(value);
        return new HBox(5, lbl, val);
    }
}

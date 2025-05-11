package app.Market;

import app.Classes.DataBaseConnection;
import app.Market.Cart.CartApplication;
import app.Market.Cart.CartController;
import app.Market.Cart.CartItem;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class MarketController implements Initializable {

    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    private DataBaseConnection dataBaseConnection;

    @FXML private VBox supplyContainer;
    @FXML private TextField searchField;
    @FXML private Label cartBadge;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataBaseConnection = new DataBaseConnection(dbPath);
        loadSupplies();
        updateCartBadge();
    }

    private void loadSupplies() {
        supplyContainer.getChildren().clear();
        List<Map<String, String>> supplies = dataBaseConnection.select("SELECT * FROM supply WHERE qnt > 0;");
        supplies.forEach(supply -> supplyContainer.getChildren().add(createProductCard(supply)));
    }

    private HBox createProductCard(Map<String, String> supply) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(5, 15, 5, 15));
        card.setAlignment(Pos.CENTER_LEFT);

        // Product Image
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

        // Product Details
        VBox details = new VBox(8);
        details.getChildren().addAll(
                createDetailRow("Product:", supply.get("name")),
                createDetailRow("Price:", "$" + supply.get("sell_price")),
                createDetailRow("Stock:", supply.get("qnt")),
                createDetailRow("Supplier:", getSupplierName(supply.get("supplier_id"))),
                createDetailRow("Location:", getStockLocation(supply.get("stock_id")))
        );
        details.setPrefWidth(350);

        // Cart Controls
        Spinner<Integer> spinner = new Spinner<>(1, Integer.parseInt(supply.get("qnt")), 1);
        spinner.setEditable(true);
        spinner.setPrefWidth(80);
        spinner.getEditor().setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("\\d*")) return c;
            return null;
        }));

        Button addButton = new Button("Add to Cart");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> handleAddToCart(supply, spinner.getValue()));

        VBox controlsContainer = new VBox(10, addButton, spinner);
        controlsContainer.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(imageView, details, spacer, controlsContainer);
        return card;
    }

    private String getSupplierName(String supplierId) {
        List<Map<String, String>> supplier = dataBaseConnection.select(
                "SELECT name FROM supplier WHERE id = " + supplierId);
        return supplier.isEmpty() ? "N/A" : supplier.get(0).get("name");
    }

    private String getStockLocation(String stockId) {
        List<Map<String, String>> stock = dataBaseConnection.select(
                "SELECT name FROM stock WHERE id = " + stockId);
        return stock.isEmpty() ? "N/A" : stock.get(0).get("name");
    }

    private HBox createDetailRow(String label, String value) {
        Label lbl = new Label(label);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        Label val = new Label(value);
        return new HBox(5, lbl, val);
    }

    private void handleAddToCart(Map<String, String> supply, int quantity) {
        CartItem newItem = new CartItem(
                Integer.parseInt(supply.get("id")),
                supply.get("name"),
                Double.parseDouble(supply.get("sell_price")),
                quantity,
                supply.get("image")
        );

        Optional<CartItem> existing = CartController.cartItems.stream()
                .filter(item -> item.getSupplyId() == newItem.getSupplyId())
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            CartController.cartItems.add(newItem);
        }

        updateCartBadge();
        showAlert("Success", quantity + " × " + newItem.getName() + " added to cart");
    }

    private void updateCartBadge() {
        if (cartBadge != null) {
            int count = CartController.cartItems.stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();
            cartBadge.setText(String.valueOf(count));
            cartBadge.setVisible(count > 0);
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String query = searchField.getText().trim();
        List<Map<String, String>> results = dataBaseConnection.select(
                "SELECT * FROM supply WHERE name LIKE '%" + query + "%' AND qnt > 0;"
        );
        supplyContainer.getChildren().clear();
        results.forEach(supply -> supplyContainer.getChildren().add(createProductCard(supply)));
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadSupplies();
        searchField.clear();
    }

    @FXML
    private void GoToCart(MouseEvent event) {
        CartApplication cartApp = new CartApplication();
        Stage stage = (Stage) supplyContainer.getScene().getWindow();
        try {
            cartApp.start(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

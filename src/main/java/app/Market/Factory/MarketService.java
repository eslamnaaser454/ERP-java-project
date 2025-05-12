package app.Market.Factory;

import app.Classes.DataBaseConnection;
import app.Market.Cart.CartController;
import app.Market.Cart.CartItem;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MarketService {
    private final DataBaseConnection dbConnection;
    private final Label cartBadge;

    public MarketService(DataBaseConnection dbConnection, Label cartBadge) {
        this.dbConnection = dbConnection;
        this.cartBadge = cartBadge;
    }

    public String getSupplierName(String supplierId) {
        List<Map<String, String>> supplier = dbConnection.select("SELECT name FROM supplier WHERE id = " + supplierId);
        return supplier.isEmpty() ? "N/A" : supplier.get(0).get("name");
    }

    public String getStockLocation(String stockId) {
        List<Map<String, String>> stock = dbConnection.select("SELECT name FROM stock WHERE id = " + stockId);
        return stock.isEmpty() ? "N/A" : stock.get(0).get("name");
    }

    public void handleAddToCart(Map<String, String> supply, int quantity) {
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
        int count = CartController.cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        cartBadge.setText(String.valueOf(count));
        cartBadge.setVisible(count > 0);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public List<Map<String, String>> getSupplies() {
        return dbConnection.select("SELECT * FROM supply WHERE qnt > 0;");
    }

    public List<Map<String, String>> searchSupplies(String query) {
        return dbConnection.select("SELECT * FROM supply WHERE name LIKE '%" + query + "%' AND qnt > 0;");
    }
}

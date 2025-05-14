package app.Market.Factory;

import app.Classes.DataBaseConnection;
import app.Market.Cart.CartController;
import app.Market.Cart.CartItem;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MarketService {
    private final DataBaseConnection dataBaseConnection;
    private final Label cartBadge;

    public MarketService(DataBaseConnection dataBaseConnection, Label cartBadge) {
        this.dataBaseConnection = dataBaseConnection;
        this.cartBadge = cartBadge;
        updateCartBadge();
    }

    public List<Map<String, String>> getSupplies() {
        return dataBaseConnection.select("SELECT * FROM supply WHERE qnt > 0;");
    }

    public List<Map<String, String>> searchSupplies(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getSupplies();
        }

        String searchQuery = "SELECT * FROM supply WHERE qnt > 0 AND (name LIKE '%" + query + "%' OR sell_price LIKE '%" + query + "%');";
        return dataBaseConnection.select(searchQuery);
    }

    public void handleAddToCart(Map<String, String> supply, Integer quantity) {
        // Handle null or invalid quantity
        if (quantity == null || quantity <= 0) {
            showAlert(Alert.AlertType.WARNING, "Invalid Quantity",
                    "Please enter a valid quantity greater than zero.");
            return;
        }

        int supplyId = Integer.parseInt(supply.get("id"));
        String name = supply.get("name");
        double price = Double.parseDouble(supply.get("sell_price"));
        String imagePath = supply.get("image");

        try {
            // Get the current stock quantity
            int availableStock = Integer.parseInt(supply.get("qnt"));

            // Get current quantity in cart for this item
            int currentCartQuantity = getCurrentCartQuantity(supplyId);

            // Validate that the requested quantity doesn't exceed available stock
            if (quantity > availableStock) {
                showAlert(Alert.AlertType.WARNING, "Insufficient Stock",
                        "Cannot add " + quantity + " items. Only " + availableStock + " available in stock.");
                return;
            }

            // Check if adding this quantity would exceed available stock when combined with what's already in cart
            if (currentCartQuantity + quantity > availableStock) {
                showAlert(Alert.AlertType.WARNING, "Insufficient Stock",
                        "You already have " + currentCartQuantity + " of this item in your cart. " +
                                "Cannot add " + quantity + " more. Only " + availableStock + " available in stock.");
                return;
            }

            // Check if the item is already in the cart
            for (CartItem item : CartController.cartItems) {
                if (item.getSupplyId() == supplyId) {
                    // Item exists, update quantity
                    item.setQuantity(item.getQuantity() + quantity);
                    updateCartBadge();
                    return;
                }
            }

            // If we get here, the item is not in the cart yet
            CartController.cartItems.add(new CartItem(supplyId, name, price, quantity, imagePath));
            updateCartBadge();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Invalid quantity or stock value. Please try again.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "An error occurred while adding to cart: " + e.getMessage());
        }
    }

    // Helper method to get the current quantity of an item in the cart
    private int getCurrentCartQuantity(int supplyId) {
        for (CartItem item : CartController.cartItems) {
            if (item.getSupplyId() == supplyId) {
                return item.getQuantity();
            }
        }
        return 0; // Item not in cart
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateCartBadge() {
        int count = CartController.cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        cartBadge.setText(String.valueOf(count));
        cartBadge.setVisible(count > 0);
    }

    public String getSupplierName(String supplierId) {
        List<Map<String, String>> result = dataBaseConnection.select("SELECT name FROM supplier WHERE id = " + supplierId);
        return result != null && !result.isEmpty() ? result.get(0).get("name") : "Unknown";
    }

    public String getStockLocation(String stockId) {
        List<Map<String, String>> result = dataBaseConnection.select("SELECT location FROM stock WHERE id = " + stockId);
        return result != null && !result.isEmpty() ? result.get(0).get("location") : "Unknown";
    }
}
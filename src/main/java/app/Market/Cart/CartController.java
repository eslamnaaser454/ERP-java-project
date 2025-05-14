package app.Market.Cart;

import app.Classes.DataBaseConnection;
import app.Market.MarketApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    private DataBaseConnection dataBaseConnection;
    public static ArrayList<CartItem> cartItems = new ArrayList<>();

    // Store customer data for quick access
    private Map<String, CustomerInfo> customerMap = new HashMap<>();

    @FXML private VBox cartContainer;
    @FXML private Label totalLabel;
    @FXML private Button checkoutButton;
    @FXML private TextField customerNameField;
    @FXML private TextField customerPhoneField;
    @FXML private Button backToShoppingButton;
    @FXML private ComboBox<String> customerComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataBaseConnection = new DataBaseConnection(dbPath);

        // Load existing customers
        loadCustomers();

        // Setup customer selection handler
        customerComboBox.setOnAction(e -> selectCustomer());

        refreshCartView();

        // Disable checkout button if cart is empty
        checkoutButton.setDisable(cartItems.isEmpty());

        // Configure back button
        backToShoppingButton = new Button("Back to Shopping");
        backToShoppingButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        backToShoppingButton.setOnAction(e -> goBackToShopping());
    }

    private void loadCustomers() {
        try {
            // Get unique customers from invoice table
            String query = "SELECT DISTINCT customer_name, customer_phone FROM invoice ORDER BY customer_name";
            List<Map<String, String>> customers = dataBaseConnection.select(query);

            ObservableList<String> customerNames = FXCollections.observableArrayList();
            customerNames.add("-- Select Existing Customer --");

            // Add customers to map and combo box
            if (customers != null && customers.size() > 0) {
                for (Map<String, String> customer : customers) {
                    String name = customer.get("customer_name");
                    String phone = customer.get("customer_phone");

                    // Add to map for quick lookup
                    customerMap.put(name, new CustomerInfo(name, phone));

                    // Add to combo box
                    customerNames.add(name);
                }
            }

            customerComboBox.setItems(customerNames);
            customerComboBox.getSelectionModel().selectFirst();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load customer data: " + e.getMessage());
        }
    }

    private void selectCustomer() {
        String selectedName = customerComboBox.getValue();
        if (selectedName != null && !selectedName.startsWith("--")) {
            CustomerInfo customer = customerMap.get(selectedName);
            if (customer != null) {
                customerNameField.setText(customer.getName());
                customerPhoneField.setText(customer.getPhone());
            }
        }
    }

    @FXML
    private void goBackToShopping() {
        try {
            MarketApplication marketApp = new MarketApplication();
            Stage stage = (Stage) cartContainer.getScene().getWindow();
            marketApp.start(stage);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not return to shopping: " + e.getMessage());
        }
    }

    private void refreshCartView() {
        cartContainer.getChildren().clear();
        double total = 0;

        for (CartItem item : cartItems) {
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-spacing: 15;");

            // Product Image
            ImageView imageView = new ImageView();
            try {
                File file = new File(System.getProperty("user.dir") + item.getImagePath());
                if (!file.exists()) {
                    file = new File(System.getProperty("user.dir") + "/src/main/resources/app/images/defaultImages/NoneImage.jpg");
                }
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);
            imageView.setPreserveRatio(true);

            // Product Details
            VBox details = new VBox(8);
            details.getChildren().addAll(
                    createLabel(item.getName()),
                    createLabel("Quantity: " + item.getQuantity()),
                    createLabel("Price: $" + item.getUnitPrice()),
                    createLabel("Total: $" + item.getTotalPrice())
            );

            // Remove Button
            Button removeButton = new Button("Remove");
            removeButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
            removeButton.setOnAction(e -> {
                cartItems.remove(item);
                refreshCartView();
                checkoutButton.setDisable(cartItems.isEmpty());
            });

            // Add all components to the card
            card.getChildren().addAll(imageView, details, removeButton);
            cartContainer.getChildren().add(card);
            total += item.getTotalPrice();
        }

        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    @FXML
    private void handleCheckout() {
        // Validate customer details
        if (!validateCustomerDetails()) {
            return;
        }

        // Process the sale for each cart item
        processSale();
    }

    private boolean validateCustomerDetails() {
        String customerName = customerNameField.getText();
        String customerPhone = customerPhoneField.getText();

        if (customerName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Customer Name Field Is Empty");
            return false;
        }

        if (customerPhone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Customer Contact Field Is Empty");
            return false;
        } else if (customerPhone.length() < 11) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Contact Number");
            return false;
        }

        if (cartItems.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Cart is empty");
            return false;
        }

        return true;
    }

    private void processSale() {
        try {
            dataBaseConnection = new DataBaseConnection(dbPath);

            // Insert into invoice table
            String customerName = customerNameField.getText();
            String customerPhone = customerPhoneField.getText();

            String invoiceQuery = "INSERT INTO invoice(customer_name, customer_phone) VALUES ('" + customerName + "','" + customerPhone + "');";
            int invoiceID = dataBaseConnection.insert(invoiceQuery);

            // Process each cart item
            boolean allSuccessful = true;
            for (CartItem item : cartItems) {
                // Get supply details from database
                String supplyQuery = "SELECT * FROM supply WHERE id = " + item.getSupplyId() + ";";
                List<Map<String, String>> supplyList = dataBaseConnection.select(supplyQuery);

                if (supplyList == null || supplyList.size() == 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Supply with ID " + item.getSupplyId() + " not found");
                    allSuccessful = false;
                    continue;
                }

                Map<String, String> supply = supplyList.get(0);
                int availableQty = Integer.parseInt(supply.get("qnt"));

                // Check if enough quantity available
                if (item.getQuantity() > availableQty) {
                    showAlert(Alert.AlertType.WARNING, "Insufficient Stock",
                            "Not enough stock for " + item.getName() + ". Available: " + availableQty);
                    allSuccessful = false;
                    continue;
                }

                // Insert into sale table
                String saleQuery = "INSERT INTO sale(product_name, unitePrice, qnt, invoice_id, supply_id) VALUES ('" +
                        item.getName() + "'," +
                        item.getUnitPrice() + "," +
                        item.getQuantity() + "," +
                        invoiceID + "," +
                        item.getSupplyId() + ")";
                dataBaseConnection.execute(saleQuery);

                // Update supply quantity
                int updatedQnt = availableQty - item.getQuantity();
                String updateQuery = "UPDATE supply SET qnt=" + updatedQnt + " WHERE id=" + item.getSupplyId() + ";";
                dataBaseConnection.execute(updateQuery);
            }

            if (allSuccessful) {

                showAlert(Alert.AlertType.INFORMATION, "Success", "Sale completed successfully!");
                // Clear cart and fields after successful checkout
                customerNameField.clear();
                customerPhoneField.clear();
                cartItems.clear();
                refreshCartView();
                checkoutButton.setDisable(true);

                // Refresh customer list to include the new customer
                loadCustomers();
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning",
                        "Some items could not be processed. Please check your cart and try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing the sale: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14;");
        return label;
    }



    // Helper class to store customer information
    private class CustomerInfo {
        private final String name;
        private final String phone;

        public CustomerInfo(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }
    }
}
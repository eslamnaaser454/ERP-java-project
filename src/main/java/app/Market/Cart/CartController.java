package app.Market.Cart;

import app.Classes.DataBaseConnection;
import app.Classes.Logging;
import app.Market.Cart.composite.Order;
import app.Market.Cart.payment.BankPayment;
import app.Market.Cart.payment.InstapayPayment;
import app.Market.Cart.payment.PaymentContext;
import app.Market.Cart.payment.WalletPayment;
import app.Market.MarketApplication;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    private final String dbPath = System.getProperty("user.dir") + "\\src\\main\\resources\\database.db";
    private DataBaseConnection dataBaseConnection;
    public static ArrayList<CartItem> cartItems = new ArrayList<>();

    // Store customer data for quick access
    private Map<String, CustomerInfo> customerMap = new HashMap<>();

    // Payment context for strategy pattern
    private PaymentContext paymentContext = new PaymentContext();

    @FXML private VBox cartContainer;
    @FXML private Label totalLabel;
    @FXML private Button checkoutButton;
    @FXML private TextField customerNameField;
    @FXML private TextField customerPhoneField;
    @FXML private Button backToShoppingButton;
    @FXML private ComboBox<String> customerComboBox;
    @FXML private ComboBox<String> paymentMethodComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dataBaseConnection = new DataBaseConnection(dbPath);

        // Load existing customers
        loadCustomers();

        // Setup customer selection handler
        customerComboBox.setOnAction(e -> selectCustomer());

        // Setup payment method options
        setupPaymentMethods();

        refreshCartView();

        // Disable checkout button if cart is empty
        checkoutButton.setDisable(cartItems.isEmpty());

        // Configure back button
        backToShoppingButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
    }

    private void setupPaymentMethods() {
        paymentMethodComboBox.getItems().addAll("Bank Card", "Wallet", "Instapay");
        paymentMethodComboBox.getSelectionModel().selectFirst();
    }

    private void loadCustomers() {
        try {
            // Get unique customers from invoice table
            String query = "SELECT DISTINCT customer_name, customer_phone FROM invoice ORDER BY customer_name";
            List<Map<String, String>> customers = dataBaseConnection.select(query);

            customerComboBox.getItems().add("-- Select Existing Customer --");

            // Add customers to map and combo box
            if (customers != null && customers.size() > 0) {
                for (Map<String, String> customer : customers) {
                    String name = customer.get("customer_name");
                    String phone = customer.get("customer_phone");

                    // Add to map for quick lookup
                    customerMap.put(name, new CustomerInfo(name, phone));

                    // Add to combo box
                    customerComboBox.getItems().add(name);
                }
            }

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
    public void goBackToShopping() {
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

        // Create order using composite pattern
        String customerName = customerNameField.getText();
        String customerPhone = customerPhoneField.getText();
        Order order = new Order(customerName, customerPhone);
        order.addAllItems(cartItems);

        // Setup payment strategy based on selection
        String selectedPaymentMethod = paymentMethodComboBox.getValue();
        setupPaymentStrategy(selectedPaymentMethod);

        // Process payment using selected strategy
        boolean paymentSuccessful = false;
        try {
            paymentSuccessful = paymentContext.processPayment(order);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Payment Error",
                    "Error processing payment: " + e.getMessage());
            return;
        }

        if (paymentSuccessful) {
            // Process the sale in the database
            processSale(order);
        } else {
            showAlert(Alert.AlertType.WARNING, "Payment Cancelled",
                    "The payment process was cancelled or failed.");
        }
    }

    private void setupPaymentStrategy(String paymentMethod) {
        switch (paymentMethod) {
            case "Bank Card":
                paymentContext.setPaymentStrategy(new BankPayment());
                break;
            case "Wallet":
                paymentContext.setPaymentStrategy(new WalletPayment());
                break;
            case "Instapay":
                paymentContext.setPaymentStrategy(new InstapayPayment());
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Invalid Payment Method",
                        "Please select a valid payment method.");
                throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }
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

        if (paymentMethodComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a payment method");
            return false;
        }

        return true;
    }

    private void processSale(Order order) {
        try {
            // Make sure we have a fresh DB connection
            dataBaseConnection = new DataBaseConnection(dbPath);
            Logging logging = new Logging();
            // Set current date and time if not already set
            if (order.getOrderDate() == null) {
                order.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }

            // First, check if the invoice table has payment_method column
            boolean hasPaymentMethodColumn = checkIfColumnExists("invoice", "payment_method");
            boolean hasOrderDateColumn = checkIfColumnExists("invoice", "order_date");

            // Insert into invoice table
            String customerName = order.getCustomerName();
            String customerPhone = order.getCustomerPhone();
            String paymentMethod = order.getPaymentMethod();

            // Build the invoice query based on available columns
            StringBuilder invoiceQueryBuilder = new StringBuilder("INSERT INTO invoice(customer_name, customer_phone");
            StringBuilder valuesBuilder = new StringBuilder("VALUES ('");
            valuesBuilder.append(customerName).append("','").append(customerPhone).append("'");

            if (hasPaymentMethodColumn) {
                invoiceQueryBuilder.append(", payment_method");
                valuesBuilder.append(",'").append(paymentMethod).append("'");
            }

            if (hasOrderDateColumn) {
                invoiceQueryBuilder.append(", order_date");
                valuesBuilder.append(",'").append(order.getOrderDate()).append("'");
            }

            invoiceQueryBuilder.append(") ");
            valuesBuilder.append(")");

            String invoiceQuery = invoiceQueryBuilder.toString() + valuesBuilder.toString();

            // Debug output
            System.out.println("Executing invoice query: " + invoiceQuery);

            // Insert invoice and get generated ID
            int invoiceID = dataBaseConnection.insert(invoiceQuery);
            if (invoiceID <= 0) {
                throw new Exception("Failed to insert invoice record. Returned ID: " + invoiceID);
            }

            order.setInvoiceId(invoiceID);
            System.out.println("Invoice created with ID: " + invoiceID);
            logging.addLog("Created invoice #" + invoiceID + " for customer " + customerName);
            // Process each cart item
            boolean allSuccessful = true;
            for (CartItem item : cartItems) {
                try {
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

                    System.out.println("Executing sale query: " + saleQuery);
                    boolean saleInserted = dataBaseConnection.execute(saleQuery);

                    if (!saleInserted) {
                        throw new Exception("Failed to insert sale record for item: " + item.getName());
                    }

                    // Update supply quantity
                    int updatedQnt = availableQty - item.getQuantity();
                    String updateQuery = "UPDATE supply SET qnt=" + updatedQnt + " WHERE id=" + item.getSupplyId() + ";";
                    System.out.println("Executing update query: " + updateQuery);
                    boolean supplyUpdated = dataBaseConnection.execute(updateQuery);

                    if (!supplyUpdated) {
                        throw new Exception("Failed to update supply quantity for item: " + item.getName());
                    }

                    System.out.println("Successfully processed item: " + item.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error Processing Item",
                            "Error processing item " + item.getName() + ": " + e.getMessage());
                    allSuccessful = false;
                }
            }

            if (allSuccessful) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Sale completed successfully!\n\n" + paymentContext.getTransactionDetails());

                // Show order receipt
                showOrderReceipt(order);

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

    /**
     * Check if a column exists in a table
     * @param tableName The table to check
     * @param columnName The column to check for
     * @return true if the column exists, false otherwise
     */
    private boolean checkIfColumnExists(String tableName, String columnName) {
        try {
            String query = "PRAGMA table_info(" + tableName + ")";
            List<Map<String, String>> columns = dataBaseConnection.select(query);

            if (columns != null) {
                for (Map<String, String> column : columns) {
                    if (column.get("name").equalsIgnoreCase(columnName)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showOrderReceipt(Order order) {
        // Display receipt using TextArea in a dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Order Receipt");
        dialog.setHeaderText("Thank you for your purchase!");

        TextArea textArea = new TextArea(order.getComponentDetails());
        textArea.setEditable(false);
        textArea.setPrefWidth(400);
        textArea.setPrefHeight(300);

        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.showAndWait();
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
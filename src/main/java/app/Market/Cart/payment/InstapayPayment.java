package app.Market.Cart.payment;

import app.Market.Cart.composite.Order;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Concrete strategy for processing Instapay payments
 */
public class InstapayPayment implements PaymentStrategy {
    private String instapayId;
    private String transactionId;
    private double amount;

    @Override
    public boolean processPayment(Order order) {
        // Create a custom dialog instead of the simple TextInputDialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Instapay Payment");
        dialog.setHeaderText("Enter your Instapay details");
        dialog.setContentText("Instapay ID:");

        // Get the layout components to add validation
        GridPane grid = (GridPane) dialog.getDialogPane().getContent();

        // Add error label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 1, 1);

        // Get text field (it's the first node in the grid)
        javafx.scene.control.TextField textField = (javafx.scene.control.TextField) grid.getChildren().get(1);

        // Disable OK button initially if the field is empty
        ButtonType okButtonType = dialog.getDialogPane().getButtonTypes().stream()
                .filter(buttonType -> buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                .findFirst().orElse(ButtonType.OK);

        javafx.scene.control.Button okButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        // Add listener to validate input
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValid = validateInstapayId(newValue, errorLabel);
            okButton.setDisable(!isValid);
        });

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().isEmpty()) {
            this.instapayId = result.get();
            this.amount = order.getTotal();

            // Generate a transaction ID
            this.transactionId = "INS-" + System.currentTimeMillis();

            // Set payment method in order
            order.setPaymentMethod("Instapay");

            // Set order date
            order.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // Payment successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment Successful");
            alert.setHeaderText(null);
            alert.setContentText("Instapay payment processed successfully.\nTransaction ID: " + transactionId);
            alert.showAndWait();

            return true;
        }

        return false;
    }

    private boolean validateInstapayId(String id, Label errorLabel) {
        if (id == null || id.trim().isEmpty()) {
            errorLabel.setText("Instapay ID is required");
            return false;
        }

        // Validate format username@instapay
        if (!Pattern.matches("^[a-zA-Z0-9._-]+@instapay$", id)) {
            errorLabel.setText("Format must be username@instapay");
            return false;
        }

        // Validate username part length (minimum 3 characters)
        String username = id.split("@")[0];
        if (username.length() < 3) {
            errorLabel.setText("Username must be at least 3 characters");
            return false;
        }

        errorLabel.setText("");
        return true;
    }
    @Override
    public String getTransactionDetails() {
        return String.format("Instapay Payment\nInstapay ID: %s\nAmount: $%.2f\nTransaction ID: %s",
                instapayId, amount, transactionId);
    }

    @Override
    public String getMethodName() {
        return "Instapay";
    }
}
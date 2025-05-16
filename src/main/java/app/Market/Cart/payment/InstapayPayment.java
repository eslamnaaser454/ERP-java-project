package app.Market.Cart.payment;

import app.Market.Cart.composite.Order;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Concrete strategy for processing Instapay payments
 * Note: This now shows the simple text input dialog that was previously in WalletPayment
 */
public class InstapayPayment implements PaymentStrategy {
    private String instapayId;
    private String transactionId;
    private double amount;

    @Override
    public boolean processPayment(Order order) {
        // Collect Instapay ID
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Instapay Payment");
        dialog.setHeaderText("Enter your Instapay details");
        dialog.setContentText("Instapay ID:");

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
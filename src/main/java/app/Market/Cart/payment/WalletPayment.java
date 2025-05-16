package app.Market.Cart.payment;

import app.Market.Cart.composite.Order;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Concrete strategy for processing wallet payments
 * Note: This now shows the provider selection dialog that was previously in InstapayPayment
 */
public class WalletPayment implements PaymentStrategy {
    private String phoneNumber;
    private String provider;
    private String transactionId;
    private double amount;

    @Override
    public boolean processPayment(Order order) {
        // Select wallet provider
        List<String> providers = Arrays.asList("Vodafone Cash", "Orange Money", "Etisalat Cash", "We Pay");
        ChoiceDialog<String> providerDialog = new ChoiceDialog<>(providers.get(0), providers);
        providerDialog.setTitle("Wallet Payment");
        providerDialog.setHeaderText("Select your wallet provider");
        providerDialog.setContentText("Provider:");

        Optional<String> providerResult = providerDialog.showAndWait();
        if (!providerResult.isPresent()) {
            return false;
        }

        this.provider = providerResult.get();

        // Collect phone number for wallet
        TextInputDialog phoneDialog = new TextInputDialog();
        phoneDialog.setTitle("Wallet Payment");
        phoneDialog.setHeaderText("Enter your " + provider + " phone number");
        phoneDialog.setContentText("Phone Number:");

        Optional<String> phoneResult = phoneDialog.showAndWait();
        if (phoneResult.isPresent() && !phoneResult.get().isEmpty()) {
            this.phoneNumber = phoneResult.get();
            this.amount = order.getTotal();

            // Generate a transaction ID
            this.transactionId = "WLT-" + System.currentTimeMillis();

            // Set payment method in order
            order.setPaymentMethod("Wallet (" + provider + ")");

            // Set order date
            order.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // Payment successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment Successful");
            alert.setHeaderText(null);
            alert.setContentText("Wallet payment processed successfully.\nTransaction ID: " + transactionId);
            alert.showAndWait();

            return true;
        }

        return false;
    }

    @Override
    public String getTransactionDetails() {
        return String.format("Wallet Payment\nProvider: %s\nPhone: %s\nAmount: $%.2f\nTransaction ID: %s",
                provider, phoneNumber, amount, transactionId);
    }

    @Override
    public String getMethodName() {
        return "Wallet";
    }
}
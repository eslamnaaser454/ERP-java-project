package app.Market.Cart.payment;

import app.Market.Cart.composite.Order;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Concrete strategy for processing wallet payments
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

        // Collect phone number for wallet with validation
        TextInputDialog phoneDialog = new TextInputDialog();
        phoneDialog.setTitle("Wallet Payment");
        phoneDialog.setHeaderText("Enter your " + provider + " phone number");
        phoneDialog.setContentText("Phone Number:");

        // Get the layout components to add validation
        GridPane grid = (GridPane) phoneDialog.getDialogPane().getContent();

        // Add error label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        grid.add(errorLabel, 1, 1);

        // Get text field (it's the first node in the grid)
        javafx.scene.control.TextField textField = (javafx.scene.control.TextField) grid.getChildren().get(1);

        // Disable OK button initially if the field is empty
        ButtonType okButtonType = phoneDialog.getDialogPane().getButtonTypes().stream()
                .filter(buttonType -> buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                .findFirst().orElse(ButtonType.OK);

        javafx.scene.control.Button okButton = (javafx.scene.control.Button) phoneDialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        // Add listener to validate input
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isValid = validatePhoneNumber(newValue, provider, errorLabel);
            okButton.setDisable(!isValid);
        });

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

    private boolean validatePhoneNumber(String phone, String provider, Label errorLabel) {
        if (phone == null || phone.trim().isEmpty()) {
            errorLabel.setText("Phone number is required");
            return false;
        }

        // Remove spaces and dashes for validation
        String cleanPhone = phone.replaceAll("[ -]", "");

        // Basic pattern for Egyptian mobile numbers
        if (!Pattern.matches("^01[0-2|5]{1}[0-9]{8}$", cleanPhone)) {
            errorLabel.setText("Invalid Egyptian phone number");
            return false;
        }

        // Provider-specific prefix validation
        if (provider.equals("Vodafone Cash") && !cleanPhone.startsWith("010")) {
            errorLabel.setText("Vodafone numbers start with 010");
            return false;
        } else if (provider.equals("Orange Money") && !cleanPhone.startsWith("012")) {
            errorLabel.setText("Orange numbers start with 012");
            return false;
        } else if (provider.equals("Etisalat Cash") && !cleanPhone.startsWith("011")) {
            errorLabel.setText("Etisalat numbers start with 011");
            return false;
        } else if (provider.equals("We Pay") && !cleanPhone.startsWith("015")) {
            errorLabel.setText("We numbers start with 015");
            return false;
        }

        errorLabel.setText("");
        return true;
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
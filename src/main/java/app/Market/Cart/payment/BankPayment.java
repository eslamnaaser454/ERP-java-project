package app.Market.Cart.payment;

import app.Market.Cart.composite.Order;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Concrete strategy for processing bank payments
 */
public class BankPayment implements PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String transactionId;
    private double amount;

    @Override
    public boolean processPayment(Order order) {
        // Create the custom dialog for bank payment
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Bank Payment");
        dialog.setHeaderText("Enter your bank card details");

        // Set button types
        ButtonType confirmButtonType = new ButtonType("Confirm Payment", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Create the grid for form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Card Number");
        TextField nameField = new TextField();
        nameField.setPromptText("Card Holder Name");
        TextField expiryField = new TextField();
        expiryField.setPromptText("MM/YY");
        PasswordField cvvField = new PasswordField();
        cvvField.setPromptText("CVV");

        grid.add(new Label("Card Number:"), 0, 0);
        grid.add(cardNumberField, 1, 0);
        grid.add(new Label("Card Holder:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Expiry Date:"), 0, 2);
        grid.add(expiryField, 1, 2);
        grid.add(new Label("CVV:"), 0, 3);
        grid.add(cvvField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a pair when the confirm button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Pair<>(cardNumberField.getText(), nameField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if (result.isPresent()) {
            this.cardNumber = result.get().getKey();
            this.cardHolderName = result.get().getValue();
            this.expiryDate = expiryField.getText();
            this.amount = order.getTotal();

            // Generate a transaction ID
            this.transactionId = "BNK-" + System.currentTimeMillis();

            // Set payment method in order
            order.setPaymentMethod("Bank Card");

            // Set order date
            order.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // Payment successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment Successful");
            alert.setHeaderText(null);
            alert.setContentText("Bank payment processed successfully.\nTransaction ID: " + transactionId);
            alert.showAndWait();

            return true;
        }

        return false;
    }

    @Override
    public String getTransactionDetails() {
        return String.format("Bank Card Payment\nCard Number: %s\nCard Holder: %s\nAmount: $%.2f\nTransaction ID: %s",
                maskCardNumber(cardNumber), cardHolderName, amount, transactionId);
    }

    private String maskCardNumber(String number) {
        if (number == null || number.length() < 4) {
            return number;
        }
        String lastFour = number.substring(number.length() - 4);
        return "xxxx-xxxx-xxxx-" + lastFour;
    }

    @Override
    public String getMethodName() {
        return "Bank Card";
    }
}
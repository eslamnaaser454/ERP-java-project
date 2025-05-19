package app.Market.Cart.payment;

import app.Market.Cart.composite.Order;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Pattern;

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

        // Add error labels (initially empty)
        Label cardNumberError = new Label();
        cardNumberError.getStyleClass().add("error-label");
        cardNumberError.setStyle("-fx-text-fill: red;");

        Label nameError = new Label();
        nameError.getStyleClass().add("error-label");
        nameError.setStyle("-fx-text-fill: red;");

        Label expiryError = new Label();
        expiryError.getStyleClass().add("error-label");
        expiryError.setStyle("-fx-text-fill: red;");

        Label cvvError = new Label();
        cvvError.getStyleClass().add("error-label");
        cvvError.setStyle("-fx-text-fill: red;");

        grid.add(new Label("Card Number:"), 0, 0);
        grid.add(cardNumberField, 1, 0);
        grid.add(cardNumberError, 2, 0);

        grid.add(new Label("Card Holder:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(nameError, 2, 1);

        grid.add(new Label("Expiry Date:"), 0, 2);
        grid.add(expiryField, 1, 2);
        grid.add(expiryError, 2, 2);

        grid.add(new Label("CVV:"), 0, 3);
        grid.add(cvvField, 1, 3);
        grid.add(cvvError, 2, 3);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the card number field by default
        cardNumberField.requestFocus();

        // Disable confirm button until all fields are validated
        Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        // Validation listeners
        cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateForm(cardNumberField, nameField, expiryField, cvvField,
                    cardNumberError, nameError, expiryError, cvvError, confirmButton);
        });

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateForm(cardNumberField, nameField, expiryField, cvvField,
                    cardNumberError, nameError, expiryError, cvvError, confirmButton);
        });

        expiryField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateForm(cardNumberField, nameField, expiryField, cvvField,
                    cardNumberError, nameError, expiryError, cvvError, confirmButton);
        });

        cvvField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateForm(cardNumberField, nameField, expiryField, cvvField,
                    cardNumberError, nameError, expiryError, cvvError, confirmButton);
        });

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

    private void validateForm(TextField cardNumberField, TextField nameField, TextField expiryField, PasswordField cvvField,
                              Label cardNumberError, Label nameError, Label expiryError, Label cvvError, Button confirmButton) {

        boolean cardNumberValid = validateCardNumber(cardNumberField.getText(), cardNumberError);
        boolean nameValid = validateCardHolderName(nameField.getText(), nameError);
        boolean expiryValid = validateExpiryDate(expiryField.getText(), expiryError);
        boolean cvvValid = validateCvv(cvvField.getText(), cvvError);

        // Enable/disable confirm button based on validation results
        confirmButton.setDisable(!(cardNumberValid && nameValid && expiryValid && cvvValid));
    }

    private boolean validateCardNumber(String cardNumber, Label errorLabel) {
        // Remove spaces and dashes for validation
        String cleanNumber = cardNumber.replaceAll("[ -]", "");

        if (cleanNumber.isEmpty()) {
            errorLabel.setText("Card number is required");
            return false;
        }

        if (!Pattern.matches("^[0-9]{13,19}$", cleanNumber)) {
            errorLabel.setText("Invalid card number format");
            return false;
        }

        // Luhn algorithm check (optional for enhanced validation)
        if (!isValidLuhn(cleanNumber)) {
            errorLabel.setText("Invalid card number");
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    private boolean validateCardHolderName(String name, Label errorLabel) {
        if (name == null || name.trim().isEmpty()) {
            errorLabel.setText("Name is required");
            return false;
        }

        if (name.trim().length() < 3) {
            errorLabel.setText("Name is too short");
            return false;
        }

        if (!Pattern.matches("^[A-Za-z\\s'-]+$", name)) {
            errorLabel.setText("Invalid characters in name");
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    private boolean validateExpiryDate(String expiry, Label errorLabel) {
        if (expiry == null || expiry.trim().isEmpty()) {
            errorLabel.setText("Expiry date is required");
            return false;
        }

        if (!Pattern.matches("^(0[1-9]|1[0-2])/([0-9]{2})$", expiry)) {
            errorLabel.setText("Format must be MM/YY");
            return false;
        }

        // Check if the date is in the future
        try {
            String[] parts = expiry.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000; // Convert to 4-digit year

            YearMonth expiryDate = YearMonth.of(year, month);
            YearMonth currentDate = YearMonth.now();

            if (expiryDate.isBefore(currentDate)) {
                errorLabel.setText("Card has expired");
                return false;
            }
        } catch (Exception e) {
            errorLabel.setText("Invalid date format");
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    private boolean validateCvv(String cvv, Label errorLabel) {
        if (cvv == null || cvv.trim().isEmpty()) {
            errorLabel.setText("CVV is required");
            return false;
        }

        if (!Pattern.matches("^[0-9]{3,4}$", cvv)) {
            errorLabel.setText("CVV must be 3-4 digits");
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    // Luhn algorithm implementation for credit card validation
    private boolean isValidLuhn(String number) {
        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
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
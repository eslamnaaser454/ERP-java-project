package app.Market.Factory;

import app.Market.Cart.CartController;
import app.Market.Cart.CartItem;
import app.Suppliers.Preview.PreviewSupply.PreviewSupplyApplication;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

public class SimpleProductCard implements ProductCard {
    private final MarketService marketService;

    public SimpleProductCard(MarketService marketService) {
        this.marketService = marketService;
    }

    @Override
    public HBox createCard(Map<String, String> supply) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(5, 15, 5, 15));
        card.setAlignment(Pos.CENTER_LEFT);

        ImageView imageView = new ImageView();
        try {
            File file = new File(System.getProperty("user.dir") + supply.get("image"));
            if (!file.exists()) {
                file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\app\\images\\defaultImages\\NoneImage.jpg");
            }
            imageView.setImage(new Image(file.toURI().toURL().toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        imageView.setFitHeight(120);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);

        VBox details = new VBox(8);
        details.getChildren().addAll(
                createDetailRow("Product:", supply.get("name")),
                createDetailRow("Price:", "$" + supply.get("sell_price")),
                createDetailRow("Stock:", supply.get("qnt")),
                createDetailRow("Supplier:", marketService.getSupplierName(supply.get("supplier_id"))),
                createDetailRow("Location:", marketService.getStockLocation(supply.get("stock_id")))
        );
        details.setPrefWidth(350);

        // Get available quantity for validation - making it final but assigning it in a different way
        int stockQty;
        try {
            stockQty = Integer.parseInt(supply.get("qnt"));
        } catch (NumberFormatException e) {
            stockQty = 1;
        }
        // Now create a final reference
        final int availableQty = stockQty;

        // Create spinner with validation
        Spinner<Integer> spinnerObj;
        if (availableQty > 0) {
            spinnerObj = new Spinner<>(1, availableQty, 1);
        } else {
            spinnerObj = new Spinner<>(0, 0, 0); // No stock
        }
        // Make spinner final for lambda usage
        final Spinner<Integer> spinner = spinnerObj;

        spinner.setEditable(true);
        spinner.setPrefWidth(80);

        // Use safely final valueFactory for lambda
        final SpinnerValueFactory<Integer> valueFactory = spinner.getValueFactory();

        // Add text formatter to ensure only valid numbers are entered
        spinner.getEditor().setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Empty value not allowed
            if (newText.isEmpty()) {
                return null;
            }

            // Only allow digits
            if (!newText.matches("\\d*")) {
                return null;
            }

            try {
                int value = Integer.parseInt(newText);
                // Check if value exceeds max or is less than min
                if (value > availableQty) {
                    // Show warning for max value
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Quantity");
                    alert.setHeaderText(null);
                    alert.setContentText("Maximum available quantity is " + availableQty);
                    alert.show();
                    return null;
                }

                if (value < 1) {
                    // Don't allow values less than 1
                    return null;
                }
            } catch (NumberFormatException e) {
                return null;
            }

            return change;
        }));

        // Handle empty text and focus lost
        spinner.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                try {
                    String text = spinner.getEditor().getText();
                    int value = 1;

                    if (!text.isEmpty()) {
                        value = Integer.parseInt(text);
                    }

                    if (text.isEmpty() || value < 1) {
                        valueFactory.setValue(1);
                    } else if (value > availableQty) {
                        valueFactory.setValue(availableQty);
                    }
                } catch (NumberFormatException e) {
                    valueFactory.setValue(1);
                }
            }
        });

        Button addButton = new Button("Add to Cart");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        addButton.setOnAction(e -> {
            // Get spinner value with validation
            Integer qty;
            try {
                String textValue = spinner.getEditor().getText();
                if (textValue.isEmpty()) {
                    qty = 1; // Default value
                } else {
                    qty = Integer.parseInt(textValue);

                    // Validate qty is within range
                    if (qty < 1) {
                        qty = 1;
                    } else if (qty > availableQty) {
                        qty = availableQty;
                    }
                }
            } catch (NumberFormatException ex) {
                // Default to 1 if there's an error
                qty = 1;
                valueFactory.setValue(1);
            }

            marketService.handleAddToCart(supply, qty);
        });

        // New Preview Button - Modified to open in the same window
        Button previewButton = new Button("Preview");
        previewButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        previewButton.setOnAction(e -> {
            try {
                // Get the current window/stage
                Stage stage = (Stage) card.getScene().getWindow();

                // Create the preview application and start it in the same window
                PreviewSupplyApplication previewApp = new PreviewSupplyApplication(supply.get("id"));
                previewApp.start(stage);
            } catch (IOException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to open preview: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        VBox controlsContainer = new VBox(10, addButton, previewButton, spinner);
        controlsContainer.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(imageView, details, spacer, controlsContainer);
        return card;
    }

    private HBox createDetailRow(String label, String value) {
        Label lbl = new Label(label);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        Label val = new Label(value);
        return new HBox(5, lbl, val);
    }
}
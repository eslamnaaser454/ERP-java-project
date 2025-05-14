package app.Market.Cart;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CartController implements Initializable {

    public static ArrayList<CartItem> cartItems = new ArrayList<>();

    @FXML private VBox cartContainer;
    @FXML private Label totalLabel;
    @FXML private Button checkoutButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshCartView();
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
            });

            // Add all components to the card
            card.getChildren().addAll(imageView, details, removeButton);
            cartContainer.getChildren().add(card);
            total += item.getTotalPrice();
        }

        totalLabel.setText(String.format("Total: $%.2f", total));
        checkoutButton.setDisable(cartItems.isEmpty());
    }

    @FXML
    private void handleCheckout() {
        cartItems.clear();
        refreshCartView();
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14;");
        return label;
    }
}
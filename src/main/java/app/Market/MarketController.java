package app.Market;

import app.Classes.DataBaseConnection;
import app.Market.Cart.CartApplication;
import app.Market.Factory.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MarketController implements Initializable {

    @FXML private VBox supplyContainer;
    @FXML private TextField searchField;
    @FXML private Label cartBadge;

    private MarketService marketService;
    private ProductCardFactory productCardFactory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DataBaseConnection dbConnection = new DataBaseConnection(System.getProperty("user.dir") + "\\src\\main\\resources\\database.db");

        // Setup cart badge styling
        cartBadge.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-background-radius: 10px; -fx-padding: 2px 6px; -fx-font-weight: bold;");

        marketService = new MarketService(dbConnection, cartBadge);
        productCardFactory = new SimpleProductCardFactory(marketService);

        loadSupplies();
    }

    private void loadSupplies() {
        supplyContainer.getChildren().clear();
        marketService.getSupplies().forEach(supply ->
                supplyContainer.getChildren().add(productCardFactory.createProductCard().createCard(supply)));
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String query = searchField.getText().trim();
        supplyContainer.getChildren().clear();
        marketService.searchSupplies(query).forEach(supply ->
                supplyContainer.getChildren().add(productCardFactory.createProductCard().createCard(supply)));
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadSupplies();
        searchField.clear();
    }

    @FXML
    private void GoToCart(MouseEvent event) {
        CartApplication cartApp = new CartApplication();
        Stage stage = (Stage) supplyContainer.getScene().getWindow();
        try {
            cartApp.start(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package app.Market.Factory;

import javafx.scene.layout.HBox;

import java.util.Map;

public interface ProductCard {
    HBox createCard(Map<String, String> supply);
}

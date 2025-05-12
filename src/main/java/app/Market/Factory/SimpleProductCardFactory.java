package app.Market.Factory;

public class SimpleProductCardFactory implements ProductCardFactory {
    private final MarketService marketService;

    public SimpleProductCardFactory(MarketService marketService) {
        this.marketService = marketService;
    }

    @Override
    public ProductCard createProductCard() {
        return new SimpleProductCard(marketService);
    }
}

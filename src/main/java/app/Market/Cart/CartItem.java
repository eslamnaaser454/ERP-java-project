package app.Market.Cart;

public class CartItem {
    private final int supplyId;
    private final String name;
    private final double unitPrice;
    private int quantity;
    private final String imagePath;

    public CartItem(int supplyId, String name, double unitPrice, int quantity, String imagePath) {
        this.supplyId = supplyId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    // Getters
    public int getSupplyId() { return supplyId; }

    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public String getImagePath() { return imagePath; }
    public double getTotalPrice() { return unitPrice * quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
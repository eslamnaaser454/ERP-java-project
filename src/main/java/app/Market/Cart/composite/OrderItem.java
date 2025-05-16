package app.Market.Cart.composite;

import app.Market.Cart.CartItem;

/**
 * Leaf component in the composite pattern
 * Represents an individual item in the order
 */
public class OrderItem extends OrderComponent {
    private CartItem item;

    public OrderItem(CartItem item) {
        this.item = item;
    }

    @Override
    public double getTotal() {
        return item.getTotalPrice();
    }

    @Override
    public String getComponentDetails() {
        return String.format("%s (x%d) - $%.2f",
                item.getName(),
                item.getQuantity(),
                item.getTotalPrice());
    }

    public CartItem getItem() {
        return item;
    }
}
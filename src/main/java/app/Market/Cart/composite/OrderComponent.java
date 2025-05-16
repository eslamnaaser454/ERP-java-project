package app.Market.Cart.composite;

/**
 * Base component in the composite pattern
 * This is the abstract component that both leaves (OrderItem) and composites (Order) will extend
 */
public abstract class OrderComponent {
    public abstract double getTotal();
    public abstract String getComponentDetails();
}
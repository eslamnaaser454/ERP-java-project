package app.Market.Cart.composite;

import app.Market.Cart.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite component in the composite pattern
 * Represents an order that contains multiple items
 */
public class Order extends OrderComponent {
    private int invoiceId;
    private String customerName;
    private String customerPhone;
    private List<OrderComponent> components = new ArrayList<>();
    private String paymentMethod;
    private String orderDate;

    public Order(String customerName, String customerPhone) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }

    public void addComponent(OrderComponent component) {
        components.add(component);
    }

    public void addAllItems(List<CartItem> items) {
        for (CartItem item : items) {
            components.add(new OrderItem(item));
        }
    }

    @Override
    public double getTotal() {
        return components.stream().mapToDouble(OrderComponent::getTotal).sum();
    }

    @Override
    public String getComponentDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Order Summary:\n");
        details.append("Customer: ").append(customerName).append("\n");
        details.append("Phone: ").append(customerPhone).append("\n");
        details.append("Items:\n");

        for (OrderComponent component : components) {
            details.append("- ").append(component.getComponentDetails()).append("\n");
        }

        details.append("Total: $").append(String.format("%.2f", getTotal())).append("\n");
        details.append("Payment Method: ").append(paymentMethod).append("\n");

        return details.toString();
    }

    // Getters and Setters
    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public List<OrderComponent> getComponents() {
        return components;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
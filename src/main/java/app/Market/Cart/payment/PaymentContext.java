package app.Market.Cart.payment;

import app.Market.Cart.composite.Order;

/**
 * Context class for the payment strategy pattern
 * This class maintains a reference to the current payment strategy
 * and delegates the payment processing to it
 */
public class PaymentContext {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public boolean processPayment(Order order) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("No payment strategy selected");
        }
        return paymentStrategy.processPayment(order);
    }

    public String getTransactionDetails() {
        if (paymentStrategy == null) {
            return "No payment processed";
        }
        return paymentStrategy.getTransactionDetails();
    }

    public String getMethodName() {
        if (paymentStrategy == null) {
            return "None";
        }
        return paymentStrategy.getMethodName();
    }
}
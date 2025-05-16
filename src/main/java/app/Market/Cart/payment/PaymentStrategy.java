package app.Market.Cart.payment;

import app.Market.Cart.composite.Order;

/**
 * Strategy interface for payment processing
 * Each payment method implements this interface
 */
public interface PaymentStrategy {
    /**
     * Process a payment for an order
     * @param order The order to process payment for
     * @return true if payment successful, false otherwise
     */
    boolean processPayment(Order order);

    /**
     * Get details about the payment transaction
     * @return A string with payment details
     */
    String getTransactionDetails();

    /**
     * Get the name of the payment method
     * @return Payment method name
     */
    String getMethodName();
}
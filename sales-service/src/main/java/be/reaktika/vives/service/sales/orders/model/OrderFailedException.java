package be.reaktika.vives.service.sales.orders.model;

public class OrderFailedException extends RuntimeException {
    public OrderFailedException(String message) {
        super(message);
    }
}

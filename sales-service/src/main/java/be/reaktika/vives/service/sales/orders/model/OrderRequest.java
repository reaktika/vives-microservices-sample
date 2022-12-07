package be.reaktika.vives.service.sales.orders.model;

import java.util.Collection;

public record OrderRequest(Long customerId, Collection<OrderItem> orderdItems) {
}

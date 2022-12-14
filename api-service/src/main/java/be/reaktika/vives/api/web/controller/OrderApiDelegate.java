package be.reaktika.vives.api.web.controller;

import be.reaktika.vives.api.ports.SalesPort;
import be.reaktika.vives.furniture.api.model.product.Order;
import be.reaktika.vives.furniture.api.model.product.OrderConfirmation;
import be.reaktika.vives.service.sales.orders.model.ConfirmedOrder;
import be.reaktika.vives.service.sales.orders.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderApiDelegate implements be.reaktika.vives.furniture.api.product.OrderApiDelegate {

    private final Logger logger = LoggerFactory.getLogger(OrderApiDelegate.class);
    private final SalesPort sales;

    public OrderApiDelegate(SalesPort sales) {
        this.sales = sales;
    }

    @Override
    public ResponseEntity<OrderConfirmation> orderItem(List<Order> order) {
        logger.info("Received order, forwarding to the sales service");
        var confirmedOrder = sales.orderItems(order).block();
        logger.info("confirmation received " + confirmedOrder);
        var orderConfirmation = mapToResponse(confirmedOrder);
        return ResponseEntity.ok(orderConfirmation);
    }

    private OrderConfirmation mapToResponse(ConfirmedOrder confirmedOrder) {
        return new OrderConfirmation()
                .orderId(confirmedOrder.orderId())
                .orders(confirmedOrder.request()
                        .orderdItems().stream()
                        .map(this::mapToResponse)
                        .collect(Collectors.toList()));

    }

    private Order mapToResponse(OrderItem item) {
       return new Order()
               .productId(item.productId())
               .quantity(item.quantity());
    }
}

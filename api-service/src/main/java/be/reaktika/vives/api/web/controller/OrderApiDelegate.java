package be.reaktika.vives.api.web.controller;

import be.reaktika.vives.api.ports.SalesPort;
import be.reaktika.vives.furniture.api.model.product.Order;
import be.reaktika.vives.service.sales.orders.model.ConfirmedOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderApiDelegate implements be.reaktika.vives.furniture.api.product.OrderApiDelegate {

    private final Logger logger = LoggerFactory.getLogger(OrderApiDelegate.class);
    private final SalesPort sales;

    public OrderApiDelegate(SalesPort sales) {
        this.sales = sales;
    }

    @Override
    public ResponseEntity<Void> orderItem(List<Order> order) {
        logger.info("ordering from the sales service");
        ConfirmedOrder confirmation = sales.orderItems(order).block();
        logger.info("confirmation received " + confirmation);
        return be.reaktika.vives.furniture.api.product.OrderApiDelegate.super.orderItem(order);
    }
}

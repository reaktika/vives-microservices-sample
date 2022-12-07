package be.reaktika.vives.service.sales;

import be.reaktika.vives.service.sales.orders.model.ConfirmedOrder;
import be.reaktika.vives.service.sales.orders.model.OrderFailedException;
import be.reaktika.vives.service.sales.orders.model.OrderRequest;
import be.reaktika.vives.service.sales.ports.InventoryItemClaim;
import be.reaktika.vives.service.sales.ports.InventoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final static AtomicLong orderCounter = new AtomicLong(0);

    private final KafkaTemplate<String, ConfirmedOrder> orderProducerTemplate;
    private final InventoryService inventoryService;
    private final String orderTopic;

    public OrderService(KafkaTemplate<String, ConfirmedOrder> orderProducerTemplate,
                        InventoryService inventoryService,
                        @Value("${vives.kafka.order.topic: orders}")String orderTopic) {
        this.orderProducerTemplate = orderProducerTemplate;
        this.inventoryService = inventoryService;
        this.orderTopic = orderTopic;
    }

    public ConfirmedOrder placeOrder(OrderRequest order) {
        logger.info("placing order for customer {} with {} orderitems in total", order.customerId(), order.orderdItems().size());
        var claims = order.orderdItems().stream()
                .map(requestedItem -> new InventoryItemClaim(requestedItem.productId(), requestedItem.quantity()))
                .toList();

        var claimResult = inventoryService.claimItems(claims);
        if (claimResult.isSuccess()) {
            final var confirmation = new ConfirmedOrder(orderCounter.incrementAndGet(), order);
            logger.info("publishing confirmed order on topic " + orderTopic);
            orderProducerTemplate.send(orderTopic, confirmation);
            return confirmation;
        } else {
            throw new OrderFailedException("failed to order the items because claiming the items failed");
        }
    }

    public CompletableFuture<ConfirmedOrder> placeOrderAsync(OrderRequest order) {
        logger.info("placing async order for customer {} with {} orderitems in total", order.customerId(), order.orderdItems().size());
        var claims = order.orderdItems().stream().map(requestedItem -> new InventoryItemClaim(requestedItem.productId(), requestedItem.quantity())).toList();

        return inventoryService.claimItemsAsync(claims).thenApply(result -> {
            if (result.isSuccess()) {
                final var confirmation = new ConfirmedOrder(orderCounter.incrementAndGet(), order);
                logger.info("publishing confirmed order on topic " + orderTopic);
                orderProducerTemplate.send(orderTopic, confirmation);
                return confirmation;
            } else {
                throw new OrderFailedException("failed to order the items because claiming the items failed");
            }
        });
    }


}

package be.reaktika.vives.service.sales.adapters;

import be.reaktika.vives.service.sales.OrderService;
import be.reaktika.vives.service.sales.orders.model.ConfirmedOrder;
import be.reaktika.vives.service.sales.orders.model.OrderItem;
import be.reaktika.vives.service.sales.orders.model.OrderRequest;
import be.reaktika.vives.furniture.api.model.product.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("sales")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);


    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "order", consumes = "application/json")
    public ResponseEntity<ConfirmedOrder> orderItem(RequestEntity<List<Order>> request, @RequestParam("requestId") String requestId) {
        logger.info("controller received order {} with id {}",request, requestId);
        var orderedItems =  request.getBody().stream().map(this::mapOrderToOrderItem).toList();
        ConfirmedOrder confirmation = orderService.placeOrder(
                new OrderRequest(101L, orderedItems, requestId));
        logger.info("controller done processing request");
        return ResponseEntity.ok(confirmation);
    }

    private OrderItem mapOrderToOrderItem(Order order) {
        return new OrderItem(order.getProductId(), order.getQuantity());
    }

    @PostMapping(value = "order-async", consumes = "application/json")
    public CompletableFuture<ResponseEntity<ConfirmedOrder>> orderItemAsync(RequestEntity<List<Order>> request, @RequestParam("requestId") String requestId) {
        logger.info("controller received order {}",request);
        var orderedItems =  request.getBody().stream().map(this::mapOrderToOrderItem).toList();
        CompletableFuture<ResponseEntity<ConfirmedOrder>> futureResult = orderService.placeOrderAsync(
                        new OrderRequest(101L, orderedItems, requestId))
                .thenApply(confirmation -> {
                    logger.info("controller receives confirmation");
                    return ResponseEntity.ok(confirmation);
                });
        logger.info("controller done processing request");
        return futureResult;
    }
}

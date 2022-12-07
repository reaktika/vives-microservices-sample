package be.reaktika.vives.service.sales.adapters;

import be.reaktika.vives.service.sales.OrderService;
import be.reaktika.vives.service.sales.orders.model.ConfirmedOrder;
import be.reaktika.vives.service.sales.orders.model.OrderItem;
import be.reaktika.vives.service.sales.orders.model.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
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
    public ResponseEntity<ConfirmedOrder> orderItem(RequestEntity request) {
        logger.info("controller received order {}",request);
        ConfirmedOrder confirmation = orderService.placeOrder(
                new OrderRequest(101L, List.of(
                        new OrderItem(1L, 10),
                        new OrderItem(2L, 5))));
        logger.info("controller done processing request");
        return ResponseEntity.ok(confirmation);
    }

    @PostMapping(value = "order-async", consumes = "application/json")
    public CompletableFuture<ResponseEntity<ConfirmedOrder>> orderItemAsync(RequestEntity request) {
        logger.info("controller received order {}",request);
        CompletableFuture<ResponseEntity<ConfirmedOrder>> futureResult = orderService.placeOrderAsync(
                        new OrderRequest(101L, List.of(
                                new OrderItem(1L, 10),
                                new OrderItem(2L, 5))))
                .thenApply(confirmation -> {
                    logger.info("controller receives confirmation");
                    return ResponseEntity.ok(confirmation);
                });
        logger.info("controller done processing request");
        return futureResult;
    }
}

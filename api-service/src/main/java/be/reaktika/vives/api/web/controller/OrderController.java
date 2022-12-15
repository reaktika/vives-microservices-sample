package be.reaktika.vives.api.web.controller;

import be.reaktika.vives.api.OrderConsumerService;
import be.reaktika.vives.api.ports.SalesPort;
import be.reaktika.vives.furniture.api.model.product.Order;
import be.reaktika.vives.furniture.api.model.product.OrderConfirmation;
import be.reaktika.vives.furniture.api.product.OrderApiController;
import be.reaktika.vives.service.sales.orders.model.ConfirmedOrder;
import be.reaktika.vives.service.sales.orders.model.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
public class OrderController extends OrderApiController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final SalesPort sales;
    private final OrderConsumerService orderConsumerService;

    public OrderController(NativeWebRequest request, SalesPort sales, OrderConsumerService orderConsumerService, ObjectMapper objectMapper) {
        super(request);
        this.sales = sales;
        this.orderConsumerService = orderConsumerService;

    }

    @Override
    public CompletableFuture<ResponseEntity<OrderConfirmation>> orderItem(List<Order> order) {
        UUID uuid = UUID.randomUUID();
        logger.info("Received order, forwarding to the sales service, using uuid " + uuid);
        CompletableFuture<ResponseEntity<OrderConfirmation>> futureResponse = new CompletableFuture<>();
        sales.orderItems(order, uuid.toString()).subscribe();
        logger.info("registering callback");
        orderConsumerService.registerRequest(uuid, confirmedOrder -> {
            logger.info("executing callback");
            futureResponse.complete(ResponseEntity.ok(mapToResponse(confirmedOrder)));
        });


        return futureResponse;
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

package be.reaktika.vives.api.ports;

import be.reaktika.vives.furniture.api.model.product.Order;
import be.reaktika.vives.furniture.api.model.product.Product;
import be.reaktika.vives.service.sales.orders.model.ConfirmedOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class SalesPort {

    private final Logger logger = LoggerFactory.getLogger(SalesPort.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public SalesPort(@Qualifier("sales") WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public Mono<ConfirmedOrder> orderItems(List<Order> orderList) {
        return webClient.method(HttpMethod.POST)
                .uri("/order")
                .bodyValue(convertOrdersToBody(orderList))
                .exchangeToMono(resp -> resp.bodyToMono(String.class))
                .map(this::convertStringBodyToConfirmation);
    }

    private String convertOrdersToBody(List<Order> body) {
        logger.info("converting list of orders to string ");
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ConfirmedOrder convertStringBodyToConfirmation(String body) {
        logger.info("converting received body to list " + body);
        try {
            return objectMapper.readValue(body, ConfirmedOrder.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}

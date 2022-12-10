package be.reaktika.vives.api.ports;

import be.reaktika.vives.furniture.api.model.product.Product;
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
public class LogisticsPort {

    private final Logger logger = LoggerFactory.getLogger(LogisticsPort.class);

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public LogisticsPort(@Qualifier("logistics") WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public Mono<List<Product>> getProducts() {
        WebClient.RequestBodySpec request = webClient.method(HttpMethod.GET)
                .uri("/products");

        return request.exchangeToMono(resp -> resp.bodyToMono(String.class))
                .map(this::convertStringBodyToList);
    }

    private List<Product> convertStringBodyToList(String body) {
        logger.info("converting received body to list " + body);
        try {
            return Arrays.asList(objectMapper.readValue(body, Product[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

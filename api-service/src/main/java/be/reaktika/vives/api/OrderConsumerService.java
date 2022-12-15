package be.reaktika.vives.api;

import be.reaktika.vives.service.sales.orders.model.ConfirmedOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class OrderConsumerService {

    private final Logger logger = LoggerFactory.getLogger(OrderConsumerService.class);

    private final Map<String, Consumer<ConfirmedOrder>> inflightRequets = new ConcurrentHashMap<>();

    @KafkaListener(topics = "${vives.kafka.order.topic}", groupId = "${vives.kafka.consumer.groupId}")
    public void processConfirmedOrder(ConfirmedOrder order) {
        logger.info("topicListener received confirmed {}", order );
        Optional.ofNullable(inflightRequets.remove(order.request().requestId()))
                .ifPresentOrElse(callback -> callback.accept(order),
                        () -> logger.info("received confirmed order for unknown request {}", order.request().requestId()));
    }

    public void registerRequest(UUID uuid, Consumer<ConfirmedOrder> confirmationCallback) {
        inflightRequets.put(uuid.toString(), confirmationCallback);
    }
}

package be.reaktika.vives.api;

import be.reaktika.vives.service.sales.orders.model.ConfirmedOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumerService {

    private final Logger logger = LoggerFactory.getLogger(OrderConsumerService.class);

    @KafkaListener(topics = "${vives.kafka.order.topic}", groupId = "${vives.kafka.consumer.groupId}")
    public void processConfirmedOrder(ConfirmedOrder order) {
        logger.info("topicListener received order {}", order );
    }
}

package be.reaktika.vives.service.sales.config;


import be.reaktika.vives.service.sales.orders.model.ConfirmedOrder;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfig {

    @Value(value = "${vives.kafka.bootstrapAddress}")
    private String bootstrapAddress;
    @Bean
    public ProducerFactory<String, ConfirmedOrder> orderProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        //the keys are strings
        configProps.put(
                org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        //the values are json
        configProps.put(
                org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ConfirmedOrder> orderProducerTemplate() {
        return new KafkaTemplate<>(orderProducerFactory());
    }
}

package com.ryazancev.purchase.kafka;

import com.ryazancev.dto.customer.UpdateBalanceRequest;
import com.ryazancev.dto.product.UpdateQuantityRequest;
import org.apache.kafka.clients.producer.ProducerConfig;
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
public class PurchaseProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);

        return props;
    }

    @Bean
    public ProducerFactory<
            String, UpdateBalanceRequest> customerProducerFactory() {

        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, UpdateBalanceRequest> customerKafkaTemplate(
            ProducerFactory<
                    String, UpdateBalanceRequest> customerProducerFactory) {

        return new KafkaTemplate<>(customerProducerFactory);
    }

    @Bean
    public ProducerFactory<
            String, UpdateQuantityRequest> productProducerFactory() {

        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, UpdateQuantityRequest> productKafkaTemplate(
            ProducerFactory<
                    String, UpdateQuantityRequest> productProducerFactory) {

        return new KafkaTemplate<>(productProducerFactory);
    }
}

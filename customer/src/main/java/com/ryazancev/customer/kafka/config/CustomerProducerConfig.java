package com.ryazancev.customer.kafka.config;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CustomerProducerConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> longValueProducerConfig() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class);

        return props;
    }

    @Bean
    public ProducerFactory<String, Long> longValueProducerFactory() {

        return new DefaultKafkaProducerFactory<>(longValueProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, Long> longValueKafkaTemplate(
            ProducerFactory<String, Long> reviewProducerFactory) {

        return new KafkaTemplate<>(reviewProducerFactory);
    }
}

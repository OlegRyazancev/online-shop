package com.ryazancev.admin.kafka.config;

import com.ryazancev.dto.admin.ObjectRequest;
import com.ryazancev.dto.admin.RegistrationRequestDto;
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
public class AdminProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    public Map<String, Object> registerProducerConfig() {
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
    public ProducerFactory<String,
            RegistrationRequestDto> registerProducerFactory() {

        return new DefaultKafkaProducerFactory<>(registerProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, RegistrationRequestDto> registerKafkaTemplate(
            ProducerFactory<String, RegistrationRequestDto
                    > producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }


    public Map<String, Object> changeStatusProducerConfig() {

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
    public ProducerFactory<String, ObjectRequest> changeStatusProducerFactory() {

        return new DefaultKafkaProducerFactory<>(changeStatusProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, ObjectRequest> changeStatusKafkaTemplate(
            ProducerFactory<String, ObjectRequest> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }
}

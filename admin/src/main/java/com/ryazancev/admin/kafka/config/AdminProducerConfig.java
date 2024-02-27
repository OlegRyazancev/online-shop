package com.ryazancev.admin.kafka.config;

import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.UserLockRequest;
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

/**
 * @author Oleg Ryazancev
 */

@Configuration
public class AdminProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    public Map<String, Object> jsonProducerConfig() {
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
    public KafkaTemplate<String, RegistrationRequestDto>
    registerKafkaTemplate() {

        return createKafkaTemplate(
                createProducerFactory(RegistrationRequestDto.class));
    }

    @Bean
    public KafkaTemplate<String, ObjectRequest>
    changeStatusKafkaTemplate() {

        return createKafkaTemplate(
                createProducerFactory(ObjectRequest.class));
    }

    @Bean
    public KafkaTemplate<String, UserLockRequest>
    toggleUserLockKafkaTemplate() {

        return createKafkaTemplate(
                createProducerFactory(UserLockRequest.class));
    }

    private <T> ProducerFactory<String, T>
    createProducerFactory(Class<T> valueType) {

        return new DefaultKafkaProducerFactory<>(jsonProducerConfig());
    }


    private <T> KafkaTemplate<String, T>
    createKafkaTemplate(ProducerFactory<String, T> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }


}

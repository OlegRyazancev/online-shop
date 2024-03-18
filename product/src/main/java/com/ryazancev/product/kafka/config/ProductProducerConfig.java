package com.ryazancev.product.kafka.config;

import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.notification.NotificationRequest;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
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
public class ProductProducerConfig {

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

    private <T> ProducerFactory<String, T>
    createJsonProducerFactory(Class<T> valueType) {

        return new DefaultKafkaProducerFactory<>(jsonProducerConfig());
    }

    private <T> ProducerFactory<String, T>
    createLongValueProducerFactory(Class<T> valueType) {

        return new DefaultKafkaProducerFactory<>(longValueProducerConfig());
    }

    private <T> KafkaTemplate<String, T>
    createKafkaTemplate(ProducerFactory<String, T> producerFactory) {

        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTemplate<String, RegistrationRequestDto>
    adminKafkaTemplate() {

        return createKafkaTemplate(
                createJsonProducerFactory(RegistrationRequestDto.class));
    }

    @Bean
    public KafkaTemplate<String, MailDto>
    mailKafkaTemplate() {

        return createKafkaTemplate(
                createJsonProducerFactory(MailDto.class));
    }

    @Bean
    public KafkaTemplate<String, Long>
    reviewKafkaTemplate() {

        return createKafkaTemplate(
                createLongValueProducerFactory(Long.class));
    }

    @Bean
    public KafkaTemplate<String, NotificationRequest>
    notificationKafkaTemplate(){

        return createKafkaTemplate(
                createJsonProducerFactory(NotificationRequest.class));
    }
}

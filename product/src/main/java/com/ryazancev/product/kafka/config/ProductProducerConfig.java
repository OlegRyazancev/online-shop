package com.ryazancev.product.kafka.config;

import com.ryazancev.dto.admin.RegistrationRequestDto;
import com.ryazancev.dto.mail.MailDto;
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

    @Bean
    public KafkaTemplate<String, RegistrationRequestDto>
    adminKafkaTemplate() {

        return createKafkaTemplate(
                createProducerFactory(RegistrationRequestDto.class));
    }

    @Bean
    public KafkaTemplate<String, MailDto>
    mailKafkaTemplate() {

        return createKafkaTemplate(
                createProducerFactory(MailDto.class));
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

    @Bean
    public ProducerFactory<String, Long> reviewProducerFactory() {

        return new DefaultKafkaProducerFactory<>(longValueProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, Long> reviewKafkaTemplate(
            ProducerFactory<String, Long> reviewProducerFactory) {

        return new KafkaTemplate<>(reviewProducerFactory);
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

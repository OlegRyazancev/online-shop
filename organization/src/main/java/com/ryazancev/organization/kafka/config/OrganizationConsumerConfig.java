package com.ryazancev.organization.kafka.config;

import com.ryazancev.dto.admin.RegistrationRequestDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrganizationConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> registrationConsumerConfig() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        return props;
    }

    @Bean
    public ConsumerFactory<String, RegistrationRequestDTO> registrationConsumerFactory() {

        JsonDeserializer<RegistrationRequestDTO> jsonDeserializer =
                new JsonDeserializer<>();

        jsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                registrationConsumerConfig(),
                new StringDeserializer(),
                jsonDeserializer);
    }

    @Bean
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, RegistrationRequestDTO>>
    registrationMessageFactory(ConsumerFactory<
            String, RegistrationRequestDTO> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<
                String, RegistrationRequestDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    public Map<String, Object> freezeConsumerConfig() {

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                LongDeserializer.class);

        return props;
    }

    @Bean
    public ConsumerFactory<String, Long> freezeConsumerFactory() {

        return new DefaultKafkaConsumerFactory<>(freezeConsumerConfig());
    }

    @Bean
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String,
                    Long>> freezeMessageFactory(
            ConsumerFactory<String, Long> freezeConsumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, Long> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(freezeConsumerFactory);

        return factory;
    }

}

package com.ryazancev.admin.kafka.config;

import com.ryazancev.dto.admin.RegistrationRequestDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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
public class AdminConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    public Map<String, Object> consumerConfig() {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        return props;
    }

    @Bean
    public ConsumerFactory
            <String, RegistrationRequestDto> consumerFactory() {

        JsonDeserializer<RegistrationRequestDto> jsonDeserializer =
                new JsonDeserializer<>();

        jsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                consumerConfig(),
                new StringDeserializer(),
                jsonDeserializer);
    }

    @Bean
    public KafkaListenerContainerFactory
            <ConcurrentMessageListenerContainer
                    <String, RegistrationRequestDto>> messageFactory(
            ConsumerFactory
                    <String, RegistrationRequestDto>
                    consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<
                String, RegistrationRequestDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

}

package com.ryazancev.product.kafka.config;

import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.product.UpdateQuantityRequest;
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

/**
 * @author Oleg Ryazancev
 */

@Configuration
public class ProductConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    public Map<String, Object> baseConsumerConfig() {

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return props;
    }

    @Bean
    public ConsumerFactory<String, UpdateQuantityRequest>
    updateQuantityConsumerFactory() {

        JsonDeserializer<UpdateQuantityRequest> jsonDeserializer =
                new JsonDeserializer<>();

        jsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerConfig(),
                new StringDeserializer(),
                jsonDeserializer);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<
            String, UpdateQuantityRequest>>
    updateQuantityMessageFactory(ConsumerFactory<
            String, UpdateQuantityRequest> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String,
                UpdateQuantityRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, RegistrationRequestDto>
    changeRegistrationStatusConsumerFactory() {

        JsonDeserializer<RegistrationRequestDto> jsonDeserializer =
                new JsonDeserializer<>();

        jsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerConfig(),
                new StringDeserializer(),
                jsonDeserializer);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<
            String, RegistrationRequestDto>>
    changeRegistrationStatusMessageFactory(ConsumerFactory<
            String, RegistrationRequestDto> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String,
                RegistrationRequestDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, Long>
    longValueConsumerFactory() {

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerConfig(),
                new StringDeserializer(),
                new LongDeserializer());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<
            String, Long>>
    longValueMessageFactory(ConsumerFactory<
            String, Long> longValueConsumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, Long> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(longValueConsumerFactory);

        return factory;
    }


    @Bean
    public ConsumerFactory<String, ObjectRequest>
    changeObjectStatusConsumerFactory() {

        JsonDeserializer<ObjectRequest> jsonDeserializer =
                new JsonDeserializer<>();

        jsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerConfig(),
                new StringDeserializer(),
                jsonDeserializer);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<
            String, ObjectRequest>>
    changeObjectStatusMessageFactory(ConsumerFactory<
            String, ObjectRequest> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<
                String, ObjectRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}

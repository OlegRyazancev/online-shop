package com.ryazancev.purchase.service.impl;

import com.ryazancev.dto.customer.UpdateBalanceRequest;
import com.ryazancev.dto.product.UpdateQuantityRequest;
import com.ryazancev.purchase.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<
            String, UpdateQuantityRequest> productKafkaTemplate;
    private final KafkaTemplate<
            String, UpdateBalanceRequest> customerKafkaTemplate;

    @Value("${spring.kafka.topic.customer}")
    private String customerTopic;

    @Value("${spring.kafka.topic.product}")
    private String productTopic;

    public KafkaProducerServiceImpl(
            @Qualifier("productKafkaTemplate")
            KafkaTemplate<String, UpdateQuantityRequest> productKafkaTemplate,
            @Qualifier("customerKafkaTemplate")
            KafkaTemplate<String, UpdateBalanceRequest> customerKafkaTemplate) {

        this.productKafkaTemplate = productKafkaTemplate;
        this.customerKafkaTemplate = customerKafkaTemplate;
    }

    @Override
    public void sendMessageToProductTopic(UpdateQuantityRequest message) {
        try {
            productKafkaTemplate.send(productTopic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessageToCustomerTopic(UpdateBalanceRequest message) {
        try {
            customerKafkaTemplate.send(customerTopic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

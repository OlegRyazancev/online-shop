package com.ryazancev.purchase.kafka;

import com.ryazancev.common.dto.customer.UpdateBalanceRequest;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.product.UpdateQuantityRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Oleg Ryazancev
 */

@Service
@Slf4j
public class PurchaseProducerService {

    private final KafkaTemplate<
            String, UpdateQuantityRequest> productKafkaTemplate;
    private final KafkaTemplate<
            String, UpdateBalanceRequest> customerKafkaTemplate;


    @Value("${spring.kafka.topic.customer.update}")
    private String customerTopic;

    @Value("${spring.kafka.topic.product.update}")
    private String productTopic;


    public PurchaseProducerService(
            @Qualifier("productKafkaTemplate")
            KafkaTemplate<String, UpdateQuantityRequest>
                    productKafkaTemplate,
            @Qualifier("customerKafkaTemplate")
            KafkaTemplate<String, UpdateBalanceRequest>
                    customerKafkaTemplate) {

        this.productKafkaTemplate = productKafkaTemplate;
        this.customerKafkaTemplate = customerKafkaTemplate;
    }

    public void sendMessageToProductTopic(UpdateQuantityRequest request) {

        log.info("Received request to update product's quantity with id: {} " +
                        "quantity: {} on topic: {}",
                request.getProductId(),
                request.getQuantityInStock(),
                productTopic);
        try {

            log.debug("Sending update quantity request...");
            productKafkaTemplate.send(productTopic, request);

            log.debug("Request to {} was sent", productTopic);

        } catch (Exception e) {

            log.error("Failed to send request to {}: {}",
                    productTopic,
                    e.getMessage());
        }
    }

    public void sendMessageToCustomerTopic(UpdateBalanceRequest request) {

        log.info("Received request to update user's balance with id: {} " +
                        "balance: {} on topic: {}",
                request.getCustomerId(),
                request.getBalance(),
                customerTopic);
        try {

            log.debug("Sending update balance request...");
            customerKafkaTemplate.send(customerTopic, request);

            log.debug("Request to {} was sent", customerTopic);

        } catch (Exception e) {

            log.error("Failed to send request to {}: {}",
                    customerTopic,
                    e.getMessage());
        }
    }
}

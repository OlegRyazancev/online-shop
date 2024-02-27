package com.ryazancev.purchase.kafka;

import com.ryazancev.common.dto.customer.UpdateBalanceRequest;
import com.ryazancev.common.dto.product.UpdateQuantityRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Oleg Ryazancev
 */

@Service
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
            KafkaTemplate<String, UpdateQuantityRequest> productKafkaTemplate,
            @Qualifier("customerKafkaTemplate")
            KafkaTemplate<String, UpdateBalanceRequest> customerKafkaTemplate) {

        this.productKafkaTemplate = productKafkaTemplate;
        this.customerKafkaTemplate = customerKafkaTemplate;
    }

    public void sendMessageToProductTopic(UpdateQuantityRequest message) {
        try {
            productKafkaTemplate.send(productTopic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToCustomerTopic(UpdateBalanceRequest message) {
        try {
            customerKafkaTemplate.send(customerTopic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

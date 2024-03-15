package com.ryazancev.customer.kafka;

import com.ryazancev.common.dto.customer.UpdateBalanceRequest;
import com.ryazancev.customer.model.Customer;
import com.ryazancev.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerMessageListener {


    private final CustomerService customerService;

    @KafkaListener(
            topics = "${spring.kafka.topic.customer.update}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void updateCustomerBalance(UpdateBalanceRequest request) {

        log.info("Received message to update customer balance with id: {}",
                request.getCustomerId());

        try {

            log.trace("Updating customer balance...");
            Customer customer =
                    customerService.updateBalance(request);

            log.info("Customer was updated. Actual balance: {}",
                    customer.getBalance());

        } catch (Exception e) {

            log.error("Customer was not updated");
        }

    }
}

package com.ryazancev.customer.kafka;

import com.ryazancev.customer.service.CustomerService;
import com.ryazancev.dto.customer.UpdateBalanceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {


    private final CustomerService customerService;

    @KafkaListener(
            topics = "${spring.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void consumeMail(UpdateBalanceRequest request) {
        log.info("Received message to update customer: {}, set balance to: {}",
                request.getCustomerId(),
                request.getBalance());

        log.info("Updating customer...");

        customerService.updateBalance(request);

        log.info("Customer successfully updated");
    }
}

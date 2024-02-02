package com.ryazancev.product.service.impl;

import com.ryazancev.dto.product.UpdateQuantityRequest;
import com.ryazancev.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListeners {


    private final ProductService productService;

    @KafkaListener(
            topics = "${spring.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void consumeMail(UpdateQuantityRequest request) {
        log.info("Received message to update quantity of: {}, set to: {}",
                request.getProductId(),
                request.getQuantityInStock());

        log.info("Updating product...");

        productService.updateQuantity(request);

        log.info("Product successfully updated");
    }
}

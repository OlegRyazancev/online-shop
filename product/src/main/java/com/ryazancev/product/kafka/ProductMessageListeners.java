package com.ryazancev.product.kafka;

import com.ryazancev.dto.admin.RegistrationRequestDTO;
import com.ryazancev.dto.product.UpdateQuantityRequest;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMessageListeners {


    private final ProductService productService;

    @KafkaListener(
            topics = "${spring.kafka.topic.product.update}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "updateQuantityMessageFactory"
    )
    public void updateQuantity(UpdateQuantityRequest request) {

        log.info("Received message to update quantity of: {}, set to: {}",
                request.getProductId(),
                request.getQuantityInStock());

        log.info("Updating product...");

        productService.updateQuantity(request);

        log.info("Product successfully updated");
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.product.register}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "changeStatusMessageFactory"
    )
    public void changeStatusAndRegister(RegistrationRequestDTO requestDTO) {

        log.info("Received answer message from admin with response {}",
                requestDTO.getStatus());

        switch (requestDTO.getStatus()) {
            case ACCEPTED -> {
                productService.changeStatusAndRegister(
                        requestDTO.getObjectToRegisterId(),
                        ProductStatus.ACTIVE);

                log.info("Product now is: {}",
                        ProductStatus.ACTIVE);
            }
            case REJECTED -> {
                productService.changeStatusAndRegister(
                        requestDTO.getObjectToRegisterId(),
                        ProductStatus.INACTIVE);

                log.info("Product now is: {}",
                        ProductStatus.INACTIVE);
            }
            default -> {
            }
        }


    }
}

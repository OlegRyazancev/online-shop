package com.ryazancev.product.kafka;

import com.ryazancev.common.dto.admin.ObjectRequest;
import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.enums.ObjectStatus;
import com.ryazancev.common.dto.product.UpdateQuantityRequest;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.processor.KafkaMessageProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMessageListeners {


    private final ProductService productService;
    private final KafkaMessageProcessor kafkaMessageProcessor;

    @KafkaListener(
            topics = "${spring.kafka.topic.product.update}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "updateQuantityMessageFactory"
    )
    public void updateQuantity(UpdateQuantityRequest request) {

        log.info("Received message to update product quantity with id: {}, " +
                        "set to: {}",
                request.getProductId(),
                request.getQuantityInStock());
        try {

            log.debug("Updating quantity...");
            productService.updateQuantity(
                    request.getProductId(),
                    request.getQuantityInStock());

            log.debug("Quantity was updated successfully");

        } catch (Exception e) {

            log.error("Failed to update quantity: {}", e.getMessage());
        }

    }

    @KafkaListener(
            topics = "${spring.kafka.topic.product.register}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "changeRegistrationStatusMessageFactory"
    )
    public void changeStatusAndRegister(RegistrationRequestDto requestDto) {

        log.info("Received message from admin to change status of " +
                        "product with id: {}, to {}",
                requestDto.getObjectToRegisterId(),
                requestDto.getStatus());

        Product product = productService.getById(
                requestDto.getObjectToRegisterId(),
                false
        );

        Long productId = product.getId();

        try {

            switch (requestDto.getStatus()) {
                case ACCEPTED -> {

                    log.debug("Changing status to ACTIVE..");
                    productService.changeStatus(
                            productId,
                            ProductStatus.ACTIVE);

                    log.debug("Registering product...");
                    productService.register(productId);

                    log.debug("Sending accepted email...");
                    kafkaMessageProcessor
                            .sendAcceptedMailToCustomerByProductId(product);

                    log.debug("Product status is now: {}",
                            ProductStatus.ACTIVE);
                }
                case REJECTED -> {

                    log.debug("Changing status to INACTIVE..");
                    productService.changeStatus(
                            productId,
                            ProductStatus.INACTIVE);

                    log.debug("Sending rejected email...");
                    kafkaMessageProcessor
                            .sendRejectedMailToCustomerByProductId(product);

                    log.debug("Product status is now: {}",
                            ProductStatus.INACTIVE);
                }
                default -> {

                    log.warn("Received unexpected request type {}" +
                                    " or status: {}",
                            requestDto.getObjectType(),
                            requestDto.getStatus());
                }
            }
        } catch (Exception e) {

            log.error("Exception occurred while registering product: {}",
                    e.getMessage());
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.product.delete}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "longValueMessageFactory"
    )

    public void deleteProductsByOrganizationId(Long organizationId) {

        log.info("Received message to delete products by organization id: {}",
                organizationId);
        try {

            List<Product> products = productService
                    .getByOrganizationId(organizationId);

            log.debug("Deleting {} products", products.size());
            products.forEach(product ->
                    productService.markProductAsDeleted(product.getId()));

            log.info("Products were deleted successfully");

        } catch (Exception e) {

            log.error("Failed to delete products: {}", e.getMessage());
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.product.change-status}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "changeObjectStatusMessageFactory"
    )
    public void changeProductStatus(ObjectRequest request) {

        log.info("Received message from admin to change status of " +
                        "product with id: {}, to {}",
                request.getObjectId(),
                request.getObjectStatus());

        try {

            ProductStatus status =
                    (request.getObjectStatus() == ObjectStatus.ACTIVATE)
                            ? ProductStatus.ACTIVE
                            : ProductStatus.FROZEN;

            log.debug("Changing status...");
            productService.changeStatus(request.getObjectId(), status);

            log.debug("Product status changed to: {}", status);

        } catch (Exception e) {

            log.error("Failed to change status: {}", e.getMessage());
        }

    }
}

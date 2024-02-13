package com.ryazancev.product.kafka;

import com.ryazancev.dto.admin.RegistrationRequestDTO;
import com.ryazancev.dto.product.UpdateQuantityRequest;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

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

        productService.updateQuantity(
                request.getProductId(),
                request.getQuantityInStock());


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
                productService.changeStatus(
                        requestDTO.getObjectToRegisterId(),
                        ProductStatus.ACTIVE);

                productService.register(
                        requestDTO.getObjectToRegisterId());

                log.info("Product now is: {}",
                        ProductStatus.ACTIVE);
            }
            case REJECTED -> {
                productService.changeStatus(
                        requestDTO.getObjectToRegisterId(),
                        ProductStatus.INACTIVE);

                log.info("Product now is: {}",
                        ProductStatus.INACTIVE);
            }
            default -> {
            }
        }
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.product.delete}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "longValueMessageFactory"
    )

    public void deleteProductsByOrganizationId(Long organizationId) {

        log.info("Received message from organization to delete " +
                        "product where organization id: {}",
                organizationId);

        List<Product> products = productService
                .getByOrganizationId(organizationId);

        products.forEach(product ->
                productService.markProductAsDeleted(product.getId()));

        log.info("Products successfully deleted");
    }

    @KafkaListener(
            topics = "${spring.kafka.topic.product.freeze}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "longValueMessageFactory"
    )
    public void freezeProduct(Long productId) {

        log.info("Received message from admin to freeze " +
                        "product with id: {}",
                productId);

        productService.changeStatus(productId, ProductStatus.FROZEN);

        log.info("Product successfully froze");
    }
}

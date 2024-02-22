package com.ryazancev.product.kafka;

import com.ryazancev.dto.admin.ObjectRequest;
import com.ryazancev.dto.admin.enums.ObjectStatus;
import com.ryazancev.dto.admin.RegistrationRequestDto;
import com.ryazancev.dto.product.UpdateQuantityRequest;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.ProductUtil;
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
    private final ProductUtil productUtil;


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
            containerFactory = "changeRegistrationStatusMessageFactory"
    )
    public void changeStatusAndRegister(RegistrationRequestDto requestDto) {

        log.info("Received answer message from admin with response {}",
                requestDto.getStatus());

        switch (requestDto.getStatus()) {
            case ACCEPTED -> {
                productService.changeStatus(
                        requestDto.getObjectToRegisterId(),
                        ProductStatus.ACTIVE);

                productService.register(
                        requestDto.getObjectToRegisterId());

                productUtil.sendAcceptedMailToCustomerByProductId(
                        requestDto.getObjectToRegisterId());

                log.info("Product now is: {}",
                        ProductStatus.ACTIVE);
            }
            case REJECTED -> {
                productService.changeStatus(
                        requestDto.getObjectToRegisterId(),
                        ProductStatus.INACTIVE);

                productUtil.sendRejectedMailToCustomerByProductId(
                        requestDto.getObjectToRegisterId());

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
            topics = "${spring.kafka.topic.product.change-status}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "changeObjectStatusMessageFactory"
    )
    public void changeProductStatus(ObjectRequest request) {

        log.info("Received message from admin to change status of " +
                        "product with id: {}, to {}",
                request.getObjectId(),
                request.getObjectStatus());

        ProductStatus status =
                (request.getObjectStatus() == ObjectStatus.ACTIVATE)
                        ? ProductStatus.ACTIVE
                        : ProductStatus.FROZEN;

        productService.changeStatus(request.getObjectId(), status);

        log.info("Product status successfully changed to: {}", status);
    }
}

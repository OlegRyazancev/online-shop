package com.ryazancev.product.util;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.OrganizationClient;
import com.ryazancev.dto.admin.ObjectType;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.mail.MailDto;
import com.ryazancev.dto.mail.MailType;
import com.ryazancev.product.kafka.ProductProducerService;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@RequiredArgsConstructor
public class ProductUtil {

    private final ProductProducerService productProducerService;
    private final ProductService productService;

    private final OrganizationClient organizationClient;
    private final CustomerClient customerClient;

    public void sendAcceptedMailToCustomerByProductId(Long productId) {
        MailDto mailDto = createMailDto(
                productId,
                MailType.OBJECT_REGISTRATION_ACCEPTED);

        productProducerService.sendMessageToMailTopic(mailDto);
    }

    public void sendRejectedMailToCustomerByProductId(Long productId) {
        MailDto mailDto = createMailDto(
                productId,
                MailType.OBJECT_REGISTRATION_REJECTED);

        productProducerService.sendMessageToMailTopic(mailDto);
    }


    private MailDto createMailDto(Long productId,
                                  MailType mailType) {

        boolean statusCheck = false;
        Product registered = productService.getById(
                productId, statusCheck);

        Long customerId = organizationClient
                .getOwnerId(registered.getOrganizationId());
        CustomerDto customerDto = customerClient.getSimpleById(customerId);

        Properties properties = new Properties();

        properties.setProperty(
                "object_name", registered.getProductName());
        properties.setProperty(
                "object_type", ObjectType.PRODUCT.name().toLowerCase());

        return MailDto.builder()
                .email(customerDto.getEmail())
                .type(mailType)
                .name(customerDto.getUsername())
                .properties(properties)
                .build();
    }
}

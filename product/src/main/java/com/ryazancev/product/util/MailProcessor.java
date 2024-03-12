package com.ryazancev.product.util;

import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.mail.MailType;
import com.ryazancev.product.kafka.ProductProducerService;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ClientsService;
import com.ryazancev.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class MailProcessor {

    private final ProductProducerService productProducerService;
    private final ProductService productService;

    private final ClientsService clientsService;

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

        Long customerId = (Long) clientsService
                .getOrganizationOwnerIdById(registered.getOrganizationId());

        CustomerDto customerDto = clientsService
                .getSimpleCustomerById(customerId)
                .safelyCast(CustomerDto.class);
        
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

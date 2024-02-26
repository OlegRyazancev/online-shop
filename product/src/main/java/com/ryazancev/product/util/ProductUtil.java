package com.ryazancev.product.util;

import com.ryazancev.dto.Element;
import com.ryazancev.dto.Fallback;
import com.ryazancev.dto.admin.enums.ObjectType;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.mail.MailDto;
import com.ryazancev.dto.mail.MailType;
import com.ryazancev.product.kafka.ProductProducerService;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ClientsService;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.exception.custom.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static com.ryazancev.product.util.exception.Message.CUSTOMER_SERVICE_UNAVAILABLE;

@Component
@RequiredArgsConstructor
public class ProductUtil {

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

        Element customerObj = clientsService
                .getSimpleCustomerById(customerId);

        if (customerObj instanceof Fallback) {

            throw new ServiceUnavailableException(
                    CUSTOMER_SERVICE_UNAVAILABLE,
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        CustomerDto customerDto = (CustomerDto) customerObj;

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

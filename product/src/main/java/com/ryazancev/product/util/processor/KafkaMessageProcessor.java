package com.ryazancev.product.util.processor;

import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.mail.MailType;
import com.ryazancev.product.kafka.ProductProducerService;
import com.ryazancev.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class KafkaMessageProcessor {

    private final ProductProducerService productProducerService;
    private final DtoProcessor dtoProcessor;

    public void sendProductIdToDeleteReviewTopic(Long id) {

        productProducerService.sendMessageToReviewTopic(id);
    }

    public void sendRegistrationRequestToAdminTopic(Long productId) {

        RegistrationRequestDto requestDto = RegistrationRequestDto.builder()
                .objectToRegisterId(productId)
                .objectType(ObjectType.PRODUCT)
                .build();

        productProducerService.sendMessageToAdminTopic(requestDto);
    }

    public void sendAcceptedMailToCustomerByProductId(Product product) {
        MailDto mailDto = dtoProcessor.createMailDto(
                product,
                MailType.OBJECT_REGISTRATION_ACCEPTED);

        productProducerService.sendMessageToMailTopic(mailDto);
    }

    public void sendRejectedMailToCustomerByProductId(Product product) {

        MailDto mailDto = dtoProcessor.createMailDto(
                product,
                MailType.OBJECT_REGISTRATION_REJECTED);

        productProducerService.sendMessageToMailTopic(mailDto);
    }
}

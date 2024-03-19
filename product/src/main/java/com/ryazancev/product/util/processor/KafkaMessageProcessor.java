package com.ryazancev.product.util.processor;

import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.mail.MailType;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.review.ReviewDto;
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
    private final NotificationProcessor notificationProcessor;

    public void sendProductIdToDeleteReviewTopic(final Long id) {

        productProducerService.sendMessageToReviewTopic(id);
    }

    public void sendRegistrationRequestToAdminTopic(final Long productId) {

        RegistrationRequestDto requestDto = RegistrationRequestDto.builder()
                .objectToRegisterId(productId)
                .objectType(ObjectType.PRODUCT)
                .build();

        productProducerService.sendMessageToAdminTopic(requestDto);
    }

    public void sendAcceptedMailToCustomerByProductId(final Product product) {

        MailDto mailDto = dtoProcessor.createMailDto(
                product,
                MailType.OBJECT_REGISTRATION_ACCEPTED);

        productProducerService.sendMessageToMailTopic(mailDto);
    }

    public void sendRejectedMailToCustomerByProductId(final Product product) {

        MailDto mailDto = dtoProcessor.createMailDto(
                product,
                MailType.OBJECT_REGISTRATION_REJECTED);

        productProducerService.sendMessageToMailTopic(mailDto);
    }

    public void sendReviewCreatedNotification(final ReviewDto reviewDto,
                                              final Long organizationId) {

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createAdminNotification(reviewDto, organizationId);

        productProducerService.sendNotification(privateNotificationRequest);
    }

    public void sendNewRegistrationRequestNotification() {

        NotificationRequest adminNotificationRequest =
                notificationProcessor.createAdminNotification();

        productProducerService.sendNotification(adminNotificationRequest);
    }
}

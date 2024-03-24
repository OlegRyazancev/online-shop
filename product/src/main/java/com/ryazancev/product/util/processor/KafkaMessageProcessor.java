package com.ryazancev.product.util.processor;

import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.mail.MailType;
import com.ryazancev.common.dto.notification.NotificationRequest;
import com.ryazancev.common.dto.review.ReviewDto;
import com.ryazancev.product.kafka.ProductProducerService;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.util.RequestHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageProcessor {

    private final ProductProducerService productProducerService;
    private final DtoProcessor dtoProcessor;
    private final NotificationProcessor notificationProcessor;

    @Async("asyncTaskExecutor")
    public void sendProductIdToDeleteReviewTopic(final Long id) {

        log.info("Method sendProductIdToDeleteReviewTopic starts work "
                + "at thread: " + Thread.currentThread().getName());

        productProducerService.sendMessageToReviewTopic(id);
    }

    @Async("asyncTaskExecutor")
    public void sendRegistrationRequestToAdminTopic(final Long productId) {

        log.info("Method sendRegistrationRequestToAdminTopic starts work "
                + "at thread: " + Thread.currentThread().getName());

        RegistrationRequestDto requestDto = RegistrationRequestDto.builder()
                .objectToRegisterId(productId)
                .objectType(ObjectType.PRODUCT)
                .build();

        productProducerService.sendMessageToAdminTopic(requestDto);
    }

    @Async("asyncTaskExecutor")
    public void sendReviewCreatedNotification(
            final ReviewDto reviewDto,
            final Long organizationId,
            final RequestHeader requestHeader) {

        log.info("Method sendReviewCreatedNotification starts work "
                + "at thread: " + Thread.currentThread().getName());

        NotificationRequest privateNotificationRequest =
                notificationProcessor
                        .createAdminNotification(
                                reviewDto,
                                organizationId,
                                requestHeader
                        );

        productProducerService.sendNotification(privateNotificationRequest);
    }

    @Async("asyncTaskExecutor")
    public void sendNewRegistrationRequestNotification(
            final RequestHeader requestHeader) {

        log.info("Method sendNewRegistrationRequestNotification starts work "
                + "at thread: " + Thread.currentThread().getName());

        NotificationRequest adminNotificationRequest =
                notificationProcessor.createAdminNotification(requestHeader);

        productProducerService.sendNotification(adminNotificationRequest);
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
}

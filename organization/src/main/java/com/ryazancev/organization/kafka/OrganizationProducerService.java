package com.ryazancev.organization.kafka;

import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.notification.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Oleg Ryazancev
 */

@Service
@Slf4j
public class OrganizationProducerService {

    private final KafkaTemplate<String, RegistrationRequestDto>
            adminKafkaTemplate;

    private final KafkaTemplate<String, Long>
            productKafkaTemplate;

    private final KafkaTemplate<String, MailDto>
            mailKafkaTemplate;

    private final KafkaTemplate<String, NotificationRequest>
            notificationKafkaTemplate;

    @Value("${spring.kafka.topic.admin}")
    private String adminTopic;

    @Value("${spring.kafka.topic.product}")
    private String productTopic;

    @Value("${spring.kafka.topic.mail}")
    private String mailTopic;

    @Value("${spring.kafka.topic.notification}")
    private String notificationTopic;

    public OrganizationProducerService(
            @Qualifier("adminKafkaTemplate") final KafkaTemplate<
                    String, RegistrationRequestDto> adminKafkaTemplate,
            @Qualifier("productKafkaTemplate") final KafkaTemplate<
                    String, Long> productKafkaTemplate,
            @Qualifier("mailKafkaTemplate") final KafkaTemplate<
                    String, MailDto> mailKafkaTemplate,
            @Qualifier("notificationKafkaTemplate") final KafkaTemplate<
                    String, NotificationRequest> notificationKafkaTemplate) {

        this.adminKafkaTemplate = adminKafkaTemplate;
        this.productKafkaTemplate = productKafkaTemplate;
        this.mailKafkaTemplate = mailKafkaTemplate;
        this.notificationKafkaTemplate = notificationKafkaTemplate;
    }

    public void sendMessageToAdminTopic(
            final RegistrationRequestDto requestDto) {

        log.info("Received request to make registration request "
                + "of organization on topic: {}", adminTopic);

        try {

            log.debug("Sending request...");
            adminKafkaTemplate.send(adminTopic, requestDto);

            log.debug("Request to {} was sent", adminTopic);

        } catch (Exception e) {

            log.error("Failed to send request to {}: {}",
                    adminTopic,
                    e.getMessage());
        }
    }

    public void sendMessageToProductTopic(
            final Long organizationId) {

        log.info("Received request to send productId {} to topic: {}",
                organizationId, productTopic);

        try {

            log.debug("Sending request...");
            productKafkaTemplate.send(productTopic, organizationId);

            log.debug("Request to {} was sent", productTopic);

        } catch (Exception e) {

            log.error("Failed to send message to {}: {}",
                    productTopic,
                    e.getMessage());
        }
    }

    public void sendMessageToMailTopic(
            final MailDto mailDto) {

        log.info("Received request to send {} email to {} on topic: {}",
                mailDto.getType(),
                mailDto.getEmail(),
                mailTopic);

        try {

            log.debug("Sending request...");
            mailKafkaTemplate.send(mailTopic, mailDto);

            log.debug("Request to {} was sent", mailTopic);

        } catch (Exception e) {

            log.error("Failed to send request to {}: {}",
                    mailTopic,
                    e.getMessage());
        }
    }

    public void sendNotification(
            final NotificationRequest request) {

        log.info("Received request to send {} notification {} to user "
                        + "with id: {} from recipient with id: {}",
                request.getScope(),
                request.getType(),
                request.getRecipientId(),
                request.getSenderId());

        try {

            log.debug("Sending notification request...");
            notificationKafkaTemplate.send(notificationTopic, request);

            log.debug("Notification request sent to topic: {}",
                    notificationTopic);

        } catch (Exception e) {

            log.error("Failed to send notification request: {}",
                    e.getMessage());
        }
    }
}

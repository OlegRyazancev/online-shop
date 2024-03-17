package com.ryazancev.product.kafka;

import com.ryazancev.common.dto.admin.RegistrationRequestDto;
import com.ryazancev.common.dto.mail.MailDto;
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
public class ProductProducerService {

    private final KafkaTemplate<String, RegistrationRequestDto>
            adminKafkaTemplate;
    private final KafkaTemplate<String, Long>
            reviewKafkaTemplate;

    private final KafkaTemplate<String, MailDto>
            mailKafkaTemplate;

    @Value("${spring.kafka.topic.admin}")
    private String adminTopic;

    @Value("${spring.kafka.topic.review}")
    private String reviewTopic;

    @Value("${spring.kafka.topic.mail}")
    private String mailTopic;


    public ProductProducerService(
            @Qualifier("adminKafkaTemplate")
            KafkaTemplate<String, RegistrationRequestDto> adminKafkaTemplate,
            @Qualifier("reviewKafkaTemplate")
            KafkaTemplate<String, Long> reviewKafkaTemplate,
            @Qualifier("mailKafkaTemplate")
            KafkaTemplate<String, MailDto> mailKafkaTemplate) {

        this.adminKafkaTemplate = adminKafkaTemplate;
        this.reviewKafkaTemplate = reviewKafkaTemplate;
        this.mailKafkaTemplate = mailKafkaTemplate;
    }

    public void sendMessageToAdminTopic(RegistrationRequestDto requestDto) {

        log.info("Received request to make registration request " +
                "of product on topic: {}", adminTopic);

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

    public void sendMessageToReviewTopic(Long productId) {

        log.info("Received request to send productId {} to topic: {}",
                productId, reviewTopic);

        try {

            log.debug("Sending request...");
            reviewKafkaTemplate.send(reviewTopic, productId);

            log.debug("Request to {} was sent", reviewTopic);

        } catch (Exception e) {

            log.error("Failed to send message to {}: {}",
                    reviewTopic,
                    e.getMessage());
        }
    }

    public void sendMessageToMailTopic(MailDto mailDto) {

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
}

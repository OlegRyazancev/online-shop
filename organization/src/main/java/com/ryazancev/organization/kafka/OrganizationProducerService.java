package com.ryazancev.organization.kafka;

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
public class OrganizationProducerService {

    private final KafkaTemplate<String, RegistrationRequestDto>
            adminKafkaTemplate;

    private final KafkaTemplate<String, Long>
            productKafkaTemplate;

    private final KafkaTemplate<String, MailDto>
            mailKafkaTemplate;

    @Value("${spring.kafka.topic.admin}")
    private String adminTopic;

    @Value("${spring.kafka.topic.product}")
    private String productTopic;

    @Value("${spring.kafka.topic.mail}")
    private String mailTopic;

    public OrganizationProducerService(
            @Qualifier("adminKafkaTemplate")
            KafkaTemplate<String, RegistrationRequestDto> adminKafkaTemplate,
            @Qualifier("productKafkaTemplate")
            KafkaTemplate<String, Long> productKafkaTemplate,
            @Qualifier("mailKafkaTemplate")
            KafkaTemplate<String, MailDto> mailKafkaTemplate) {

        this.adminKafkaTemplate = adminKafkaTemplate;
        this.productKafkaTemplate = productKafkaTemplate;
        this.mailKafkaTemplate = mailKafkaTemplate;
    }

    public void sendMessageToAdminTopic(RegistrationRequestDto requestDto) {

        log.info("Request to {} to make registration request " +
                "of organization was received", adminTopic);

        try {

            log.trace("sending request...");
            adminKafkaTemplate.send(adminTopic, requestDto);

            log.debug("Request to {} was sent", adminTopic);

        } catch (Exception e) {

            log.error("Request to {} was not sent: {}",
                    adminTopic,
                    e.getMessage());
        }
    }

    public void sendMessageToProductTopic(Long organizationId) {

        log.info("Request to send organizationId: {} to {} was received",
                organizationId, productTopic);

        try {

            log.trace("sending request...");
            productKafkaTemplate.send(productTopic, organizationId);

            log.debug("Request to {} was sent", productTopic);

        } catch (Exception e) {

            log.error("Message to {} was not sent: {}",
                    productTopic,
                    e.getMessage());
        }
    }

    public void sendMessageToMailTopic(MailDto mailDto) {

        log.info("Request to send {} email to {} was received",
                mailDto.getType(),
                mailDto.getEmail());

        try {

            log.trace("sending request...");
            mailKafkaTemplate.send(mailTopic, mailDto);

            log.debug("Request to {} was sent", mailTopic);

        } catch (Exception e) {

            log.error("Request to {} was not sent:{}",
                    mailTopic,
                    e.getMessage());

        }
    }
}

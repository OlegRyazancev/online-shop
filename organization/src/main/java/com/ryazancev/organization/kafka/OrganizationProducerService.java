package com.ryazancev.organization.kafka;

import com.ryazancev.dto.admin.RegistrationRequestDto;
import com.ryazancev.dto.mail.MailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
        try {
            adminKafkaTemplate.send(adminTopic, requestDto);
            log.info("Message to {} was successfully sent", adminTopic);
        } catch (Exception e) {
            log.info("Message to {} was not sent", adminTopic);
            e.printStackTrace();
        }
    }

    public void sendMessageToProductTopic(Long organizationId) {
        try {
            productKafkaTemplate.send(productTopic, organizationId);
            log.info("Message to {} was successfully sent", productTopic);
        } catch (Exception e) {
            log.info("Message to {} was not sent", productTopic);
            e.printStackTrace();

        }
    }

    public void sendMessageToMailTopic(MailDto mailDto) {
        try {
            mailKafkaTemplate.send(mailTopic, mailDto);
            log.info("Message to {} was successfully sent", mailTopic);
        } catch (Exception e) {
            log.info("Message to {} was not sent", mailTopic);
            e.printStackTrace();

        }
    }
}

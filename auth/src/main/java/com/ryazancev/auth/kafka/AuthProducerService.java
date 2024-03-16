package com.ryazancev.auth.kafka;

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
public class AuthProducerService {

    @Value("${spring.kafka.topic.mail}")
    private String mailTopicName;

    private final KafkaTemplate<String, MailDto> mailKafkaTemplate;

    public AuthProducerService(
            @Qualifier("mailKafkaTemplate")
            KafkaTemplate<String, MailDto> mailKafkaTemplate) {
        this.mailKafkaTemplate = mailKafkaTemplate;
    }

    public void sendMessageToMailTopic(MailDto mailDto) {

        log.info("Received request to send email to: {}",
                mailDto.getEmail());

        try {

            log.trace("Sending request...");
            mailKafkaTemplate.send(mailTopicName, mailDto);

            log.debug("Request to send email to {} was sent successfully",
                    mailDto.getEmail());

        } catch (Exception e) {

            log.error("Failed to send email to {}: {}",
                    mailDto.getEmail(),
                    e.getMessage());
        }
    }
}

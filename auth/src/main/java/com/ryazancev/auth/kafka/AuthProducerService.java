package com.ryazancev.auth.kafka;

import com.ryazancev.dto.mail.MailDto;
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
        try {
            mailKafkaTemplate.send(mailTopicName, mailDto);
            log.info("Message to {} was successfully sent", mailTopicName);
        } catch (Exception e) {
            log.info("Message to {} was not sent", mailTopicName);
            e.printStackTrace();
        }
    }
}

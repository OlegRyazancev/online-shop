package com.ryazancev.mail.kafka;

import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class MailMessageListener {


    private final MailService mailService;

    @KafkaListener(
            topics = "${spring.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "messageFactory"
    )
    void consumeMail(MailDto mailDto) {

        log.info("Received message to send email to: {}, email type: {}",
                mailDto.getEmail(), mailDto.getType().name());

        try {

            log.trace("Sending message...");
            mailService.sendEmail(mailDto);

            log.debug("Mail was sent to: {}", mailDto.getEmail());

        } catch (Exception e) {

            log.error("Mail was not sent to {}: {}",
                    mailDto.getEmail(),
                    e.getMessage());
        }

    }
}

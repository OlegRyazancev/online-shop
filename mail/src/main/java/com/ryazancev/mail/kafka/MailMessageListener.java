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

        log.info("Received message to send {} email to: {}",
                mailDto.getType(), mailDto.getEmail());

        try {

            log.trace("Sending message...");
            mailService.sendEmail(mailDto);

            log.debug("Email sent successfully to: {}", mailDto.getEmail());

        } catch (Exception e) {

            log.error("Failed to send email to {}: {}",
                    mailDto.getEmail(),
                    e.getMessage());
        }

    }
}

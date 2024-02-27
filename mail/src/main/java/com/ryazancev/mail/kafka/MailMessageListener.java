package com.ryazancev.mail.kafka;

import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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
        log.info("Sending message...");

        mailService.sendEmail(mailDto);

        log.info("Mail successfully send to: {}", mailDto.getEmail());
    }
}

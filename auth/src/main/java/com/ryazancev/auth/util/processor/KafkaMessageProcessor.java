package com.ryazancev.auth.util.processor;

import com.ryazancev.auth.kafka.AuthProducerService;
import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.model.User;
import com.ryazancev.common.dto.mail.MailDto;
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

    private final DtoProcessor dtoProcessor;
    private final AuthProducerService authProducerService;

    @Async("asyncTaskExecutor")
    public void sendRegistrationMailToCustomer(final ConfirmationToken token) {

        log.info("Method sendRegistrationMailToCustomer starts work "
                + "at thread: " + Thread.currentThread().getName());

        MailDto mailDto = dtoProcessor.createRegistrationMailDto(token);

        authProducerService.sendMessageToMailTopic(mailDto);
    }

    @Async("asyncTaskExecutor")
    public void sendConfirmationMailToCustomer(final User user,
                                               final ConfirmationToken token) {

        log.info("Method sendConfirmationMailToCustomer starts work "
                + "at thread: " + Thread.currentThread().getName());

        MailDto mailDto = dtoProcessor.createConfirmationMailDto(
                user.getEmail(),
                user.getName(),
                token.getToken());

        authProducerService.sendMessageToMailTopic(mailDto);
    }
}

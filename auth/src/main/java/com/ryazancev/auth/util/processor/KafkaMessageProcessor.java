package com.ryazancev.auth.util.processor;

import com.ryazancev.auth.kafka.AuthProducerService;
import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.model.User;
import com.ryazancev.common.dto.mail.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class KafkaMessageProcessor {

    private final DtoProcessor dtoProcessor;
    private final AuthProducerService authProducerService;

    public void sendRegistrationMailToCustomer(ConfirmationToken token) {

        MailDto mailDto = dtoProcessor.createRegistrationMailDto(token);
        authProducerService.sendMessageToMailTopic(mailDto);
    }

    public void sendConfirmationMailToCustomer(User user,
                                               ConfirmationToken token) {

        MailDto mailDto = dtoProcessor.createConfirmationMailDto(
                user.getEmail(),
                user.getName(),
                token.getToken());

        authProducerService.sendMessageToMailTopic(mailDto);

    }
}

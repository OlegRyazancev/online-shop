package com.ryazancev.auth.util.processor;

import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.common.dto.mail.MailDto;
import com.ryazancev.common.dto.mail.MailType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Oleg Ryazancev
 */

@Component
public class DtoProcessor {

    @Value("${security.jwt.confirmation_link_prefix}")
    private String confirmationLinkPrefix;

    public MailDto createConfirmationMailDto(final String email,
                                             final String name,
                                             final String token) {

        Properties properties = new Properties();
        properties.setProperty("link", confirmationLinkPrefix + token);

        return MailDto.builder()
                .email(email)
                .name(name)
                .type(MailType.MAIL_CONFIRMATION)
                .properties(properties)
                .build();
    }

    public MailDto createRegistrationMailDto(final ConfirmationToken token) {

        return MailDto.builder()
                .email(token.getUser().getEmail())
                .name(token.getUser().getName())
                .type(MailType.USER_REGISTRATION)
                .properties(null)
                .build();
    }
}

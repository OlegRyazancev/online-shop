package com.ryazancev.auth.util;

import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.model.User;
import com.ryazancev.dto.mail.MailDto;
import com.ryazancev.dto.mail.MailType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

@Component
public class AuthUtil {

    @Value("${security.jwt.confirmation_link_prefix}")
    private String CONFIRMATION_LINK_PREFIX;

    public  MailDto createConfirmationMailDto(
            String email, String name, String token) {

        Properties properties = new Properties();
        properties.setProperty("link", CONFIRMATION_LINK_PREFIX + token);

        return MailDto.builder()
                .email(email)
                .name(name)
                .type(MailType.MAIL_CONFIRMATION)
                .properties(properties)
                .build();
    }

    public MailDto createRegistrationMailDto(String email, String name) {

        return MailDto.builder()
                .email(email)
                .name(name)
                .type(MailType.USER_REGISTRATION)
                .properties(null)
                .build();
    }

    public ConfirmationToken getConfirmationToken(User user) {

        return ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
    }

}

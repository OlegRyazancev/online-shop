package com.ryazancev.auth.util;

import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.model.User;
import com.ryazancev.clients.mail.dto.MailDTO;
import com.ryazancev.clients.mail.dto.MailType;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

public class AuthUtil {

    private static final String CONFIRMATION_LINK_PREFIX =
            "http://localhost:8080/api/v1/auth/confirm?token=";

    public static MailDTO createConfirmationMailDTO(
            String email, String name, String token) {

        Properties properties = new Properties();
        properties.setProperty("link", CONFIRMATION_LINK_PREFIX + token);

        return MailDTO.builder()
                .email(email)
                .name(name)
                .type(MailType.CONFIRMATION)
                .properties(properties)
                .build();
    }

    public static MailDTO createRegistrationMailDTO(String email, String name) {

        return MailDTO.builder()
                .email(email)
                .name(name)
                .type(MailType.REGISTRATION)
                .properties(null)
                .build();
    }

    public static ConfirmationToken getConfirmationToken(User user) {

        return ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
    }

}

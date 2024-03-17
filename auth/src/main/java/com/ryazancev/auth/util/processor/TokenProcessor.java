package com.ryazancev.auth.util.processor;

import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Oleg Ryazancev
 */

@Component
public class TokenProcessor {

    public ConfirmationToken createConfirmationToken(User user) {

        return ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
    }
}

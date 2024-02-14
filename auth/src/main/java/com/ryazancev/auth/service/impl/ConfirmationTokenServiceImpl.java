package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.repository.ConfirmationTokenRepository;
import com.ryazancev.auth.repository.UserRepository;
import com.ryazancev.auth.service.ConfirmationTokenService;
import com.ryazancev.auth.util.AuthUtil;
import com.ryazancev.auth.util.exception.custom.ConfirmationTokenException;
import com.ryazancev.dto.mail.MailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    @Value("${spring.kafka.topic.mail}")
    private String mailTopicName;

    private final KafkaTemplate<String, MailDTO> kafkaTemplate;

    @Transactional
    @Override
    public String confirm(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new ConfirmationTokenException(
                                "Token not found",
                                HttpStatus.NOT_FOUND));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new ConfirmationTokenException(
                    "Email already confirmed",
                    HttpStatus.BAD_REQUEST);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ConfirmationTokenException(
                    "Token expired",
                    HttpStatus.BAD_REQUEST);
        }
        String email = confirmationToken.getUser().getEmail();
        String name = confirmationToken.getUser().getName();

        confirmationTokenRepository.updateConfirmedAt(token);

        userRepository.enableUser(email);

        MailDTO mailDTO = AuthUtil.createRegistrationMailDTO(email, name);
        kafkaTemplate.send(mailTopicName, mailDTO);

        return "Email confirmed successfully!";
    }


    @Override
    public void save(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }
}

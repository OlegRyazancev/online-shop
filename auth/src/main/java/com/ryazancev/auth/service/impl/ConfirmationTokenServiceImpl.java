package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.repository.ConfirmationTokenRepository;
import com.ryazancev.auth.repository.UserRepository;
import com.ryazancev.auth.service.ConfirmationTokenService;
import com.ryazancev.auth.util.AuthUtil;
import com.ryazancev.auth.util.exception.custom.ConfirmationTokenException;
import com.ryazancev.clients.mail.MailClient;
import com.ryazancev.clients.mail.dto.MailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    private final MailClient mailClient;

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
        mailClient.sendEmail(mailDTO);

        return "Email confirmed successfully!";
    }


    @Override
    public void save(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }
}

package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.kafka.AuthProducerService;
import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.repository.ConfirmationTokenRepository;
import com.ryazancev.auth.repository.UserRepository;
import com.ryazancev.auth.service.ConfirmationTokenService;
import com.ryazancev.auth.util.AuthUtil;
import com.ryazancev.auth.util.exception.custom.ConfirmationTokenException;
import com.ryazancev.auth.util.validator.AuthValidator;
import com.ryazancev.common.dto.mail.MailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final AuthUtil authUtil;
    private final AuthValidator authValidator;
    private final AuthProducerService authProducerService;

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    private final MessageSource messageSource;

    @Transactional
    @Override
    public String confirm(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new ConfirmationTokenException(
                                messageSource.getMessage(
                                        "token_not_found",
                                        null,
                                        Locale.getDefault()
                                ),
                                HttpStatus.NOT_FOUND));

        authValidator.validateConfirmationStatus(confirmationToken);
        authValidator.validateExpiration(confirmationToken);

        String email = confirmationToken.getUser().getEmail();
        String name = confirmationToken.getUser().getName();

        confirmationTokenRepository.updateConfirmedAt(token);
        userRepository.enableUser(email);

        MailDto mailDto = authUtil.createRegistrationMailDto(email, name);
        authProducerService.sendMessageToMailTopic(mailDto);

        return messageSource.getMessage(
                "email_confirmed_successfully",
                null,
                Locale.getDefault()
        );
    }


    @Override
    public void save(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }
}

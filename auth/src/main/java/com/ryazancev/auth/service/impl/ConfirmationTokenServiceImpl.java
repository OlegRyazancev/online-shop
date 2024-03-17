package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.repository.ConfirmationTokenRepository;
import com.ryazancev.auth.repository.UserRepository;
import com.ryazancev.auth.service.ConfirmationTokenService;
import com.ryazancev.auth.util.exception.custom.ConfirmationTokenException;
import com.ryazancev.auth.util.processor.KafkaMessageProcessor;
import com.ryazancev.auth.util.validator.AuthValidator;
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

    private final AuthValidator authValidator;

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    private final MessageSource messageSource;
    private final KafkaMessageProcessor kafkaMessageProcessor;

    @Transactional
    @Override
    public String confirm(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new ConfirmationTokenException(
                                messageSource.getMessage(
                                        "exception.auth.token_not_found",
                                        null,
                                        Locale.getDefault()
                                ),
                                HttpStatus.NOT_FOUND));

        authValidator.validateConfirmationStatus(confirmationToken);
        authValidator.validateExpiration(confirmationToken);



        confirmationTokenRepository.updateConfirmedAt(token);
        userRepository.enableUser(confirmationToken.getUser().getEmail());

        kafkaMessageProcessor.sendRegistrationMailToCustomer(confirmationToken);

        return messageSource.getMessage(
                "service.auth.email_confirmed_successfully",
                null,
                Locale.getDefault()
        );
    }


    @Override
    public void save(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }
}

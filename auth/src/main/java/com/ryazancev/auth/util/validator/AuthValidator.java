package com.ryazancev.auth.util.validator;

import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.model.User;
import com.ryazancev.auth.repository.UserRepository;
import com.ryazancev.auth.util.exception.CustomExceptionFactory;
import com.ryazancev.common.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final UserRepository userRepository;

    private final MessageSource messageSource;

    public void validateUserAccess(
            final User user) {

        if (user.getDeletedAt() != null) {

            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd.MM.yyyy HH:mm");
            String formattedDate = user.getDeletedAt().format(formatter);

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .deletedAccount(
                            messageSource,
                            user.getEmail(),
                            formattedDate
                    );
        }
    }

    public void validateConfirmationStatus(
            final ConfirmationToken confirmationToken) {

        if (confirmationToken.getConfirmedAt() != null) {

            throw CustomExceptionFactory
                    .getConfirmationToken()
                    .emailConfirmed(messageSource);
        }
    }

    public void validateExpiration(
            final ConfirmationToken confirmationToken) {

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {

            throw CustomExceptionFactory
                    .getConfirmationToken()
                    .expired(
                            messageSource,
                            String.valueOf(expiredAt)
                    );
        }
    }

    public void validateEmailUniqueness(
            final UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {

            throw CustomExceptionFactory
                    .getUserCreation()
                    .emailExists(messageSource);
        }
    }

    public void validatePasswordConfirmation(
            final UserDto userDto) {

        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {

            throw CustomExceptionFactory
                    .getUserCreation()
                    .passwordMismatch(messageSource);
        }
    }

}

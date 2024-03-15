package com.ryazancev.auth.util.validator;

import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.model.User;
import com.ryazancev.auth.repository.UserRepository;
import com.ryazancev.auth.util.exception.custom.AccessDeniedException;
import com.ryazancev.auth.util.exception.custom.ConfirmationTokenException;
import com.ryazancev.auth.util.exception.custom.UserCreationException;
import com.ryazancev.common.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final UserRepository userRepository;

    private final MessageSource messageSource;

    public void validateUserAccess(User user) {

        if (user.getDeletedAt() != null) {

            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd.MM.yyyy HH:mm");
            String formattedDate = user.getDeletedAt().format(formatter);

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "deleted_account_format",
                            new Object[]{user.getEmail(), formattedDate},
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    public void validateConfirmationStatus(ConfirmationToken confirmationToken) {

        if (confirmationToken.getConfirmedAt() != null) {
            throw new ConfirmationTokenException(
                    messageSource.getMessage(
                            "email_confirmed",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public void validateExpiration(ConfirmationToken confirmationToken) {

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ConfirmationTokenException(
                    messageSource.getMessage(
                            "token_expired",
                            new Object[]{expiredAt},
                            Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public void validateEmailUniqueness(UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserCreationException(
                    messageSource.getMessage(
                            "email_exists",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public void validatePasswordConfirmation(UserDto userDto) {

        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            throw new UserCreationException(
                    messageSource.getMessage(
                            "password_mismatch",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST);
        }
    }

}

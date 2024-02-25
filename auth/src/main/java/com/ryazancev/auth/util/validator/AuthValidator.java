package com.ryazancev.auth.util.validator;

import com.ryazancev.auth.model.ConfirmationToken;
import com.ryazancev.auth.model.User;
import com.ryazancev.auth.repository.UserRepository;
import com.ryazancev.auth.util.exception.custom.AccessDeniedException;
import com.ryazancev.auth.util.exception.custom.ConfirmationTokenException;
import com.ryazancev.auth.util.exception.custom.UserCreationException;
import com.ryazancev.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.ryazancev.auth.util.exception.Message.*;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final UserRepository userRepository;

    public void validateUserAccess(User user) {

        if (user.getDeletedAt() != null) {

            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd.MM.yyyy HH:mm");
            String formattedDate = user.getDeletedAt().format(formatter);

            throw new AccessDeniedException(
                    String.format(
                            DELETED_ACCOUNT_FORMAT,
                            user.getEmail(),
                            formattedDate), HttpStatus.FORBIDDEN);
        }
    }

    public void validateConfirmationStatus(ConfirmationToken confirmationToken) {

        if (confirmationToken.getConfirmedAt() != null) {
            throw new ConfirmationTokenException(
                    EMAIL_CONFIRMED,
                    HttpStatus.BAD_REQUEST);
        }
    }

    public void validateExpiration(ConfirmationToken confirmationToken) {

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ConfirmationTokenException(
                    TOKEN_EXPIRED,
                    HttpStatus.BAD_REQUEST);
        }
    }

    public void validateEmailUniqueness(UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserCreationException(
                    EMAIL_EXISTS,
                    HttpStatus.BAD_REQUEST);
        }
    }

    public void validatePasswordConfirmation(UserDto userDto) {

        if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
            throw new UserCreationException(
                    PASSWORD_MISMATCH,
                    HttpStatus.BAD_REQUEST);
        }
    }

}

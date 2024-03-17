package com.ryazancev.auth.util.exception;

import com.ryazancev.auth.util.exception.custom.AccessDeniedException;
import com.ryazancev.auth.util.exception.custom.ConfirmationTokenException;
import com.ryazancev.auth.util.exception.custom.UserCreationException;
import com.ryazancev.auth.util.exception.custom.UserNotFoundException;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.exception.OnlineShopException;
import com.ryazancev.common.exception.ServiceUnavailableException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ExceptionBody> handleServiceUnavailable(
            ServiceUnavailableException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.AUTH,
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleUserNotFound(
            UserNotFoundException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.AUTH,
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<ExceptionBody> handleUserCreation(
            UserCreationException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.AUTH,
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(ConfirmationTokenException.class)
    public ResponseEntity<ExceptionBody> handleConfirmationToken(
            ConfirmationTokenException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.AUTH,
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionBody> handleConfirmationToken(
            AccessDeniedException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.AUTH,
                        e.getHttpStatus()
                ));
    }


    @ExceptionHandler(
            org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ExceptionBody> handleAccessDeniedSecurity(
            AccessDeniedException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ExceptionBody(
                        "Access Denied",
                        ServiceStage.AUTH,
                        HttpStatus.FORBIDDEN
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionBody> handleConstraintViolation(
            ConstraintViolationException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        ExceptionBody exceptionBody = new ExceptionBody("Validation failed");
        exceptionBody.setErrors(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                )));
        exceptionBody.setHttpStatus(HttpStatus.BAD_REQUEST);
        exceptionBody.setServiceStage(ServiceStage.AUTH);

        return ResponseEntity
                .status(exceptionBody.getHttpStatus())
                .body(exceptionBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        ExceptionBody exceptionBody = new ExceptionBody("Validation failed");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        exceptionBody.setErrors(errors.stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage)));

        exceptionBody.setHttpStatus(HttpStatus.BAD_REQUEST);
        exceptionBody.setServiceStage(ServiceStage.AUTH);

        return ResponseEntity
                .status(exceptionBody.getHttpStatus())
                .body(exceptionBody);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionBody> handleAuthentication(
            AuthenticationException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionBody(
                        "Authentication failed",
                        null,
                        ServiceStage.AUTH,
                        HttpStatus.UNAUTHORIZED
                ));
    }

    @ExceptionHandler(OnlineShopException.class)
    public ResponseEntity<ExceptionBody> handleOnlineShop(
            OnlineShopException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        e.getErrors(),
                        e.getServiceStage(),
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBody> handleAny(Exception e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionBody(
                        "Internal error: " + e.getMessage(),
                        ServiceStage.AUTH,
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));
    }
}

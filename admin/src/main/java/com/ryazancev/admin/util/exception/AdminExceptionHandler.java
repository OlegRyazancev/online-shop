package com.ryazancev.admin.util.exception;

import com.ryazancev.admin.util.exception.custom.InvalidRequestStatusException;
import com.ryazancev.admin.util.exception.custom.RequestNotFoundException;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.exception.OnlineShopException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@RestControllerAdvice
public class AdminExceptionHandler {

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleRequestNotFound(
            final RequestNotFoundException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.ADMIN,
                        e.getHttpStatus(),
                        e.getCode().name(),
                        e.getTimestamp()
                ));
    }

    @ExceptionHandler(InvalidRequestStatusException.class)
    public ResponseEntity<ExceptionBody> handleInvalidRequestStatus(
            final InvalidRequestStatusException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.ADMIN,
                        e.getHttpStatus(),
                        e.getCode().name(),
                        e.getTimestamp()
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionBody> handleConstraintViolation(
            final ConstraintViolationException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        ExceptionBody exceptionBody = new ExceptionBody(
                CustomErrorCode.OS_ADMIN_SECURITY
                        .getMessage("Validation failed"));

        exceptionBody.setErrors(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                )));
        exceptionBody.setHttpStatus(HttpStatus.BAD_REQUEST);
        exceptionBody.setServiceStage(ServiceStage.ADMIN);
        exceptionBody.setCode(CustomErrorCode.OS_ADMIN_SECURITY.name());
        exceptionBody.setTimestamp(LocalDateTime.now());

        return ResponseEntity
                .status(exceptionBody.getHttpStatus())
                .body(exceptionBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        ExceptionBody exceptionBody = new ExceptionBody(
                CustomErrorCode.OS_ADMIN_SECURITY
                        .getMessage("Validation failed"));

        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        exceptionBody.setErrors(errors.stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage)));

        exceptionBody.setHttpStatus(HttpStatus.BAD_REQUEST);
        exceptionBody.setServiceStage(ServiceStage.ADMIN);
        exceptionBody.setCode(CustomErrorCode.OS_ADMIN_SECURITY.name());
        exceptionBody.setTimestamp(LocalDateTime.now());

        return ResponseEntity
                .status(exceptionBody.getHttpStatus())
                .body(exceptionBody);
    }

    @ExceptionHandler(OnlineShopException.class)
    public ResponseEntity<ExceptionBody> handleOnlineShop(
            final OnlineShopException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        e.getErrors(),
                        e.getServiceStage(),
                        e.getHttpStatus(),
                        e.getCode(),
                        e.getTimestamp()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBody> handleAny(
            final Exception e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionBody(
                        CustomErrorCode.OS_ADMIN_INTERNAL_500
                                .getMessage(e.getMessage()),
                        ServiceStage.ADMIN,
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));
    }
}

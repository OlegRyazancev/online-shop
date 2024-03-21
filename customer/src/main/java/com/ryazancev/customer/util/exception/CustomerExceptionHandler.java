package com.ryazancev.customer.util.exception;

import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.exception.OnlineShopException;
import com.ryazancev.common.exception.ServiceUnavailableException;
import com.ryazancev.customer.util.exception.custom.AccessDeniedException;
import com.ryazancev.customer.util.exception.custom.CustomerCreationException;
import com.ryazancev.customer.util.exception.custom.CustomerNotFoundException;
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
public class CustomerExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ExceptionBody> handleServiceUnavailable(
            final ServiceUnavailableException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.CUSTOMER,
                        e.getHttpStatus(),
                        e.getCode(),
                        e.getTimestamp()
                ));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleCustomerNotFound(
            final CustomerNotFoundException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.CUSTOMER,
                        e.getHttpStatus(),
                        e.getCode().name(),
                        e.getTimestamp()
                ));
    }

    @ExceptionHandler(CustomerCreationException.class)
    public ResponseEntity<ExceptionBody> handleCustomerCreation(
            final CustomerCreationException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.CUSTOMER,
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

        ExceptionBody exceptionBody = new ExceptionBody("Validation failed");
        exceptionBody.setErrors(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                )));
        exceptionBody.setHttpStatus(HttpStatus.BAD_REQUEST);
        exceptionBody.setServiceStage(ServiceStage.CUSTOMER);
        exceptionBody.setCode(ErrorCode.CONSTRAINT_VIOLATION.name());
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

        ExceptionBody exceptionBody = new ExceptionBody("Validation failed");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        exceptionBody.setErrors(errors.stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage)));

        exceptionBody.setHttpStatus(HttpStatus.BAD_REQUEST);
        exceptionBody.setServiceStage(ServiceStage.CUSTOMER);
        exceptionBody.setCode(ErrorCode.METHOD_ARGUMENT_NOT_VALID.name());
        exceptionBody.setTimestamp(LocalDateTime.now());

        return ResponseEntity
                .status(exceptionBody.getHttpStatus())
                .body(exceptionBody);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionBody> handleAccessDenied(
            final AccessDeniedException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.CUSTOMER,
                        e.getHttpStatus(),
                        e.getCode().name(),
                        e.getTimestamp()
                ));
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
                        "Internal error: " + e.getMessage(),
                        ServiceStage.CUSTOMER,
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));
    }
}

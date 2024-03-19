package com.ryazancev.product.util.exception;

import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.exception.OnlineShopException;
import com.ryazancev.common.exception.ServiceUnavailableException;
import com.ryazancev.product.util.exception.custom.AccessDeniedException;
import com.ryazancev.product.util.exception.custom.ProductCreationException;
import com.ryazancev.product.util.exception.custom.ProductNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class ProductExceptionHandler {

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
                        ServiceStage.PRODUCT,
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(ProductCreationException.class)
    public ResponseEntity<ExceptionBody> handleProductCreation(
            final ProductCreationException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.PRODUCT,
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleProductNotFound(
            final ProductNotFoundException e) {

        log.error(e.getClass().getSimpleName());
        log.debug(e.getMessage());
        log.debug("Exception stack trace:", e);

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.PRODUCT,
                        e.getHttpStatus()
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
        exceptionBody.setServiceStage(ServiceStage.PRODUCT);

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
        exceptionBody.setServiceStage(ServiceStage.PRODUCT);

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
                        ServiceStage.PRODUCT,
                        e.getHttpStatus()
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
                        e.getHttpStatus()
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
                        ServiceStage.PRODUCT,
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));
    }
}

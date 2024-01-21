package com.ryazancev.customer.util.exception;

import com.ryazancev.config.OnlineShopException;
import com.ryazancev.config.ServiceStage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomerGlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleCustomerNotFound(CustomerNotFoundException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.CUSTOMER,
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(IncorrectBalanceException.class)
    public ResponseEntity<ExceptionBody> handleBalance(IncorrectBalanceException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.CUSTOMER,
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionBody> handleConstraintViolation(
            final ConstraintViolationException e) {
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed");
        exceptionBody.setErrors(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                )));
        exceptionBody.setHttpStatus(HttpStatus.BAD_REQUEST);
        exceptionBody.setServiceStage(ServiceStage.CUSTOMER);
        return ResponseEntity
                .status(exceptionBody.getHttpStatus())
                .body(exceptionBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBody> handleAny(final Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionBody(
                        "Internal error: " + e.getMessage(),
                        ServiceStage.CUSTOMER,
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));
    }


    ///GLOBAL EXCEPTION

    @ExceptionHandler(OnlineShopException.class)
    public ResponseEntity<ExceptionBody> handleOnlineShop(OnlineShopException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        e.getErrors(),
                        e.getServiceStage(),
                        e.getHttpStatus()
                ));
    }
}

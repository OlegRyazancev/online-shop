package com.ryazancev.notification.util.exception;

import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.exception.OnlineShopException;
import com.ryazancev.common.exception.ServiceUnavailableException;
import com.ryazancev.notification.util.exception.custom.InvalidScopeException;
import com.ryazancev.notification.util.exception.custom.NotificationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@RestControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ExceptionBody> handleServiceUnavailable(
            ServiceUnavailableException e) {

        log.error(e.getClass().getSimpleName());
        log.error(e.getMessage());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.NOTIFICATION,
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleNotificationNotFound(
            NotificationNotFoundException e) {

        log.error(e.getClass().getSimpleName());
        log.error(e.getMessage());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.NOTIFICATION,
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(InvalidScopeException.class)
    public ResponseEntity<ExceptionBody> handleInvalidScope(
            NotificationNotFoundException e) {

        log.error(e.getClass().getSimpleName());
        log.error(e.getMessage());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ExceptionBody(
                        e.getMessage(),
                        ServiceStage.NOTIFICATION,
                        e.getHttpStatus()
                ));
    }

    @ExceptionHandler(OnlineShopException.class)
    public ResponseEntity<ExceptionBody> handleOnlineShop(
            OnlineShopException e) {

        log.error(e.getClass().getSimpleName());
        log.error(e.getMessage());

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
        log.error(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionBody(
                        "Internal error: " + e.getMessage(),
                        ServiceStage.CUSTOMER,
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));
    }
}

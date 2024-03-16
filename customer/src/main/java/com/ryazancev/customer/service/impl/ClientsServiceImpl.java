package com.ryazancev.customer.service.impl;

import com.ryazancev.common.clients.NotificationClient;
import com.ryazancev.common.clients.PurchaseClient;
import com.ryazancev.common.clients.ReviewClient;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.common.exception.ServiceUnavailableException;
import com.ryazancev.customer.service.ClientsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Locale;


/**
 * @author Oleg Ryazancev
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final PurchaseClient purchaseClient;
    private final ReviewClient reviewClient;
    private final NotificationClient notificationClient;

    private final MessageSource messageSource;

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "purchaseServiceUnavailable"
    )
    public Object processPurchase(PurchaseEditDto purchaseEditDto) {

        return purchaseClient.processPurchase(purchaseEditDto);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "purchaseServiceUnavailable"
    )
    public Object getPurchasesByCustomerId(Long customerId) {

        return purchaseClient.getByCustomerId(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "reviewServiceUnavailable"
    )
    public Object getReviewsByCustomerId(Long customerId) {

        return reviewClient.getByCustomerId(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "notificationServiceUnavailable"
    )
    public Object getNotificationsByCustomerId(Long customerId,
                                               String scope) {
        return notificationClient
                .getNotificationsByRecipientId(customerId, scope);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "notificationServiceUnavailable"
    )
    public Object getNotificationById(String id, String scope) {

        return notificationClient.getNotificationById(id, scope);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "notificationServiceUnavailable"
    )
    public Object getRecipientIdByPrivateNotificationId(
            String notificationId) {

        return notificationClient
                .getRecipientIdByPrivateNotificationId(notificationId);
    }

    //Fallback methods

    private Object reviewServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                messageSource.getMessage(
                        "exception.customer.service_unavailable",
                        new Object[]{ServiceStage.REVIEW.toString()},
                        Locale.getDefault()
                ),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object purchaseServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(

                messageSource.getMessage(
                        "exception.customer.service_unavailable",
                        new Object[]{ServiceStage.PURCHASE.toString()},
                        Locale.getDefault()
                ),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object notificationServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                messageSource.getMessage(
                        "exception.customer.service_unavailable",
                        new Object[]{ServiceStage.NOTIFICATION.toString()},
                        Locale.getDefault()
                ),
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}

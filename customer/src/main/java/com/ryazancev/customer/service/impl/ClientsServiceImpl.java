package com.ryazancev.customer.service.impl;

import com.ryazancev.common.clients.NotificationClient;
import com.ryazancev.common.clients.ProductClient;
import com.ryazancev.common.clients.PurchaseClient;
import com.ryazancev.common.clients.ReviewClient;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.Fallback;
import com.ryazancev.common.dto.purchase.PurchaseEditDto;
import com.ryazancev.customer.service.ClientsService;
import com.ryazancev.customer.util.exception.CustomErrorCode;
import com.ryazancev.customer.util.exception.CustomExceptionFactory;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final PurchaseClient purchaseClient;
    private final ReviewClient reviewClient;
    private final NotificationClient notificationClient;
    private final ProductClient productClient;

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "purchaseServiceUnavailable"
    )
    public Object processPurchase(final PurchaseEditDto purchaseEditDto) {

        return purchaseClient.processPurchase(purchaseEditDto);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "purchaseServiceUnavailable"
    )
    public Object getPurchasesByCustomerId(final Long customerId) {

        return purchaseClient.getByCustomerId(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "reviewServiceUnavailable"
    )
    public Object getReviewsByCustomerId(final Long customerId) {

        return reviewClient.getByCustomerId(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "notificationServiceUnavailable"
    )
    public Object getNotificationsByCustomerId(final Long customerId,
                                               final String scope) {
        return notificationClient
                .getNotificationsByRecipientId(customerId, scope);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "notificationServiceUnavailable"
    )
    public Object getNotificationById(final String id,
                                      final String scope) {

        return notificationClient.getNotificationById(id, scope);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "notificationServiceUnavailable"
    )
    public Object getRecipientIdByPrivateNotificationId(
            final String notificationId) {

        return notificationClient
                .getRecipientIdByPrivateNotificationId(notificationId);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "productServiceUnavailable"
    )
    public Object getProductOwnerId(final Long productId) {

        return productClient.getOwnerId(productId);
    }

    @Override
    @CircuitBreaker(
            name = "customer",
            fallbackMethod = "getSimpleProductFallback"
    )
    public Element getSimpleProductById(final Long productId) {

        return productClient.getSimpleById(productId);
    }

    //Fallback methods

    private Object reviewServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {
            throw CustomExceptionFactory
                    .getServiceUnavailable()
                    .get(
                            CustomErrorCode.OS_CUSTOMER_SERVICE_UNAVAILABLE_503
                                    .getMessage(ServiceStage.REVIEW),
                            CustomErrorCode.OS_CUSTOMER_SERVICE_UNAVAILABLE_503
                                    .name());
        }

        throw e;
    }

    private Object productServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {
            throw CustomExceptionFactory
                    .getServiceUnavailable()
                    .get(
                            CustomErrorCode.OS_CUSTOMER_SERVICE_UNAVAILABLE_503
                                    .getMessage(ServiceStage.PRODUCT),
                            CustomErrorCode.OS_CUSTOMER_SERVICE_UNAVAILABLE_503
                                    .name());
        }

        throw e;
    }

    private Object purchaseServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {
            throw CustomExceptionFactory
                    .getServiceUnavailable()
                    .get(
                            CustomErrorCode.OS_CUSTOMER_SERVICE_UNAVAILABLE_503
                                    .getMessage(ServiceStage.PURCHASE),
                            CustomErrorCode.OS_CUSTOMER_SERVICE_UNAVAILABLE_503
                                    .name());
        }
        throw e;
    }

    private Object notificationServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {
            throw CustomExceptionFactory
                    .getServiceUnavailable()
                    .get(
                            CustomErrorCode.OS_CUSTOMER_SERVICE_UNAVAILABLE_503
                                    .getMessage(ServiceStage.NOTIFICATION),
                            CustomErrorCode.OS_CUSTOMER_SERVICE_UNAVAILABLE_503
                                    .name());
        }
        throw e;
    }

    private Element getSimpleProductFallback(final Exception e) {

        return Fallback.builder()
                .message(
                        CustomErrorCode.OS_CUSTOMER_SERVICE_UNAVAILABLE_503
                                .getMessage(ServiceStage.PRODUCT)
                )
                .build();
    }
}

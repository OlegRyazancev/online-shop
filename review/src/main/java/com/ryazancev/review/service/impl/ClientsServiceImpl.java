package com.ryazancev.review.service.impl;

import com.ryazancev.common.clients.CustomerClient;
import com.ryazancev.common.clients.ProductClient;
import com.ryazancev.common.clients.PurchaseClient;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.Fallback;
import com.ryazancev.common.exception.ServiceUnavailableException;
import com.ryazancev.review.service.ClientsService;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Locale;


/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final ProductClient productClient;
    private final CustomerClient customerClient;
    private final PurchaseClient purchaseClient;

    private final MessageSource messageSource;

    @Override
    @CircuitBreaker(
            name = "review",
            fallbackMethod = "getSimpleProductFallback"
    )
    public Element getSimpleProductById(final Long productId) {

        return productClient.getSimpleById(productId);
    }

    @Override
    @CircuitBreaker(
            name = "review",
            fallbackMethod = "getSimpleCustomerFallback"
    )
    public Element getSimpleCustomerById(final Long customerId) {

        return customerClient.getSimpleById(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "review",
            fallbackMethod = "purchaseServiceUnavailable"
    )
    public Object getPurchaseById(final String purchaseId) {

        return purchaseClient.getById(purchaseId);
    }

    @Override
    @CircuitBreaker(
            name = "review",
            fallbackMethod = "getOwnerIdFallback"
    )
    public Long getOwnerIdByProductId(final Long productId) {

        return productClient.getOwnerId(productId);
    }

//    Fallback methods

    private Element getSimpleProductFallback(final Exception e) {

        return Fallback.builder()
                .message(
                        messageSource.getMessage(
                                "exception.review.service_unavailable",
                                new Object[]{ServiceStage.PRODUCT},
                                Locale.getDefault()
                        )
                )
                .build();
    }

    private Element getSimpleCustomerFallback(final Exception e) {

        return Fallback.builder()
                .message(
                        messageSource.getMessage(
                                "exception.review.service_unavailable",
                                new Object[]{ServiceStage.CUSTOMER},
                                Locale.getDefault()
                        )
                )
                .build();
    }

    private Object purchaseServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {
            throw new ServiceUnavailableException(
                    messageSource.getMessage(
                            "exception.review.service_unavailable",
                            new Object[]{ServiceStage.PURCHASE},
                            Locale.getDefault()
                    ),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
        throw e;
    }

    private Long getOwnerIdFallback(final Exception e) {

        return -1L;
    }
}

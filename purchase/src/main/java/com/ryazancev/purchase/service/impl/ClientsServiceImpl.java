package com.ryazancev.purchase.service.impl;

import com.ryazancev.common.clients.CustomerClient;
import com.ryazancev.common.clients.ProductClient;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.Fallback;
import com.ryazancev.common.exception.ServiceUnavailableException;
import com.ryazancev.purchase.service.ClientsService;
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

    private final MessageSource messageSource;

    @Override
    @CircuitBreaker(
            name = "purchase",
            fallbackMethod = "getSimpleCustomerFallback"
    )
    public Element getSimpleCustomerById(Long customerId) {

        return customerClient.getSimpleById(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "purchase",
            fallbackMethod = "getSimpleProductFallback"
    )
    public Element getSimpleProductById(Long productId) {

        return productClient.getSimpleById(productId);
    }

    @Override
    @CircuitBreaker(
            name = "purchase",
            fallbackMethod = "customerServiceUnavailable"
    )
    public Object getCustomerBalance(Long customerId) {

        return customerClient.getBalanceById(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "purchase",
            fallbackMethod = "productServiceUnavailable"
    )
    public Object getProductPriceAndQuantity(Long productId) {

        return productClient.getPriceAndQuantityByProductId(productId);
    }

    @Override
    @CircuitBreaker(
            name = "purchase",
            fallbackMethod = "productServiceUnavailable"
    )
    public Object getProductOwnerId(Long productId) {

        return productClient.getOwnerId(productId);
    }

//    Fallback methods

    private Object productServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                messageSource.getMessage(
                        "exception.purchase.service_unavailable",
                        new Object[]{ServiceStage.PRODUCT},
                        Locale.getDefault()
                ),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object customerServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                messageSource.getMessage(
                        "exception.purchase.service_unavailable",
                        new Object[]{ServiceStage.CUSTOMER},
                        Locale.getDefault()
                ),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Element getSimpleProductFallback(Exception e) {

        return Fallback.builder()
                .message(
                        messageSource.getMessage(
                                "exception.purchase.service_unavailable",
                                new Object[]{ServiceStage.PRODUCT},
                                Locale.getDefault()
                        )
                )
                .build();
    }

    private Element getSimpleCustomerFallback(Exception e) {

        return Fallback.builder()
                .message(
                        messageSource.getMessage(
                                "exception.purchase.service_unavailable",
                                new Object[]{ServiceStage.CUSTOMER},
                                Locale.getDefault()
                        )
                )
                .build();
    }
}

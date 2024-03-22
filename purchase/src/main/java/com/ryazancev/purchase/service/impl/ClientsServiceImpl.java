package com.ryazancev.purchase.service.impl;

import com.ryazancev.common.clients.CustomerClient;
import com.ryazancev.common.clients.ProductClient;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.Fallback;
import com.ryazancev.purchase.service.ClientsService;
import com.ryazancev.purchase.util.exception.CustomErrorCode;
import com.ryazancev.purchase.util.exception.CustomExceptionFactory;
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

    private final ProductClient productClient;
    private final CustomerClient customerClient;

    @Override
    @CircuitBreaker(
            name = "purchase",
            fallbackMethod = "getSimpleCustomerFallback"
    )
    public Element getSimpleCustomerById(final Long customerId) {

        return customerClient.getSimpleById(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "purchase",
            fallbackMethod = "getSimpleProductFallback"
    )
    public Element getSimpleProductById(final Long productId) {

        return productClient.getSimpleById(productId);
    }

    @Override
    @CircuitBreaker(
            name = "purchase",
            fallbackMethod = "customerServiceUnavailable"
    )
    public Object getCustomerBalance(final Long customerId) {

        return customerClient.getBalanceById(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "purchase",
            fallbackMethod = "productServiceUnavailable"
    )
    public Object getProductPriceAndQuantity(final Long productId) {

        return productClient.getPriceAndQuantityByProductId(productId);
    }

    @Override
    @CircuitBreaker(
            name = "purchase",
            fallbackMethod = "productServiceUnavailable"
    )
    public Object getProductOwnerId(final Long productId) {

        return productClient.getOwnerId(productId);
    }

//    Fallback methods

    private Object productServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {

            throw CustomExceptionFactory
                    .getServiceUnavailable()
                    .get(
                            CustomErrorCode.OS_PURCHASE_SERVICE_UNAVAILABLE_503
                                    .getMessage(ServiceStage.PRODUCT),
                            CustomErrorCode.OS_PURCHASE_SERVICE_UNAVAILABLE_503
                                    .name());
        }
        throw e;
    }

    private Object customerServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {
            throw CustomExceptionFactory
                    .getServiceUnavailable()
                    .get(
                            CustomErrorCode.OS_PURCHASE_SERVICE_UNAVAILABLE_503
                                    .getMessage(ServiceStage.CUSTOMER),
                            CustomErrorCode.OS_PURCHASE_SERVICE_UNAVAILABLE_503
                                    .name());
        }
        throw e;
    }

    private Element getSimpleProductFallback(final Exception e) {

        return Fallback.builder()
                .message(
                        CustomErrorCode.OS_PURCHASE_SERVICE_UNAVAILABLE_503
                                .getMessage(ServiceStage.PRODUCT)
                )
                .build();
    }

    private Element getSimpleCustomerFallback(final Exception e) {

        return Fallback.builder()
                .message(
                        CustomErrorCode.OS_PURCHASE_SERVICE_UNAVAILABLE_503
                                .getMessage(ServiceStage.CUSTOMER)
                )
                .build();
    }
}

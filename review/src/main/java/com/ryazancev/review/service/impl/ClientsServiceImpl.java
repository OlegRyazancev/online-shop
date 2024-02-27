package com.ryazancev.review.service.impl;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.clients.PurchaseClient;
import com.ryazancev.dto.Element;
import com.ryazancev.dto.Fallback;
import com.ryazancev.exception.ServiceUnavailableException;
import com.ryazancev.review.service.ClientsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ryazancev.review.util.exception.Message.*;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final ProductClient productClient;
    private final CustomerClient customerClient;
    private final PurchaseClient purchaseClient;

    @Override
    @CircuitBreaker(
            name = "review",
            fallbackMethod = "getSimpleProductFallback"
    )
    public Element getSimpleProductById(Long productId) {

        return productClient.getSimpleById(productId);
    }

    @Override
    @CircuitBreaker(
            name = "review",
            fallbackMethod = "getSimpleCustomerFallback"
    )
    public Element getSimpleCustomerById(Long customerId) {

        return customerClient.getSimpleById(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "review",
            fallbackMethod = "purchaseServiceUnavailable"
    )
    public Object getPurchaseById(String purchaseId) {

        return purchaseClient.getById(purchaseId);
    }

//    Fallback methods

    private Element getSimpleProductFallback(Exception e) {

        return Fallback.builder()
                .message(PRODUCT_SERVICE_UNAVAILABLE)
                .build();
    }

    private Element getSimpleCustomerFallback(Exception e) {

        return Fallback.builder()
                .message(CUSTOMER_SERVICE_UNAVAILABLE)
                .build();
    }

    private Object purchaseServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                PURCHASE_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}

package com.ryazancev.purchase.service.impl;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.dto.Element;
import com.ryazancev.dto.Fallback;
import com.ryazancev.purchase.service.ClientsService;
import com.ryazancev.purchase.util.exception.custom.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ryazancev.purchase.util.exception.Message.CUSTOMER_SERVICE_UNAVAILABLE;
import static com.ryazancev.purchase.util.exception.Message.PRODUCT_SERVICE_UNAVAILABLE;

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
    public Element getSimpleCustomer(Long customerId) {

        return customerClient.getSimpleById(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "purchase",
            fallbackMethod = "getSimpleProductFallback"
    )
    public Element getSimpleProduct(Long productId) {

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
                PRODUCT_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object customerServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                CUSTOMER_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }

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
}

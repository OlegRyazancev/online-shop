package com.ryazancev.customer.service.impl;

import com.ryazancev.clients.PurchaseClient;
import com.ryazancev.clients.ReviewClient;
import com.ryazancev.customer.service.ClientsService;
import com.ryazancev.customer.util.exception.custom.ServiceUnavailableException;
import com.ryazancev.dto.purchase.PurchaseEditDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ryazancev.customer.util.exception.Message.PURCHASE_SERVICE_UNAVAILABLE;
import static com.ryazancev.customer.util.exception.Message.REVIEW_SERVICE_UNAVAILABLE;

/**
 * @author Oleg Ryazancev
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final PurchaseClient purchaseClient;
    private final ReviewClient reviewClient;

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

    //Fallback methods

    private Object reviewServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                REVIEW_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object purchaseServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                PURCHASE_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}

package com.ryazancev.product.service.impl;

import com.ryazancev.clients.CustomerClient;
import com.ryazancev.clients.OrganizationClient;
import com.ryazancev.clients.PurchaseClient;
import com.ryazancev.clients.ReviewClient;
import com.ryazancev.dto.Element;
import com.ryazancev.dto.Fallback;
import com.ryazancev.dto.review.ReviewEditDto;
import com.ryazancev.exception.ServiceUnavailableException;
import com.ryazancev.product.service.ClientsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ryazancev.product.util.exception.Message.*;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {


    private final OrganizationClient organizationClient;
    private final ReviewClient reviewClient;
    private final CustomerClient customerClient;
    private final PurchaseClient purchaseClient;

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "getSimpleCustomerFallback"
    )
    public Element getSimpleCustomerById(Long customerId) {

        return customerClient.getSimpleById(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "getSimpleOrganizationFallback"
    )
    public Element getSimpleOrganizationById(Long organizationId) {

        return organizationClient.getSimpleById(organizationId);
    }

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "getAverageRatingFallback"
    )
    public Double getAverageRatingByProductId(Long id) {

        return reviewClient.getAverageRatingByProductId(id);
    }


    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "organizationServiceUnavailable"
    )
    public Object getOrganizationOwnerIdById(Long organizationId) {

        return organizationClient.getOwnerId(organizationId);
    }


    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "reviewServiceUnavailable"
    )
    public Object getReviewsByProductId(Long id) {

        return reviewClient.getByProductId(id);
    }

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "reviewServiceUnavailable"
    )
    public Object createReview(ReviewEditDto reviewEditDto) {

        return reviewClient.create(reviewEditDto);
    }

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "purchaseServiceUnavailable"
    )
    public Object getPurchaseById(String purchaseId) {

        return purchaseClient.getById(purchaseId);
    }

    //Fallback methods

    private Object customerServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                CUSTOMER_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object organizationServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                ORGANIZATION_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object reviewServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                REVIEW_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object getSimpleCustomerFallback(Exception e) {

        return Fallback.builder()
                .message(CUSTOMER_SERVICE_UNAVAILABLE)
                .build();
    }

    private Object getSimpleOrganizationFallback(Exception e) {

        return Fallback.builder()
                .message(ORGANIZATION_SERVICE_UNAVAILABLE)
                .build();
    }

    private Double getAverageRatingFallback(Exception e) {

        return -1.0;
    }

    private Object purchaseServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                PURCHASE_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}

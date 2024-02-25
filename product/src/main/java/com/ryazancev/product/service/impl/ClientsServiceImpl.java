package com.ryazancev.product.service.impl;

import com.ryazancev.clients.OrganizationClient;
import com.ryazancev.clients.ReviewClient;
import com.ryazancev.dto.review.ReviewEditDto;
import com.ryazancev.product.service.ClientsService;
import com.ryazancev.product.util.exception.custom.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ryazancev.product.util.exception.Message.ORGANIZATION_SERVICE_UNAVAILABLE;
import static com.ryazancev.product.util.exception.Message.REVIEW_SERVICE_UNAVAILABLE;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final OrganizationClient organizationClient;
    private final ReviewClient reviewClient;

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "organizationServiceUnavailable"
    )
    public Object getSimpleOrganizationById(Long organizationId) {

        return organizationClient.getSimpleById(organizationId);
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
    public Object getAverageRatingByProductId(Long id) {

        return reviewClient.getAverageRatingByProductId(id);
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

    //Fallback methods

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
}

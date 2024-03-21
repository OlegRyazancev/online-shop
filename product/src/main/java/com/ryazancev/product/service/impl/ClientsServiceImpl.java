package com.ryazancev.product.service.impl;

import com.ryazancev.common.clients.CustomerClient;
import com.ryazancev.common.clients.OrganizationClient;
import com.ryazancev.common.clients.PurchaseClient;
import com.ryazancev.common.clients.ReviewClient;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.Fallback;
import com.ryazancev.common.dto.review.ReviewEditDto;
import com.ryazancev.common.exception.ServiceUnavailableException;
import com.ryazancev.product.service.ClientsService;
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


    private final OrganizationClient organizationClient;
    private final ReviewClient reviewClient;
    private final CustomerClient customerClient;
    private final PurchaseClient purchaseClient;

    private final MessageSource messageSource;

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "getSimpleCustomerFallback"
    )
    public Element getSimpleCustomerById(final Long customerId) {

        return customerClient.getSimpleById(customerId);
    }

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "getSimpleOrganizationFallback"
    )
    public Element getSimpleOrganizationById(final Long organizationId) {

        return organizationClient.getSimpleById(organizationId);
    }

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "getAverageRatingFallback"
    )
    public Double getAverageRatingByProductId(final Long id) {

        return reviewClient.getAverageRatingByProductId(id);
    }

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "organizationServiceUnavailable"
    )
    public Object getOwnerByOrganizationId(final Long organizationId) {

        return organizationClient.getOwnerId(organizationId);
    }

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "reviewServiceUnavailable"
    )
    public Object getReviewsByProductId(final Long id) {

        return reviewClient.getByProductId(id);
    }

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "reviewServiceUnavailable"
    )
    public Object createReview(final ReviewEditDto reviewEditDto) {

        return reviewClient.create(reviewEditDto);
    }

    @Override
    @CircuitBreaker(
            name = "product",
            fallbackMethod = "purchaseServiceUnavailable"
    )
    public Object getPurchaseById(final String purchaseId) {

        return purchaseClient.getById(purchaseId);
    }

    //Fallback methods

    private Object customerServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {
            throw new ServiceUnavailableException(
                    messageSource.getMessage(
                            "OS-PRODUCT-SERVICE_UNAVAILABLE-503",
                            new Object[]{ServiceStage.CUSTOMER},
                            Locale.getDefault()
                    ),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
        throw e;
    }

    private Object organizationServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {
            throw new ServiceUnavailableException(
                    messageSource.getMessage(
                            "OS-PRODUCT-SERVICE_UNAVAILABLE-503",
                            new Object[]{ServiceStage.ORGANIZATION},
                            Locale.getDefault()
                    ),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
        throw e;
    }

    private Object reviewServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {
            throw new ServiceUnavailableException(
                    messageSource.getMessage(
                            "OS-PRODUCT-SERVICE_UNAVAILABLE-503",
                            new Object[]{ServiceStage.REVIEW},
                            Locale.getDefault()
                    ),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
        throw e;
    }

    private Object purchaseServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {
            throw new ServiceUnavailableException(
                    messageSource.getMessage(
                            "OS-PRODUCT-SERVICE_UNAVAILABLE-503",
                            new Object[]{ServiceStage.PURCHASE},
                            Locale.getDefault()
                    ),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
        throw e;
    }

    private Element getSimpleCustomerFallback(final Exception e) {

        return Fallback.builder()
                .message(
                        messageSource.getMessage(
                                "OS-PRODUCT-SERVICE_UNAVAILABLE-503",
                                new Object[]{ServiceStage.CUSTOMER},
                                Locale.getDefault()
                        )
                )
                .build();
    }

    private Element getSimpleOrganizationFallback(final Exception e) {

        return Fallback.builder()
                .message(
                        messageSource.getMessage(
                                "OS-PRODUCT-SERVICE_UNAVAILABLE-503",
                                new Object[]{ServiceStage.ORGANIZATION},
                                Locale.getDefault()
                        )
                )
                .build();
    }

    private Double getAverageRatingFallback(final Exception e) {

        return -1.0;
    }
}

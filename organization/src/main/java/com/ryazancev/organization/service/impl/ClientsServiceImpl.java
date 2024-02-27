package com.ryazancev.organization.service.impl;

import com.ryazancev.common.clients.CustomerClient;
import com.ryazancev.common.clients.LogoClient;
import com.ryazancev.common.clients.ProductClient;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.dto.Fallback;
import com.ryazancev.common.exception.ServiceUnavailableException;
import com.ryazancev.organization.service.ClientsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.ryazancev.organization.util.exception.Message.*;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final LogoClient logoClient;
    private final ProductClient productClient;
    private final CustomerClient customerClient;

    @Override
    @CircuitBreaker(
            name = "organization",
            fallbackMethod = "logoServiceUnavailable"
    )
    public Object uploadLogo(MultipartFile multipartFile) {

        return logoClient.upload(multipartFile);
    }

    @Override
    @CircuitBreaker(
            name = "organization",
            fallbackMethod = "productServiceUnavailable"
    )
    public Object getProductsByOrganizationId(Long id) {

        return productClient.getProductsByOrganizationId(id);
    }

    @Override
    @CircuitBreaker(
            name = "organization",
            fallbackMethod = "getSimpleCustomerFallback"
    )
    public Element getSimpleCustomerById(Long customerId) {

        return customerClient.getSimpleById(customerId);
    }

    //Fallback methods

    private Element getSimpleCustomerFallback(Exception e) {

        return Fallback.builder()
                .message(CUSTOMER_SERVICE_UNAVAILABLE)
                .build();
    }

    private Object customerServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                CUSTOMER_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object logoServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                LOGO_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Object productServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                PRODUCT_SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}

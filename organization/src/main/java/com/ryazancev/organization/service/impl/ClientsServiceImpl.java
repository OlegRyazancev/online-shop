package com.ryazancev.organization.service.impl;

import com.ryazancev.clients.LogoClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.organization.service.ClientsService;
import com.ryazancev.organization.util.exception.custom.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.ryazancev.organization.util.exception.Message.LOGO_SERVICE_UNAVAILABLE;
import static com.ryazancev.organization.util.exception.Message.PRODUCT_SERVICE_UNAVAILABLE;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class ClientsServiceImpl implements ClientsService {

    private final LogoClient logoClient;
    private final ProductClient productClient;

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

    //Fallback methods

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

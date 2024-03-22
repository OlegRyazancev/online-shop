package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.service.ClientsService;
import com.ryazancev.auth.util.exception.CustomErrorCode;
import com.ryazancev.auth.util.exception.CustomExceptionFactory;
import com.ryazancev.common.clients.CustomerClient;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.customer.CustomerDto;
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

    private final CustomerClient customerClient;

    @Override
    @CircuitBreaker(
            name = "auth",
            fallbackMethod = "customerServiceUnavailable"
    )
    public Object createCustomer(final CustomerDto customerDto) {

        return customerClient.createCustomer(customerDto);
    }

    private Object customerServiceUnavailable(final Exception e)
            throws Exception {

        if (e instanceof RetryableException) {
            throw CustomExceptionFactory
                    .getServiceUnavailable()
                    .get(
                            CustomErrorCode.OS_AUTH_SERVICE_UNAVAILABLE_503
                                    .getMessage(ServiceStage.CUSTOMER),
                            CustomErrorCode.OS_AUTH_SERVICE_UNAVAILABLE_503
                                    .name()
                    );
        }
        throw e;
    }
}

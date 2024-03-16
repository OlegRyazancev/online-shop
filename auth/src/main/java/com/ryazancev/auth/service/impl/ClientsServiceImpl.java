package com.ryazancev.auth.service.impl;

import com.ryazancev.auth.service.ClientsService;
import com.ryazancev.common.clients.CustomerClient;
import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final CustomerClient customerClient;
    private final MessageSource messageSource;

    @Override
    @CircuitBreaker(
            name = "auth",
            fallbackMethod = "customerServiceUnavailable"
    )
    public Object createCustomer(CustomerDto customerDto) {

        return customerClient.createCustomer(customerDto);
    }

    private Object customerServiceUnavailable(Exception e) {

        throw new ServiceUnavailableException(
                messageSource.getMessage(
                        "exception.auth.service_unavailable",
                        new Object[]{ServiceStage.CUSTOMER},
                        Locale.getDefault()
                ),
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}

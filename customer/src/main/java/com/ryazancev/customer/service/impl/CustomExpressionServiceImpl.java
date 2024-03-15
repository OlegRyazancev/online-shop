package com.ryazancev.customer.service.impl;

import com.ryazancev.customer.service.ClientsService;
import com.ryazancev.customer.service.CustomExpressionService;
import com.ryazancev.customer.util.RequestHeadersProperties;
import com.ryazancev.customer.util.exception.custom.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import static com.ryazancev.customer.util.exception.Message.*;

/**
 * @author Oleg Ryazancev
 */

@Service
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final ClientsService clientsService;

    private final RequestHeadersProperties headersProperties;

    public CustomExpressionServiceImpl(final HttpServletRequest request,
                                       ClientsService clientsService) {
        this.clientsService = clientsService;
        this.headersProperties = new RequestHeadersProperties(request);
    }

    @Override
    public void checkAccountConditions() {

        checkIfAccountLocked();
        checkIfAccountConfirmed();
    }

    private void checkIfAccountConfirmed() {
        if (!headersProperties.isConfirmed()) {
            throw new AccessDeniedException(
                    EMAIL_NOT_CONFIRMED,
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessCustomer(Long customerId) {

        if (!(headersProperties.getUserId().equals(customerId)
                || headersProperties.getRoles().contains("ROLE_ADMIN"))) {

            throw new AccessDeniedException(
                    ACCESS_CUSTOMER,
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkIfAccountLocked() {

        if (headersProperties.isLocked()) {
            throw new AccessDeniedException(
                    ACCOUNT_LOCKED,
                    HttpStatus.FORBIDDEN);

        }
    }

    @Override
    public void checkIfCustomerIsAdmin(Long id) {

        if (!headersProperties.getRoles().contains("ROLE_ADMIN")) {

            throw new AccessDeniedException(
                    ACCESS_NOTIFICATIONS,
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessPrivateNotification(Long customerId,
                                               String notificationId) {

        Long recipientId = (Long) clientsService
                .getRecipientIdByPrivateNotificationId(notificationId);

        if (!recipientId.equals(headersProperties.getUserId())) {
            throw new AccessDeniedException(
                    String.format(ACCESS_PRIVATE_NOTIFICATION, notificationId),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}

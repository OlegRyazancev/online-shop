package com.ryazancev.customer.service.impl;

import com.ryazancev.customer.service.ClientsService;
import com.ryazancev.customer.service.CustomExpressionService;
import com.ryazancev.customer.util.RequestHeadersProperties;
import com.ryazancev.customer.util.exception.custom.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Service
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final ClientsService clientsService;
    private final MessageSource messageSource;

    private final RequestHeadersProperties headersProperties;

    public CustomExpressionServiceImpl(final HttpServletRequest request,
                                       ClientsService clientsService,
                                       MessageSource messageSource) {
        this.headersProperties = new RequestHeadersProperties(request);
        this.clientsService = clientsService;
        this.messageSource = messageSource;
    }

    @Override
    public void checkAccountConditions() {

        checkIfAccountLocked();
        checkIfAccountConfirmed();
    }

    private void checkIfAccountConfirmed() {
        if (!headersProperties.isConfirmed()) {
            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "email_not_confirmed",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessCustomer(Long customerId) {

        if (!(headersProperties.getUserId().equals(customerId)
                || headersProperties.getRoles().contains("ROLE_ADMIN"))) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "access_customer",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkIfAccountLocked() {

        if (headersProperties.isLocked()) {
            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "account_locked",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);

        }
    }

    @Override
    public void checkIfCustomerIsAdmin(Long id) {

        if (!headersProperties.getRoles().contains("ROLE_ADMIN")) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "access_notifications",
                            null,
                            Locale.getDefault()
                    ),
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
                    messageSource.getMessage(
                            "access_private_notification",
                            new Object[]{notificationId},
                            Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}

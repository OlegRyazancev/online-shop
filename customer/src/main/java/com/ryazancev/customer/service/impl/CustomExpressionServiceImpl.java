package com.ryazancev.customer.service.impl;

import com.ryazancev.customer.service.ClientsService;
import com.ryazancev.customer.service.CustomExpressionService;
import com.ryazancev.customer.util.RequestHeader;
import com.ryazancev.customer.util.exception.custom.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
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
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final HttpServletRequest request;
    private final ClientsService clientsService;
    private final MessageSource messageSource;

    @Override
    public void checkAccountConditions() {

        checkIfAccountLocked();
        checkIfEmailConfirmed();
    }

    private void checkIfEmailConfirmed() {

        RequestHeader requestHeader = new RequestHeader(request);

        if (!requestHeader.isConfirmed()) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.customer.email_not_confirmed",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessUser(final Long customerId) {

        RequestHeader requestHeader = new RequestHeader(request);

        if (!(requestHeader.getUserId().equals(customerId)
                || requestHeader.getRoles().contains("ROLE_ADMIN"))) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.customer.access_customer",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkIfAccountLocked() {

        RequestHeader requestHeader = new RequestHeader(request);

        if (requestHeader.isLocked()) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.customer.account_locked",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);

        }
    }

    @Override
    public void checkIfCustomerIsAdmin(final Long id) {

        RequestHeader requestHeader = new RequestHeader(request);

        if (!requestHeader.getRoles().contains("ROLE_ADMIN")) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.customer.access_notifications",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessPrivateNotification(final Long customerId,
                                               final String notificationId) {

        RequestHeader requestHeader = new RequestHeader(request);

        Long recipientId = (Long) clientsService
                .getRecipientIdByPrivateNotificationId(notificationId);

        if (!recipientId.equals(requestHeader.getUserId())) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.customer.access_private_notification",
                            new Object[]{notificationId},
                            Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}

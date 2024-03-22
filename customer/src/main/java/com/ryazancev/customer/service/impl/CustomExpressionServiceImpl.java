package com.ryazancev.customer.service.impl;

import com.ryazancev.customer.service.ClientsService;
import com.ryazancev.customer.service.CustomExpressionService;
import com.ryazancev.customer.util.RequestHeader;
import com.ryazancev.customer.util.exception.CustomExceptionFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final HttpServletRequest request;
    private final ClientsService clientsService;

    @Override
    public void checkAccountConditions() {

        checkIfAccountLocked();
        checkIfEmailConfirmed();
    }

    private void checkIfEmailConfirmed() {

        RequestHeader requestHeader = new RequestHeader(request);

        if (!requestHeader.isConfirmed()) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .emailNotConfirmed();
        }
    }

    @Override
    public void checkAccessUser(final Long customerId) {

        RequestHeader requestHeader = new RequestHeader(request);

        if (!(requestHeader.getUserId().equals(customerId)
                || requestHeader.getRoles().contains("ROLE_ADMIN"))) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .cannotAccessCustomer(String.valueOf(customerId));
        }
    }

    @Override
    public void checkIfAccountLocked() {

        RequestHeader requestHeader = new RequestHeader(request);

        if (requestHeader.isLocked()) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .accountLocked();
        }
    }

    @Override
    public void checkIfCustomerIsAdmin(final Long id) {

        RequestHeader requestHeader = new RequestHeader(request);

        if (!requestHeader.getRoles().contains("ROLE_ADMIN")) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .cannotAccessNotifications();
        }
    }

    @Override
    public void checkAccessPrivateNotification(final Long customerId,
                                               final String notificationId) {

        RequestHeader requestHeader = new RequestHeader(request);

        Long recipientId = (Long) clientsService
                .getRecipientIdByPrivateNotificationId(notificationId);

        if (!recipientId.equals(requestHeader.getUserId())) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .cannotAccessPrivateNotification(notificationId);
        }
    }
}

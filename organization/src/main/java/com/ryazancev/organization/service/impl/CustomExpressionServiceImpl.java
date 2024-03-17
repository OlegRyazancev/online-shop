package com.ryazancev.organization.service.impl;

import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.CustomExpressionService;
import com.ryazancev.organization.util.RequestHeader;
import com.ryazancev.organization.util.exception.custom.AccessDeniedException;
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

    private final OrganizationRepository organizationRepository;

    private final MessageSource messageSource;

    private final HttpServletRequest request;

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
                            "exception.organization.email_not_confirmed",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessOrganization(Long id) {

        RequestHeader requestHeader = new RequestHeader(request);

        if (!organizationRepository.isOrganizationOwner(
                requestHeader.getUserId(), id)
                || requestHeader.getRoles().contains("ROLE_ADMIN")) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.organization.access_object",
                            new Object[]{
                                    ServiceStage.ORGANIZATION,
                                    id
                            },
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessUser(Long customerId) {

        RequestHeader requestHeader = new RequestHeader(request);

        if (!(customerId.equals(requestHeader.getUserId())
                || requestHeader.getRoles().contains("ROLE_ADMIN"))) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.organization.access_object",
                            new Object[]{
                                    ServiceStage.CUSTOMER,
                                    customerId
                            },
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
                            "exception.organization.account_locked",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }
}

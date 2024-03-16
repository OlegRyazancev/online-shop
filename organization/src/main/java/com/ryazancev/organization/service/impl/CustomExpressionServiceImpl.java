package com.ryazancev.organization.service.impl;

import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.CustomExpressionService;
import com.ryazancev.organization.util.RequestHeader;
import com.ryazancev.organization.util.exception.custom.AccessDeniedException;
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

    private final OrganizationRepository organizationRepository;

    private final MessageSource messageSource;

    private final RequestHeader requestHeader;

    public CustomExpressionServiceImpl(final HttpServletRequest request,
                                       OrganizationRepository organizationRepository,
                                       MessageSource messageSource) {
        this.requestHeader = new RequestHeader(request);
        this.organizationRepository = organizationRepository;
        this.messageSource = messageSource;
    }

    @Override
    public void checkAccountConditions() {

        checkIfAccountLocked();
        checkIfEmailConfirmed();
    }

    private void checkIfEmailConfirmed() {

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

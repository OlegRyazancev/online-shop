package com.ryazancev.organization.service.impl;

import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.CustomExpressionService;
import com.ryazancev.organization.util.RequestHeader;
import com.ryazancev.organization.util.exception.CustomExceptionFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final OrganizationRepository organizationRepository;

    private final HttpServletRequest request;

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
    public void checkAccessOrganization(final Long id) {

        RequestHeader requestHeader = new RequestHeader(request);

        if (!organizationRepository.isOrganizationOwner(
                requestHeader.getUserId(), id)
                || requestHeader.getRoles().contains("ROLE_ADMIN")) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .cannotAccessObject(
                            ObjectType.ORGANIZATION,
                            String.valueOf(id)
                    );
        }
    }

    @Override
    public void checkAccessUser(final Long customerId) {

        RequestHeader requestHeader = new RequestHeader(request);

        if (!(customerId.equals(requestHeader.getUserId())
                || requestHeader.getRoles().contains("ROLE_ADMIN"))) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .cannotAccessObject(
                            ObjectType.CUSTOMER,
                            String.valueOf(customerId)
                    );
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
}

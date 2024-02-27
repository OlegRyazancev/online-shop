package com.ryazancev.organization.service.impl;

import com.ryazancev.common.dto.organization.OrganizationEditDto;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.CustomExpressionService;
import com.ryazancev.organization.util.exception.custom.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.ryazancev.organization.util.exception.Message.*;

/**
 * @author Oleg Ryazancev
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final HttpServletRequest request;
    private final OrganizationRepository organizationRepository;

    @Override
    public void checkAccountConditions() {

        checkIfAccountLocked();
        checkIfEmailConfirmed();
    }

    @Override
    public void checkAccessOrganization(Long id) {

        if (!canAccessOrganization(id)) {
            throw new AccessDeniedException(
                    ACCESS_ORGANIZATION,
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessUser(OrganizationEditDto organizationEditDto) {

        if (!canAccessUser(organizationEditDto.getOwnerId())) {
            throw new AccessDeniedException(
                    ACCESS_CUSTOMER,
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkIfAccountLocked() {

        boolean locked = Boolean.parseBoolean(request.getHeader("locked"));

        if (locked) {
            throw new AccessDeniedException(
                    ACCOUNT_LOCKED,
                    HttpStatus.FORBIDDEN);
        }
    }

    private void checkIfEmailConfirmed() {

        boolean confirmed = Boolean.parseBoolean(
                request.getHeader("confirmed"));

        if (!confirmed) {
            throw new AccessDeniedException(
                    EMAIL_NOT_CONFIRMED,
                    HttpStatus.FORBIDDEN);
        }
    }

    private boolean canAccessOrganization(Long organizationId) {

        Long currentUserId = getUserIdFromRequest();
        List<String> userRoles = getUserRolesFromRequest();

        return organizationRepository
                .isOrganizationOwner(currentUserId, organizationId)
                || userRoles.contains("ROLE_ADMIN");
    }

    private boolean canAccessUser(Long userId) {

        Long currentUserId = getUserIdFromRequest();
        List<String> userRoles = getUserRolesFromRequest();

        return userId.equals(currentUserId)
                || userRoles.contains("ROLE_ADMIN");
    }


    private Long getUserIdFromRequest() {

        return Long.valueOf(request.getHeader("userId"));
    }

    private List<String> getUserRolesFromRequest() {

        return Arrays.asList(request.getHeader("roles").split(" "));
    }


}

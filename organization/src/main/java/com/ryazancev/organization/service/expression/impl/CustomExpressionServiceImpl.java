package com.ryazancev.organization.service.expression.impl;

import com.ryazancev.dto.organization.OrganizationEditDTO;
import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.expression.CustomExpressionService;
import com.ryazancev.organization.util.exception.custom.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final HttpServletRequest request;
    private final OrganizationRepository organizationRepository;

    @Override
    public void checkAccessOrganization(Long id) {

        if (!canAccessOrganization(id)) {
            throw new AccessDeniedException(
                    "You have no permissions to access to this organization",
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessUser(OrganizationEditDTO organizationEditDTO) {

        if (!canAccessUser(organizationEditDTO.getOwnerId())) {
            throw new AccessDeniedException(
                    "You have no permissions to access to this customer",
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkIfAccountLocked() {

        boolean locked = Boolean.parseBoolean(request.getHeader("locked"));

        if (locked) {
            throw new AccessDeniedException(
                    "Access denied because your account is locked",
                    HttpStatus.FORBIDDEN);
        }
    }

    private boolean canAccessOrganization(Long organizationId) {

        Long currentUserId = getUserIdFromRequest();
        List<String> userRoles = getUserRolesFromRequest();

        log.info("user Id = {}, user roles = {}", currentUserId, userRoles);

        return organizationRepository
                .isOrganizationOwner(currentUserId, organizationId)
                || userRoles.contains("ROLE_ADMIN");
    }

    private boolean canAccessUser(Long userId) {

        Long currentUserId = getUserIdFromRequest();
        List<String> userRoles = getUserRolesFromRequest();


        log.info("user Id = {}, user roles = {}", userId, userRoles);

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

package com.ryazancev.organization.service.impl;

import com.ryazancev.organization.repository.OrganizationRepository;
import com.ryazancev.organization.service.CustomExpressionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public boolean canAccessOrganization(Long organizationId) {
        Long currentUserId = getUserIdFromRequest();
        List<String> userRoles = getUserRolesFromRequest();

        log.info("user Id = {}, user roles = {}", currentUserId, userRoles);

        return organizationRepository
                .isOrganizationOwner(currentUserId, organizationId)
                || userRoles.contains("ROLE_ADMIN");
    }

    @Override
    public boolean canAccessUser(Long userId) {
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

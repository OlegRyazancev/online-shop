package com.ryazancev.product.service.impl;

import com.ryazancev.clients.OrganizationClient;
import com.ryazancev.clients.ProductClient;
import com.ryazancev.dto.product.ProductsSimpleResponse;
import com.ryazancev.product.service.CustomExpressionService;
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
    private final OrganizationClient organizationClient;
    private final ProductClient productClient;

    @Override
    public boolean canAccessOrganization(Long organizationId) {

        Long userId = getUserIdFromRequest(request);
        List<String> userRoles = getRolesFromRequest(request);

        log.info("user Id = {}, user roles = {}", userId, userRoles);

        Long ownerId = organizationClient.getOwnerId(organizationId);

        return userId.equals(ownerId)
                || userRoles.contains("ROLE_ADMIN");
    }

    @Override
    public boolean canAccessProduct(Long productId, Long organizationId) {

        if (!canAccessOrganization(organizationId)) {
            return false;
        }

        ProductsSimpleResponse productsResponse =
                productClient.getProductsByOrganizationId(organizationId);

        return productsResponse.getProducts()
                .stream()
                .anyMatch(productDTO ->
                        productDTO.getId().equals(productId));
    }


    private List<String> getRolesFromRequest(HttpServletRequest request) {

        return Arrays.asList(request.getHeader("roles").split(" "));
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {

        return Long.valueOf(request.getHeader("id"));
    }

}

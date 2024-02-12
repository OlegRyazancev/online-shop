package com.ryazancev.product.service.expression.impl;

import com.ryazancev.clients.OrganizationClient;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.service.expression.CustomExpressionService;
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
    private final ProductService productService;

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
    public boolean canAccessProduct(Long productId) {

        Product product = productService.getById(productId);
        Long organizationId = product.getOrganizationId();

        if (!canAccessOrganization(organizationId)) {
            return false;
        }
        log.info("User is organization owner, or user is admin");

        List<String> userRoles = getRolesFromRequest(request);
        List<Product> products =
                productService.getByOrganizationId(organizationId);

        return products
                .stream()
                .anyMatch(productDTO ->
                        productDTO.getId().equals(productId))
                || userRoles.contains("ROLE_ADMIN");
    }


    private List<String> getRolesFromRequest(HttpServletRequest request) {

        return Arrays.asList(request.getHeader("roles").split(" "));
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {

        return Long.valueOf(request.getHeader("userId"));
    }

}

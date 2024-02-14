package com.ryazancev.product.service.expression.impl;

import com.ryazancev.clients.OrganizationClient;
import com.ryazancev.dto.product.ProductEditDto;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.service.expression.CustomExpressionService;
import com.ryazancev.product.util.exception.custom.AccessDeniedException;
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
    private final OrganizationClient organizationClient;
    private final ProductService productService;

    public void checkAccessProduct(Long id) {

        if (!canAccessProduct(id)) {

            throw new AccessDeniedException(
                    "You have no permissions to access to this product",
                    HttpStatus.FORBIDDEN);
        }
    }

    public void checkAccessOrganization(ProductEditDto productEditDto) {

        if (!canAccessOrganization(productEditDto.getOrganizationId())) {

            throw new AccessDeniedException(
                    "You have no permissions to access to this organization",
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


    private boolean canAccessProduct(Long productId) {

        boolean statusCheck = true;

        Product product = productService.getById(productId, statusCheck);
        Long organizationId = product.getOrganizationId();

        if (canAccessOrganization(organizationId)) {

            log.info("User is organization owner, or user is admin");

            List<String> userRoles = getRolesFromRequest(request);
            List<Product> products =
                    productService.getByOrganizationId(organizationId);

            return products
                    .stream()
                    .anyMatch(productDto ->
                            productDto.getId().equals(productId))
                    || userRoles.contains("ROLE_ADMIN");
        }

        return false;
    }

    private boolean canAccessOrganization(Long organizationId) {

        Long userId = getUserIdFromRequest(request);
        List<String> userRoles = getRolesFromRequest(request);

        log.info("user Id = {}, user roles = {}", userId, userRoles);

        Long ownerId = organizationClient.getOwnerId(organizationId);

        return userId.equals(ownerId)
                || userRoles.contains("ROLE_ADMIN");
    }


    private List<String> getRolesFromRequest(HttpServletRequest request) {

        return Arrays.asList(request.getHeader("roles").split(" "));
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {

        return Long.valueOf(request.getHeader("userId"));
    }

}

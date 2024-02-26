package com.ryazancev.product.service.impl;

import com.ryazancev.dto.Fallback;
import com.ryazancev.dto.customer.CustomerDto;
import com.ryazancev.dto.product.ProductEditDto;
import com.ryazancev.dto.purchase.PurchaseDto;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ClientsService;
import com.ryazancev.product.service.CustomExpressionService;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.exception.custom.AccessDeniedException;
import com.ryazancev.product.util.exception.custom.ServiceUnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.ryazancev.product.util.exception.Message.*;

@Service
@RequiredArgsConstructor
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final HttpServletRequest request;

    private final ProductService productService;

    private final ClientsService clientsService;

//    private final OrganizationClient organizationClient;
//    private final PurchaseClient purchaseClient;

    public void checkAccessProduct(Long id) {

        if (!canAccessProduct(id)) {

            throw new AccessDeniedException(
                    ACCESS_PRODUCT,
                    HttpStatus.FORBIDDEN);
        }
    }

    public void checkAccessOrganization(ProductEditDto productEditDto) {

        if (!canAccessOrganization(productEditDto.getOrganizationId())) {

            throw new AccessDeniedException(
                    ACCESS_ORGANIZATION,
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

    @Override
    public void checkIfEmailConfirmed() {

        boolean confirmed = Boolean.parseBoolean(
                request.getHeader("confirmed"));

        if (!confirmed) {

            throw new AccessDeniedException(
                    EMAIL_NOT_CONFIRMED,
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessPurchase(String purchaseId) {

        if (!canAccessPurchase(purchaseId)) {

            throw new AccessDeniedException(
                    ACCESS_PURCHASE,
                    HttpStatus.FORBIDDEN);
        }
    }

    private boolean canAccessPurchase(String purchaseId) {

        Long userId = getUserIdFromRequest(request);
        List<String> userRoles = getRolesFromRequest(request);

        PurchaseDto purchaseDto = (PurchaseDto) clientsService
                .getPurchaseById(purchaseId);

        Object customer = purchaseDto.getCustomer();

        if (customer instanceof CustomerDto) {

            Long customerId = ((CustomerDto) customer).getId();

            return userId.equals(customerId)
                    || userRoles.contains("ROLE_ADMIN");

        } else if (customer instanceof Fallback) {

            throw new ServiceUnavailableException(
                    ((Fallback) customer).getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }

        return false;
    }


    private boolean canAccessProduct(Long productId) {

        boolean statusCheck = true;

        Product product = productService.getById(productId, statusCheck);
        Long organizationId = product.getOrganizationId();

        if (canAccessOrganization(organizationId)) {

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

        Long ownerId = (Long) clientsService
                .getOrganizationOwnerIdById(organizationId);

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

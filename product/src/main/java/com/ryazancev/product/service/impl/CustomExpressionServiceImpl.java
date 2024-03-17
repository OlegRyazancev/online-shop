package com.ryazancev.product.service.impl;

import com.ryazancev.common.config.ServiceStage;
import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.product.ProductEditDto;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ClientsService;
import com.ryazancev.product.service.CustomExpressionService;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.RequestHeader;
import com.ryazancev.product.util.exception.custom.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final ProductService productService;
    private final ClientsService clientsService;
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
                            "exception.product.email_not_confirmed",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessProduct(Long id) {

        if (!canAccessProduct(id)) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.product.access_object",
                            new Object[]{
                                    ServiceStage.PRODUCT,
                                    id
                            },
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessOrganization(ProductEditDto productEditDto) {

        if (!canAccessOrganization(productEditDto.getOrganizationId())) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.product.access_object",
                            new Object[]{
                                    ServiceStage.ORGANIZATION,
                                    productEditDto.getOrganizationId()
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
                            "exception.product.account_locked",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void checkAccessPurchase(String purchaseId) {

        RequestHeader requestHeader = new RequestHeader(request);

        PurchaseDto purchaseDto = (PurchaseDto) clientsService
                .getPurchaseById(purchaseId);

        CustomerDto customerDto = purchaseDto.getCustomer()
                .safelyCast(CustomerDto.class, true);

        boolean canAccessPurchase =
                requestHeader.getUserId().equals(customerDto.getId())
                        || requestHeader.getRoles().contains("ROLE_ADMIN");

        if (!canAccessPurchase) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.product.access_object",
                            new Object[]{
                                    ServiceStage.PURCHASE,
                                    purchaseId
                            },
                            Locale.getDefault()
                    ),
                    HttpStatus.FORBIDDEN);
        }
    }

    private boolean canAccessProduct(Long productId) {

        RequestHeader requestHeader = new RequestHeader(request);

        boolean statusCheck = true;

        Product product = productService.getById(productId, statusCheck);
        Long organizationId = product.getOrganizationId();

        if (canAccessOrganization(organizationId)) {

            List<Product> products =
                    productService.getByOrganizationId(organizationId);

            return products
                    .stream()
                    .anyMatch(productDto ->
                            productDto.getId().equals(productId))
                    || requestHeader.getRoles().contains("ROLE_ADMIN");
        }

        return false;
    }

    private boolean canAccessOrganization(Long organizationId) {

        RequestHeader requestHeader = new RequestHeader(request);

        Long ownerId = (Long) clientsService
                .getOrganizationOwnerIdById(organizationId);

        return requestHeader.getUserId().equals(ownerId)
                || requestHeader.getRoles().contains("ROLE_ADMIN");
    }
}

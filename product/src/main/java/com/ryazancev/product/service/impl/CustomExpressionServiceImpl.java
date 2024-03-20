package com.ryazancev.product.service.impl;

import com.ryazancev.common.dto.admin.enums.ObjectType;
import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.product.ProductEditDto;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.product.model.Product;
import com.ryazancev.product.service.ClientsService;
import com.ryazancev.product.service.CustomExpressionService;
import com.ryazancev.product.service.ProductService;
import com.ryazancev.product.util.RequestHeader;
import com.ryazancev.product.util.exception.CustomExceptionFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Service
@RequiredArgsConstructor
public class CustomExpressionServiceImpl implements CustomExpressionService {

    private final ProductService productService;
    private final ClientsService clientsService;
    private final HttpServletRequest request;
    private final MessageSource messageSource;

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
                    .emailNotConfirmed(messageSource);
        }
    }

    @Override
    public void checkAccessProduct(final Long id) {

        if (!canAccessProduct(id)) {
            throw CustomExceptionFactory
                    .getAccessDenied()
                    .cannotAccessObject(
                            messageSource,
                            ObjectType.PRODUCT,
                            String.valueOf(id)
                    );
        }
    }

    @Override
    public void checkAccessOrganization(final ProductEditDto productEditDto) {

        if (!canAccessOrganization(productEditDto.getOrganizationId())) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .cannotAccessObject(
                            messageSource,
                            ObjectType.ORGANIZATION,
                            String.valueOf(productEditDto.getOrganizationId())
                    );
        }
    }

    @Override
    public void checkIfAccountLocked() {

        RequestHeader requestHeader = new RequestHeader(request);

        if (requestHeader.isLocked()) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .accountLocked(messageSource);
        }
    }

    @Override
    public void checkAccessPurchase(final String purchaseId) {

        RequestHeader requestHeader = new RequestHeader(request);

        PurchaseDto purchaseDto = (PurchaseDto) clientsService
                .getPurchaseById(purchaseId);

        CustomerDto customerDto = purchaseDto.getCustomer()
                .safelyCast(CustomerDto.class, true);

        boolean canAccessPurchase =
                requestHeader.getUserId().equals(customerDto.getId())
                        || requestHeader.getRoles().contains("ROLE_ADMIN");

        if (!canAccessPurchase) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .cannotAccessObject(
                            messageSource,
                            ObjectType.PURCHASE,
                            purchaseId
                    );
        }
    }

    private boolean canAccessProduct(final Long productId) {

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

    private boolean canAccessOrganization(final Long organizationId) {

        RequestHeader requestHeader = new RequestHeader(request);

        Long ownerId = (Long) clientsService
                .getOwnerByOrganizationId(organizationId);

        return requestHeader.getUserId().equals(ownerId)
                || requestHeader.getRoles().contains("ROLE_ADMIN");
    }
}

package com.ryazancev.product.service;

import com.ryazancev.dto.product.ProductEditDto;

public interface CustomExpressionService {

    void checkAccountPermissions();

    void checkAccessProduct(Long id);

    void checkAccessOrganization(ProductEditDto productEditDto);

    void checkIfAccountLocked();

    void checkAccessPurchase(String purchaseId);
}

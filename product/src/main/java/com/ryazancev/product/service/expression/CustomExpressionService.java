package com.ryazancev.product.service.expression;

import com.ryazancev.dto.product.ProductEditDto;

public interface CustomExpressionService {

    void checkAccessProduct(Long id);

    void checkAccessOrganization(ProductEditDto productEditDto);

    void checkIfAccountLocked();

    void checkIfEmailConfirmed();

    void checkAccessPurchase(String purchaseId);
}

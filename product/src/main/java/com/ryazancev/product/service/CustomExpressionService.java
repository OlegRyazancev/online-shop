package com.ryazancev.product.service;

import com.ryazancev.common.dto.product.ProductEditDto;

/**
 * @author Oleg Ryazancev
 */

public interface CustomExpressionService {

    void checkAccountConditions();

    void checkAccessProduct(Long id);

    void checkAccessOrganization(ProductEditDto productEditDto);

    void checkIfAccountLocked();

    void checkAccessPurchase(String purchaseId);
}

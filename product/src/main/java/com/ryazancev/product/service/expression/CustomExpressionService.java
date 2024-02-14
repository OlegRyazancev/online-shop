package com.ryazancev.product.service.expression;

import com.ryazancev.dto.product.ProductEditDTO;

public interface CustomExpressionService {

    void checkAccessProduct(Long id);

    void checkAccessOrganization(ProductEditDTO productEditDTO);

    void checkIfAccountLocked();
}

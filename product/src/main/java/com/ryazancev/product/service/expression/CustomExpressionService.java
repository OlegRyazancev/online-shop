package com.ryazancev.product.service.expression;

public interface CustomExpressionService {


    boolean canAccessOrganization(Long organizationId);


    boolean canAccessProduct(Long productId, Long organizationId);
}

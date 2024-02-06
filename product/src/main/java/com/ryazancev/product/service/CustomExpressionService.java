package com.ryazancev.product.service;

public interface CustomExpressionService {


    boolean canAccessOrganization(Long organizationId);


    boolean canAccessProduct(Long productId, Long organizationId);
}

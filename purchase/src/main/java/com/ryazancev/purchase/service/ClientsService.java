package com.ryazancev.purchase.service;

import com.ryazancev.common.dto.Element;

/**
 * @author Oleg Ryazancev
 */

public interface ClientsService {

    Element getSimpleCustomerById(Long customerId);

    Element getSimpleProductById(Long productId);

    Object getCustomerBalance(Long customerId);

    Object getProductPriceAndQuantity(Long productId);

    Object getProductOwnerId(Long productId);
}

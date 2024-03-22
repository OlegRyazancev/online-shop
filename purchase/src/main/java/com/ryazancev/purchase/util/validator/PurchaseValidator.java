package com.ryazancev.purchase.util.validator;

import com.ryazancev.purchase.util.exception.CustomExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class PurchaseValidator {

    public void
    validateSufficientBalance(final Double availableCustomerBalance,
                              final Double selectedProductPrice) {

        if (availableCustomerBalance < selectedProductPrice) {

            throw CustomExceptionFactory
                    .getIncorrectBalance()
                    .insufficientFunds();
        }
    }

    public void
    validateProductAvailability(final Integer availableProductsInStock) {

        if (availableProductsInStock == 0) {

            throw CustomExceptionFactory
                    .getOutOfStock()
                    .noProducts();
        }
    }
}

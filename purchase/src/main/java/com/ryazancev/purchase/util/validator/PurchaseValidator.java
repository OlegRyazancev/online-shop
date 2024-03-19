package com.ryazancev.purchase.util.validator;

import com.ryazancev.purchase.util.exception.custom.IncorrectBalanceException;
import com.ryazancev.purchase.util.exception.custom.OutOfStockException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class PurchaseValidator {

    private final MessageSource messageSource;

    public void
    validateSufficientBalance(final Double availableCustomerBalance,
                              final Double selectedProductPrice) {

        if (availableCustomerBalance < selectedProductPrice) {
            throw new IncorrectBalanceException(
                    messageSource.getMessage(
                            "exception.purchase.insufficient_funds",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void
    validateProductAvailability(final Integer availableProductsInStock) {

        if (availableProductsInStock == 0) {
            throw new OutOfStockException(
                    messageSource.getMessage(
                            "exception.purchase.no_products_in_stock",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.CONFLICT
            );
        }
    }
}

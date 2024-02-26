package com.ryazancev.purchase.util.validator;

import com.ryazancev.purchase.util.exception.custom.IncorrectBalanceException;
import com.ryazancev.purchase.util.exception.custom.OutOfStockException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static com.ryazancev.purchase.util.exception.Message.INSUFFICIENT_FUNDS;
import static com.ryazancev.purchase.util.exception.Message.NO_PRODUCTS_IN_STOCK;

@Component
public class PurchaseValidator {


    public void
    validateSufficientBalance(Double availableCustomerBalance,
                              Double selectedProductPrice) {

        if (availableCustomerBalance < selectedProductPrice) {
            throw new IncorrectBalanceException(
                    INSUFFICIENT_FUNDS,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void
    validateProductAvailability(Integer availableProductsInStock) {

        if (availableProductsInStock == 0) {
            throw new OutOfStockException(
                    NO_PRODUCTS_IN_STOCK,
                    HttpStatus.CONFLICT
            );
        }
    }

}

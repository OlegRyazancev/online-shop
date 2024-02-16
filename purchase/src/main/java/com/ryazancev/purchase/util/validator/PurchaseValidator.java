package com.ryazancev.purchase.util.validator;

import com.ryazancev.purchase.model.Purchase;
import com.ryazancev.purchase.util.exception.custom.IncorrectBalanceException;
import com.ryazancev.purchase.util.exception.custom.OutOfStockException;
import com.ryazancev.purchase.util.exception.custom.PurchasesNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PurchaseValidator {


    public void
    validateSufficientBalance(Double availableCustomerBalance,
                              Double selectedProductPrice) {

        if (availableCustomerBalance < selectedProductPrice) {
            throw new IncorrectBalanceException(
                    "Customer doesn't have enough money to purchase the product",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public void
    validateProductAvailability(Integer availableProductsInStock) {

        if (availableProductsInStock == 0) {
            throw new OutOfStockException(
                    "No available products in stock",
                    HttpStatus.CONFLICT
            );
        }
    }

    public void
    validatePurchasesExist(List<Purchase> purchases) {

        if (purchases.isEmpty()) {
            throw new PurchasesNotFoundException(
                    "No purchases found for customer with this ID",
                    HttpStatus.NOT_FOUND
            );
        }
    }
}

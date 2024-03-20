package com.ryazancev.purchase.util.exception;

import com.ryazancev.purchase.util.exception.custom.IncorrectBalanceException;
import com.ryazancev.purchase.util.exception.custom.OutOfStockException;
import com.ryazancev.purchase.util.exception.custom.PurchaseNotFoundException;

/**
 * @author Oleg Ryazancev
 */

public class CustomExceptionFactory {

    public static IncorrectBalanceException getIncorrectBalance() {

        return new IncorrectBalanceException();
    }

    public static OutOfStockException getOutOfStock() {

        return new OutOfStockException();
    }

    public static PurchaseNotFoundException getPurchaseNotFound() {

        return new PurchaseNotFoundException();
    }
}

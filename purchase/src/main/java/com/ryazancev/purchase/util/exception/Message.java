package com.ryazancev.purchase.util.exception;

/**
 * @author Oleg Ryazancev
 */

public class Message {

    public static final String CUSTOMER_SERVICE_UNAVAILABLE =
            "The customer service is currently unavailable. " +
                    "Please try again later";

    public static final String PRODUCT_SERVICE_UNAVAILABLE =
            "The product service is currently unavailable. " +
                    "Please try again later";
    public static final String PURCHASE_NOT_FOUND =
            "Purchase not found";

    public static final String INSUFFICIENT_FUNDS =
            "Customer doesn't have enough money to purchase the product";
    public static final String NO_PRODUCTS_IN_STOCK =
            "No available products in stock";

}

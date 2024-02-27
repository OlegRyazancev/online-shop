package com.ryazancev.review.util.exception;

/**
 * @author Oleg Ryazancev
 */

public class Message {
    public static final String DUPLICATE_REVIEW =
            "A review with the same purchase ID already exists";

    public static final String CUSTOMER_SERVICE_UNAVAILABLE =
            "The customer service is currently unavailable. " +
                    "Please try again later";

    public static final String PRODUCT_SERVICE_UNAVAILABLE =
            "The product service is currently unavailable. " +
                    "Please try again later";

    public static final String PURCHASE_SERVICE_UNAVAILABLE =
            "The purchase service is currently unavailable. " +
                    "Please try again later";
}

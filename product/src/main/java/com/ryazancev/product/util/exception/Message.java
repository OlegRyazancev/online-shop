package com.ryazancev.product.util.exception;

/**
 * @author Oleg Ryazancev
 */

public class Message {

    public static final String ACCESS_PRODUCT =
            "You have no permissions to access to this product";

    public static final String ACCESS_ORGANIZATION =
            "You have no permissions to access to this organization";

    public static final String ACCOUNT_LOCKED =
            "Access denied because your account is locked";

    public static final String EMAIL_NOT_CONFIRMED =
            "Access denied because your email is not confirmed";

    public static final String ACCESS_PURCHASE =
            "You have no permissions to access to this purchase";

    public static final String ORGANIZATION_NOT_FOUND =
            "Organization not found";

    public static final String NAME_EXISTS =
            "Product with this name already exists";

    public static final String PRODUCT_NOT_FOUND =
            "Product not found";

    public static final String ORGANIZATION_SERVICE_UNAVAILABLE =
            "The organization service is currently unavailable. " +
                    "Please try again later";

    public static final String REVIEW_SERVICE_UNAVAILABLE =
            "The review service is currently unavailable. " +
                    "Please try again later";
}

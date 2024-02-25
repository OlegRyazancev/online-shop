package com.ryazancev.customer.util.exception;


public class Message {

    public static final String ACCESS_CUSTOMER =
            "You do not have permission to access this customer";

    public static final String ACCOUNT_LOCKED =
            "Access denied because your account is locked";

    public static final String CUSTOMER_EMAIL_EXISTS =
            "A customer with this email already exists";

    public static final String CUSTOMER_ID_NOT_FOUND =
            "Customer not found with this ID";

    public static final String EMAIL_NOT_CONFIRMED =
            "Access denied because your email has not been confirmed";

    public static final String PURCHASE_SERVICE_UNAVAILABLE =
            "The purchase service is currently unavailable. Please try again later";

    public static final String REVIEW_SERVICE_UNAVAILABLE =
            "The review service is currently unavailable. Please try again later";

}

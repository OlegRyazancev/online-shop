package com.ryazancev.customer.util.exception;


public class Messages {

    //    Customer service messages
    public static final String CUSTOMER_EMAIL_EXISTS =
            "Customer with this email already exists";
    public static final String CUSTOMER_ID_NOT_FOUND =
            "Customer not found with this ID";

    //    Fallback messages
    public static final String REVIEW_SERVICE_UNAVAILABLE =
            "Review service is unavailable. Try again after some seconds";
    public static final String PURCHASE_SERVICE_UNAVAILABLE =
            "Review service is unavailable. Try again after some seconds";

    //    Custom expression messages
    public static final String EMAIL_NOT_CONFIRMED =
            "Access denied because your email is not confirmed";
    public static final String ACCESS_CUSTOMER =
            "You have no permissions to access to this customer";
    public static final String ACCOUNT_LOCKED =
            "Access denied because your account is locked";

}

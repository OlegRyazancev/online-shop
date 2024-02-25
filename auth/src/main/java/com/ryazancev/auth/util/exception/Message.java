package com.ryazancev.auth.util.exception;

/**
 * @author Oleg Ryazancev
 */

public class Message {

    public static final String CUSTOMER_SERVICE_UNAVAILABLE =
            "Customer service is currently unavailable. Please try again later";

    public static final String DELETED_ACCOUNT_FORMAT =
            "Access denied. We're sorry %s, but this account was deleted on %s";

    public static final String EMAIL_CONFIRMED =
            "This email address has already been confirmed";

    public static final String EMAIL_EXISTS =
            "A user with this email address already exists";

    public static final String PASSWORD_MISMATCH =
            "The password and password confirmation do not match";

    public static final String TOKEN_EXPIRED =
            "The token has expired";

    public static final String TOKEN_NOT_FOUND =
            "The token was not found";

    public static final String USER_NOT_FOUND =
            "The user was not found";

}

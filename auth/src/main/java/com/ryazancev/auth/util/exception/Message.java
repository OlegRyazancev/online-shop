package com.ryazancev.auth.util.exception;

/**
 * @author Oleg Ryazancev
 */

public class Message {

    public static String TOKEN_NOT_FOUND =
            "Token not found";
    public static String TOKEN_EXPIRED =
            "Token expired";
    public static String USER_NOT_FOUND =
            "User not found";
    public static final String DELETED_ACCOUNT_FORMAT =
            "Access denied. Sorry %s, but this account was deleted at %s";
    public static final String EMAIL_CONFIRMED =
            "Email already confirmed";
    public static final String EMAIL_EXISTS =
            "User with this email already exists";
    public static final String PASSWORD_MISMATCH =
            "Password and password confirmation do not match";

    public static final String CUSTOMER_SERVICE_UNAVAILABLE=
            "Customer service is unavailable. Try again after some seconds";
}

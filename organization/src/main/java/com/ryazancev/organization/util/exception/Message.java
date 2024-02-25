package com.ryazancev.organization.util.exception;

/**
 * @author Oleg Ryazancev
 */

public class Message {

    public static final String ACCESS_ORGANIZATION =
            "You have no permission to access to this organization";
    public static final String ACCESS_CUSTOMER =
            "You have no permission to access to this customer";
    public static final String EMAIL_NOT_CONFIRMED =
            "Access denied because your email is not confirmed";
    public static final String ACCOUNT_LOCKED =
            "Access denied because your account is locked";
    public static final String NAME_EXISTS =
            "Organization with this name already exists";
    public static final String DESCRIPTION_EXISTS =
            "Organization with this description already exists";
    public static final String ORGANIZATION_NOT_FOUND =
            "Organization not found";
    public static final String ORGANIZATION_FROZEN =
            "Access denied. Organization is frozen";
    public static final String ORGANIZATION_INACTIVE =
            "Access denied. Organization is inactive";
    public static final String ORGANIZATION_DELETED =
            "Access denied. Organization is inactive";
    public static final String LOGO_SERVICE_UNAVAILABLE =
            "Logo service is unavailable. Try again after some seconds";
    public static final String PRODUCT_SERVICE_UNAVAILABLE =
            "Product service is unavailable. Try again after some seconds";
}

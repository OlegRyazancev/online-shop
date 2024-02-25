package com.ryazancev.organization.util.exception;

/**
 * @author Oleg Ryazancev
 */

public class Message {

    public static final String ACCESS_CUSTOMER =
            "You do not have permission to access this customer";

    public static final String ACCESS_ORGANIZATION =
            "You do not have permission to access this organization";

    public static final String ACCOUNT_LOCKED =
            "Access denied because your account is locked";

    public static final String DESCRIPTION_EXISTS =
            "An organization with this description already exists";

    public static final String EMAIL_NOT_CONFIRMED =
            "Access denied because your email has not been confirmed";

    public static final String LOGO_SERVICE_UNAVAILABLE =
            "The logo service is currently unavailable. " +
                    "Please try again later";

    public static final String NAME_EXISTS =
            "An organization with this name already exists";

    public static final String ORGANIZATION_DELETED =
            "Access denied. The organization has been deleted";

    public static final String ORGANIZATION_FROZEN =
            "Access denied. The organization is frozen";

    public static final String ORGANIZATION_INACTIVE =
            "Access denied. The organization is inactive";

    public static final String ORGANIZATION_NOT_FOUND =
            "Organization not found";

    public static final String PRODUCT_SERVICE_UNAVAILABLE =
            "The product service is currently unavailable. " +
                    "Please try again later";

}

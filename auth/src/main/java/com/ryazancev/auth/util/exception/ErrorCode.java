package com.ryazancev.auth.util.exception;

/**
 * @author Oleg Ryazancev
 */

public enum ErrorCode {
    AUTH_SERVICE_DELETED_ACCOUNT,
    AUTH_SERVICE_EMAIl_CONFIRMED,
    AUTH_SERVICE_TOKEN_EXPIRED,
    AUTH_SERVICE_NOT_FOUND,
    AUTH_SERVICE_EMAIL_EXISTS,
    AUTH_SERVICE_PASSWORD_MISMATCH,
    AUTH_SERVICE_INVALID_REFRESH,
    AUTH_SERVICE_SECURITY_ACCESS_DENIED,
    AUTH_SERVICE_SECURITY_CONSTRAINT_VIOLATION,
    AUTH_SERVICE_SECURITY_METHOD_ARGUMENT_NOT_VALID,
    AUTH_SERVICE_SECURITY_UNAUTHORIZED,
    INTERNAL
}

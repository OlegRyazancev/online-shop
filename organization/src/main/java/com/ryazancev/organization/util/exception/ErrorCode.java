package com.ryazancev.organization.util.exception;

import jakarta.annotation.Resource;

/**
 * @author Oleg Ryazancev
 */

public enum ErrorCode {
    ACCESS_DENIED_EMAIL,
    ACCESS_DENIED_OBJECT,
    ACCESS_DENIED_ACCOUNT,
    ORGANIZATION_CREATION_NAME,
    ORGANIZATION_CREATION_DESCRIPTION,
    ORGANIZATION_NOT_FOUND_BY_ID,
    ACCESS_DENIED_STATUS,
    CONSTRAINT_VIOLATION,
    METHOD_ARGUMENT_NOT_VALID,
    INTERNAL
}

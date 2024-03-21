package com.ryazancev.admin.util.exception;

import jakarta.annotation.Resource;

/**
 * @author Oleg Ryazancev
 */

public enum ErrorCode {
    REQUEST_NOT_FOUND_BY_ID, INVALID_STATUS,
    CONSTRAINT_VIOLATION,
    METHOD_ARGUMENT_NOT_VALID,
    INTERNAL
}

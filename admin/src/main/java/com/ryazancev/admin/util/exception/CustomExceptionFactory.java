package com.ryazancev.admin.util.exception;

import com.ryazancev.admin.util.exception.custom.InvalidRequestStatusException;
import com.ryazancev.admin.util.exception.custom.RequestNotFoundException;

/**
 * @author Oleg Ryazancev
 */

public class CustomExceptionFactory {

    public static InvalidRequestStatusException getInvalidRequestStatus() {

        return new InvalidRequestStatusException();
    }

    public static RequestNotFoundException getRequestNotFound() {

        return new RequestNotFoundException();
    }
}

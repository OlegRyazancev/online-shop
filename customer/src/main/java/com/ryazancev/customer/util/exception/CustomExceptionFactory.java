package com.ryazancev.customer.util.exception;

import com.ryazancev.customer.util.exception.custom.AccessDeniedException;
import com.ryazancev.customer.util.exception.custom.CustomerCreationException;
import com.ryazancev.customer.util.exception.custom.CustomerNotFoundException;

/**
 * @author Oleg Ryazancev
 */

public class CustomExceptionFactory {

    public static AccessDeniedException getAccessDenied() {

        return new AccessDeniedException();
    }

    public static CustomerCreationException getCustomerCreation() {

        return new CustomerCreationException();
    }

    public static CustomerNotFoundException getCustomerNotFound() {

        return new CustomerNotFoundException();
    }
}

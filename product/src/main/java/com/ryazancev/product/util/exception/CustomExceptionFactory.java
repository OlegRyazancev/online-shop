package com.ryazancev.product.util.exception;

import com.ryazancev.product.util.exception.custom.AccessDeniedException;
import com.ryazancev.product.util.exception.custom.ProductCreationException;
import com.ryazancev.product.util.exception.custom.ProductNotFoundException;

/**
 * @author Oleg Ryazancev
 */

public class CustomExceptionFactory {


    public static AccessDeniedException getAccessDenied() {

        return new AccessDeniedException();
    }

    public static ProductCreationException getProductCreation() {

        return new ProductCreationException();
    }

    public static ProductNotFoundException getProductNotFound() {

        return new ProductNotFoundException();
    }
}

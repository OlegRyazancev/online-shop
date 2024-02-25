package com.ryazancev.product.util.validator;

import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.util.exception.custom.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

    public void validateFrozenStatus(Product product) {

        if (product.getStatus().equals(ProductStatus.FROZEN)) {

            throw new AccessDeniedException(
                    "Access denied. Product is frozen",
                    HttpStatus.CONFLICT);
        }
    }

    public void validateInactiveStatus(Product product) {

        if (product.getStatus().equals(ProductStatus.INACTIVE)) {

            throw new AccessDeniedException(
                    "Access denied. Product is inactive",
                    HttpStatus.CONFLICT);
        }
    }

    public void validateDeletedStatus(Product product) {

        if (product.getStatus().equals(ProductStatus.DELETED)) {

            throw new AccessDeniedException(
                    "Access denied. Product is deleted",
                    HttpStatus.CONFLICT);
        }
    }

    public void validateAllStatus(Product product) {

        validateDeletedStatus(product);
        validateInactiveStatus(product);
        validateFrozenStatus(product);
    }
}

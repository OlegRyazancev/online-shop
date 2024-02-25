package com.ryazancev.product.util.validator;

import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.util.exception.custom.AccessDeniedException;
import com.ryazancev.product.util.exception.custom.ProductCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static com.ryazancev.product.util.exception.Message.NAME_EXISTS;

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository productRepository;

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

    public void validateNameUniqueness(Product product) {

        if (productRepository.findByProductName(
                product.getProductName()).isPresent()) {
            throw new ProductCreationException(
                    NAME_EXISTS,
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}

package com.ryazancev.product.util.validator;

import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.util.exception.custom.AccessDeniedException;
import com.ryazancev.product.util.exception.custom.ProductCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository productRepository;
    private final MessageSource messageSource;

    public void validateStatus(Product product, ProductStatus status) {

        if (product.getStatus() == status) {

            throw new AccessDeniedException(
                    messageSource.getMessage(
                            "exception.product.status_access",
                            new Object[]{product.getStatus()},
                            Locale.getDefault()
                    ),
                    HttpStatus.CONFLICT);
        }
    }


    public void validateAllStatus(Product product) {

        validateStatus(product, ProductStatus.DELETED);
        validateStatus(product, ProductStatus.FROZEN);
        validateStatus(product, ProductStatus.INACTIVE);
    }

    public void validateNameUniqueness(Product product) {

        if (productRepository.findByProductName(
                product.getProductName()).isPresent()) {

            throw new ProductCreationException(
                    messageSource.getMessage(
                            "exception.product.name_exists",
                            null,
                            Locale.getDefault()
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}

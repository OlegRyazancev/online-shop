package com.ryazancev.product.util.validator;

import com.ryazancev.product.model.Product;
import com.ryazancev.product.model.ProductStatus;
import com.ryazancev.product.repository.ProductRepository;
import com.ryazancev.product.util.exception.CustomExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository productRepository;
    private final MessageSource messageSource;

    public void validateStatus(final Product product,
                               final ProductStatus status) {

        if (product.getStatus() == status) {

            throw CustomExceptionFactory
                    .getAccessDenied()
                    .statusAccess(
                            messageSource,
                            product.getStatus()
                    );
        }
    }


    public void validateAllStatus(final Product product) {

        validateStatus(product, ProductStatus.DELETED);
        validateStatus(product, ProductStatus.FROZEN);
        validateStatus(product, ProductStatus.INACTIVE);
    }

    public void validateNameUniqueness(final Product product) {

        if (productRepository.findByProductName(
                product.getProductName()).isPresent()) {

            throw CustomExceptionFactory
                    .getProductCreation()
                    .nameExists(messageSource);
        }
    }
}

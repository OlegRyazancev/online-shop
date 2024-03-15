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
import static com.ryazancev.product.util.exception.Message.PRODUCT_STATUS_ACCESS;

/**
 * @author Oleg Ryazancev
 */

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository productRepository;

    public void validateStatus(Product product, ProductStatus status) {

        if (product.getStatus() == status) {

            throw new AccessDeniedException(
                    String.format(
                            PRODUCT_STATUS_ACCESS,
                            product.getStatus().name()
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
                    NAME_EXISTS,
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}

package com.ryazancev.common.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.organization.OrganizationDto;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;

/**
 * @author Oleg Ryazancev
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = ProductDto.class,
                name = "product"
        ),
        @JsonSubTypes.Type(
                value = CustomerDto.
                        class, name = "customer"
        ),
        @JsonSubTypes.Type(
                value = OrganizationDto.class,
                name = "organization"
        ),
        @JsonSubTypes.Type(
                value = PurchaseDto.class,
                name = "purchase"
        ),
        @JsonSubTypes.Type(
                value = Fallback.class,
                name = "fallback"
        )
})
public interface Element {

    default <T> T safelyCast(Class<T> targetType, boolean throwException) {

        if (this instanceof Fallback) {
            if (throwException) {
                throw new ServiceUnavailableException(
                        ((Fallback) this).getMessage(),
                        HttpStatus.SERVICE_UNAVAILABLE);
            } else {
                String message = "SERVICE_UNAVAILABLE";

                if (targetType.equals(OrganizationDto.class)) {

                    return targetType.cast(
                            OrganizationDto.builder()
                                    .id(-1L)
                                    .name(message)
                                    .build()
                    );
                } else if (targetType.equals(ProductDto.class)) {

                    return targetType.cast(
                            ProductDto.builder()
                                    .id(-1L)
                                    .productName(message)
                                    .price(-1.0)
                                    .build()
                    );
                }
            }
        }
        return targetType.cast(this);
    }
}

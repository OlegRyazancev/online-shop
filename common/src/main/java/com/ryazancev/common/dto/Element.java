package com.ryazancev.common.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ryazancev.common.dto.customer.CustomerDto;
import com.ryazancev.common.dto.organization.OrganizationDto;
import com.ryazancev.common.dto.product.ProductDto;
import com.ryazancev.common.dto.purchase.PurchaseDto;
import com.ryazancev.common.exception.ServiceUnavailableException;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductDto.class, name = "product"),
        @JsonSubTypes.Type(value = CustomerDto.class, name = "customer"),
        @JsonSubTypes.Type(value = Fallback.class, name = "fallback"),
        @JsonSubTypes.Type(value = OrganizationDto.class, name = "organization"),
        @JsonSubTypes.Type(value = PurchaseDto.class, name = "purchase")
})
public interface Element {

    default <T> T safelyCast(Class<T> targetType) {

        if (this instanceof Fallback) {
            throw new ServiceUnavailableException(
                    ((Fallback) this).getMessage(),
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
        return targetType.cast(this);
    }
}

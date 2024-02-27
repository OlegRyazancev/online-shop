package com.ryazancev.common.dto.product;

import com.ryazancev.common.validation.OnCreate;
import com.ryazancev.common.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

/**
 * @author Oleg Ryazancev
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Product Edit DTO. Used in PUT, POST methods ")
public class ProductEditDto {

    @Schema(
            description = "Product ID",
            example = "1"
    )
    @NotNull(
            message = "Id must be not null",
            groups = OnUpdate.class
    )
    private Long id;

    @Schema(
            description = "Name of the product",
            example = "Example name"
    )
    @NotNull(
            message = "Name must be not null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Size(
            min = 3,
            max = 50,
            message = "Product name must be between 3 and 50 characters",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String productName;

    @Schema(
            description = "Description of the product",
            example = "Example description of good product"
    )
    @NotNull(
            message = "Description must be not null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Size(
            min = 10,
            max = 500,
            message = "Description must be between 10 and 500 characters",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String description;

    @Schema(
            description = "ID of the issuer of product",
            example = "1"
    )
    @NotNull(
            message = "Organization ID must not be null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Long organizationId;

    @NotNull(
            message = "Price must be not null",
            groups = {OnCreate.class, OnUpdate.class}
    )

    @Schema(
            description = "Price of the product",
            example = "123.2"
    )
    @Positive(
            message = "Price must be greater than  0",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Price must have at most 2 decimal places",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Double price;

    @Schema(
            description = "Quantity in stock of the product",
            example = "123"
    )
    @NotNull(
            message = "Quantity in stock must be not null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @PositiveOrZero(
            message = "Quantity must be greater than or equal to 0",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Integer quantityInStock;

    @Schema(
            description = "Keywords associated with the product",
            example = "[\"electronics\", \"gadget\", \"smartphone\"]"
    )
    @NotNull(
            message = "Keywords list must not be null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @NotEmpty(
            message = "Keywords list must not be empty",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Size(
            min = 2,
            max = 10,
            message = "Keywords list must have between 2 and 10 elements",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private List<String> keywords;
}

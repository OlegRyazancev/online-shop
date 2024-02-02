package com.ryazancev.dto.product;

import com.ryazancev.validation.OnCreate;
import com.ryazancev.validation.OnUpdate;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductEditDTO {

    @NotNull(message = "Id must be not null",
            groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "Name must be not null",
            groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 3,
            max = 50,
            message = "Product name must be between 3 and 50 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String productName;

    @NotNull(message = "Description must be not null",
            groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 10,
            max = 500,
            message = "Description must be between 10 and 500 characters",
            groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotNull(message = "Organization ID must not be null")
    private Long organizationId;

    @NotNull(message = "Price must be not null",
            groups = {OnCreate.class, OnUpdate.class})

    @Positive(message = "Price must be greater than  0",
            groups = {OnCreate.class, OnUpdate.class})
    @Digits(integer = 10,
            fraction = 2,
            message = "Price must have at most 2 decimal places",
            groups = {OnCreate.class, OnUpdate.class})
    private Double price;

    @NotNull(message = "Quantity in stock must be not null",
            groups = {OnCreate.class, OnUpdate.class})
    @PositiveOrZero(message = "Quantity must be greater than or equal to 0",
            groups = {OnCreate.class, OnUpdate.class})
    private Integer quantityInStock;

    @NotNull(message = "Keywords list must not be null",
            groups = {OnCreate.class, OnUpdate.class})
    @NotEmpty(message = "Keywords list must not be empty",
            groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 2,
            max = 10,
            message = "Keywords list must have between 2 and 10 elements",
            groups = {OnCreate.class, OnUpdate.class})
    private List<String> keywords;
}

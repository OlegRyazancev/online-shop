package com.ryazancev.common.dto.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.common.dto.Element;
import com.ryazancev.common.validation.OnCreate;
import com.ryazancev.common.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Customer DTO")
public class CustomerDto implements Element {

    @Schema(
            description = "ID of the customer",
            example = "1"
    )
    @NotNull(
            message = "Id must not be null",
            groups = {OnUpdate.class}
    )
    private Long id;


    @Schema(
            description = "Username of the customer",
            example = "John Doe"
    )
    @NotNull(
            message = "Username must not be null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Length(
            max = 255,
            message = "Username length must be smaller than 255 symbols",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String username;

    @Schema(
            description = "Email of the customer",
            example = "john@example.com"
    )
    @NotNull(
            message = "Email must not be null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Email(
            message = "Email must be in correct form",
            regexp = "^[A-Za-z0-9+_.-]+@(.+)$",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Length(
            max = 255,
            message = "Email length must be smaller than 255 symbols",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String email;

    @Schema(
            description = "Balance of the customer",
            example = "1000.1"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Balance must have at most 2 decimal places",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @PositiveOrZero(
            message = "Balance must be a positive number or zero",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private Double balance;

}

package com.ryazancev.dto.customer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryazancev.validation.OnCreate;
import com.ryazancev.validation.OnUpdate;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO implements Serializable {

    @NotNull(message = "Id must not be null",
            groups = {OnUpdate.class})
    private Long id;


    @NotNull(message = "Username must not be null",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255,
            message = "Username length must be smaller than 255 symbols",
            groups = {OnCreate.class, OnUpdate.class})
    private String username;

    @NotNull(message = "Email must not be null",
            groups = {OnCreate.class, OnUpdate.class})
    @Email(message = "Email must be in correct form",
            regexp = "^[A-Za-z0-9+_.-]+@(.+)$",
            groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255,
            message = "Email length must be smaller than 255 symbols",
            groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @Digits(integer = 10,
            fraction = 2,
            message = "Balance must have at most 2 decimal places",
            groups = {OnCreate.class, OnUpdate.class})
    @PositiveOrZero(message = "Balance must be a positive number or zero",
            groups = {OnCreate.class, OnUpdate.class})
    private Double balance;

}

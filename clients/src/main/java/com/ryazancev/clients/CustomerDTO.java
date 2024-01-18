package com.ryazancev.clients;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustomerDTO {

    @NotNull(message = "Id must not be null")
    private Long id;

    @NotNull(message = "Name must not be null")
    @Length(max = 255,
            message = "Name length must be smaller than 255 symbols")
    private String username;

    @Email(message = "Email must be in correct form",
            regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @Length(max = 255,
            message = "Email length must be smaller than 255 symbols")
    private String email;

    @Min(value = 0,
            message = "Balance cannot be negative")
    private Double balance;

}

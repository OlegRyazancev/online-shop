package com.ryazancev.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Oleg Ryazancev
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request model for login into account")
public class JwtRequest {

    @Schema(
            description = "Email of the user/customer",
            example = "john@example.com"
    )
    @NotNull(message = "Email must be not null")
    private String email;

    @Schema(
            description = "Password of the user/customer account",
            example = "123"
    )
    @NotNull(message = "Password must be not null")
    private String password;
}

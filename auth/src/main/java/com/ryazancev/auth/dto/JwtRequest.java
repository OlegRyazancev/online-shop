package com.ryazancev.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRequest {

    @NotNull(message = "Email must be not null")
    private String email;

    @NotNull(message = "Password must be not null")
    private String password;
}

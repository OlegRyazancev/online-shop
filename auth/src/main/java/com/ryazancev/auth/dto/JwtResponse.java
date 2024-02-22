package com.ryazancev.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response model of success login")
public class JwtResponse {

    @Schema(
            description = "User ID",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Email of the user",
            example = "john@example.com"
    )
    private String email;

    @Schema(
            description = "Associated customer ID",
            example = "1"
    )
    private Long customerId;

    @Schema(description = "JWT access token")
    private String accessToken;

    @Schema(description = "JWT refresh token")
    private String refreshToken;

    @Schema(
            description = "Indicates whether the user is locked",
            example = "false"
    )
    private Boolean locked;

    @Schema(
            description = "Indicates whether the user's account " +
                    "is confirmed via email",
            example = "true"
    )
    private Boolean confirmed;
}

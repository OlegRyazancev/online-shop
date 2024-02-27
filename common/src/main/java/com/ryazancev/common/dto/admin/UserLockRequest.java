package com.ryazancev.common.dto.admin;

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
@Schema(description = "Request model for managing user lock status")
public class UserLockRequest {

    @Schema(
            description = "Username of the user to be managed",
            example = "john@example.com"
    )
    @NotNull(message = "Username must be not null")
    private String username;

    @Schema(
            description = "Lock status of the user",
            example = "true"
    )
    @NotNull(message = "Lock value must be not null")
    private boolean lock;
}

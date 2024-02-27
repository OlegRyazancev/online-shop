package com.ryazancev.common.dto.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request model for update user in auth service")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateRequest {


    @Schema(
            description = "Customer ID associated with user",
            example = "1"
    )

    private Long customerId;

    @Schema(
            description = "Name of the user",
            example = "John Doe"
    )
    private String name;

    @Schema(
            description = "Email of the user",
            example = "john@example.com"
    )
    private String email;

}

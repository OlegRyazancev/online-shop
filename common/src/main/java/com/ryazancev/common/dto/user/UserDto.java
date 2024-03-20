package com.ryazancev.common.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ryazancev.common.validation.OnCreate;
import com.ryazancev.common.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author Oleg Ryazancev
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "User DTO for auth service")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @Schema(
            description = "User ID",
            example = "1"
    )
    @NotNull(
            message = "Id must not be null",
            groups = OnUpdate.class
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(
            description = "Name of the user",
            example = "John Doe"
    )
    @NotNull(
            message = "Name must not be null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Length(
            max = 255,
            message = "Name length must be smaller than 255 symbols",
            groups = {OnCreate.class, OnUpdate.class}
    )
    private String name;

    @Schema(
            description = "Email of the user",
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
            description = "Password of the user",
            example = "12345"
    )
    @NotNull(
            message = "Password must be not null",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Schema(
            description = "Password confirmation of the user",
            example = "12345"
    )
    @NotNull(
            message = "Password confirmation must be not null",
            groups = OnCreate.class
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordConfirmation;

    @Schema(
            description = "Indicates whether the user is locked",
            example = "false"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean locked;

    @Schema(
            description = "Indicates whether the user's account "
                    + "is confirmed via email",
            example = "true"
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean confirmed;

}

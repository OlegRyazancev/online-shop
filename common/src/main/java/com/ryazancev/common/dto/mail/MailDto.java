package com.ryazancev.common.dto.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Properties;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Mail DTO")
public class MailDto {


    @Schema(
            description = "Email address of the recipient",
            example = "john@example.com"
    )
    @NotNull(message = "Email must not be null")
    private String email;

    @Schema(
            description = "Name of the recipient",
            example = "John Doe"
    )
    @NotNull(message = "Name must not be null")
    private String name;

    @Schema(
            description = "Type of a message",
            example = "USER_REGISTRATION"
    )
    @NotNull(message = "Type must not be null")
    private MailType type;

    @Schema(description = "Additional properties of the mail")
    private Properties properties;
}

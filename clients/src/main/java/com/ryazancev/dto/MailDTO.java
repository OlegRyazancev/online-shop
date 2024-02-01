package com.ryazancev.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Properties;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailDTO {

    @NotNull(message = "Email must not be null")
    private String email;

    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Type must not be null")
    private MailType type;

    private Properties properties;
}
